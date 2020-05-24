package net.azarquiel.mihospital.views


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_agenda.*

import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.adapter.AgendaAdapter
import net.azarquiel.mihospital.model.Agenda
import org.w3c.dom.Text
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class AgendaFragment : Fragment() {

    private lateinit var calendar: Calendar
    private lateinit var tvfechaAgenda: TextView
    private lateinit var db: FirebaseFirestore
    private lateinit var adapterAgenda: AgendaAdapter
    private var agendas: ArrayList<Agenda> = ArrayList()
    private lateinit var rvagenda: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_agenda, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()
        tvfechaAgenda = view.findViewById(R.id.tvfechaAgenda) as TextView
        fecha()
        db = FirebaseFirestore.getInstance()
        rvagenda = view.findViewById(R.id.rvagenda) as RecyclerView
        initRV()
        getSanitario()
    }

    private fun getSanitario() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            setListener(it.uid)
        }
    }

    private fun fecha() {
        var currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        tvfechaAgenda.text = currentDate
    }

    private fun setListener(uid: String) {
        val sdf = SimpleDateFormat("MMMM")
        val sdf2 = SimpleDateFormat("dd-MM-YYYY")
        val sdf3 = SimpleDateFormat("YYYY")
        val mes = sdf.format(Date())
        val fecha = sdf2.format(Date())
        val year = sdf3.format(Date())

        val docRef = db.collection("agenda").document("$uid").collection(year).document("mes").collection("$mes").document("horario").collection("$fecha")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen agenda failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                val agenda2 = ArrayList(agendas.sortedWith(compareBy { it.hora }))
                agendas = agenda2
                adapterAgenda.setAgenda(agendas)
            } else {
                Log.d(TAG, "Current data agenda: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        agendas.clear()
        documents.forEach { d ->
            val nombre = d["nombre"] as String
            val hora = d["hora"] as String
            val descripcion = d["descripcion"] as String

            agendas.add(Agenda(nombre = nombre,hora = hora, descripcion = descripcion))
        }
    }
    private fun initRV() {
        adapterAgenda = AgendaAdapter(activity!!.baseContext, R.layout.rowagenda)
        rvagenda.adapter = adapterAgenda
        rvagenda.layoutManager = LinearLayoutManager(activity)
    }

}
