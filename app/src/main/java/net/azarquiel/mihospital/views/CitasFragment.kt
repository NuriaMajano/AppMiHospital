package net.azarquiel.mihospital.views


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.constraintlayout.widget.Constraints
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.rowcitas.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.adapter.CitasAdapter
import net.azarquiel.mihospital.model.Citas
import org.jetbrains.anko.*

class CitasFragment : Fragment() {

    private var cantidadcitas:Int =0
    private var nuevascitas:Int =0
    private var primeravez = true
    private lateinit var db: FirebaseFirestore
    private lateinit var adapterCitas: CitasAdapter
    private var citas: ArrayList<Citas> = ArrayList()
    private lateinit var rvcitas: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_citas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        rvcitas = view.findViewById(R.id.rvcitas) as RecyclerView
        initRV()
        setListener()
    }
    private fun initRV() {
        adapterCitas = CitasAdapter(activity!!.baseContext, R.layout.rowcitas)
        rvcitas.adapter = adapterCitas
        rvcitas.layoutManager = LinearLayoutManager(activity)
    }

    private fun setListener() {
        val docRef = db.collection("citas")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d(Constraints.TAG, "Listen citas failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                cantidadcitas = citas.size
                if (cantidadcitas > nuevascitas) {
                    if (primeravez) {
                        primeravez = false
                    }else{
                        enviarNotificacion()
                    }
                }
                nuevascitas = cantidadcitas
                adapterCitas.setCita(citas)
            } else {
                Log.d(Constraints.TAG, "Current data citas: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        citas.clear()
        documents.forEach { d ->
            val respuestas = d["respuestas"] as HashMap<Int, String>
            val id = d["id"] as String
            val resp = ArrayList(respuestas.values)
                citas.add(Citas(respuestas = resp, id = id))
        }
    }

    private fun enviarNotificacion() {
        val mBuilder: NotificationCompat.Builder
        val mNotifyMgr =
            activity!!.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val icono = R.drawable.minilogohospital
        val i = Intent(activity, CitasFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(activity, 0, i, 0)
        mBuilder = NotificationCompat.Builder(activity!!.applicationContext)
            .setContentIntent(pendingIntent)
            .setSmallIcon(icono)
            .setContentTitle(getString(R.string.citas))
            .setContentText(getString(R.string.existenCitas))
            .setVibrate(longArrayOf(100, 250, 100, 500))
            .setAutoCancel(true)
        mNotifyMgr.notify(1, mBuilder.build())

    }
}
