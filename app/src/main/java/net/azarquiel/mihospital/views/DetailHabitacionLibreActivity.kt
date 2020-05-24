package net.azarquiel.mihospital.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.Constraints
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_habitacion_libre.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.model.Habitacion

class DetailHabitacionLibreActivity : AppCompatActivity() {

    private lateinit var habitacionpulsada: Habitacion
    private lateinit var plantapulsada: String
    private lateinit var db: FirebaseFirestore
    private var limpiadora:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_habitacion_libre)
        habitacionpulsada = intent.getSerializableExtra("habitacionlibre") as Habitacion
        plantapulsada = intent.getSerializableExtra("plantapulsada") as String
        val image_view = findViewById<ImageView>(R.id.ivlimpiadetailhabitacionlibre)
        tvnombrehabitaciondetallehabitacionlibre.text = "Habitación "+habitacionpulsada.nombrehabitacion.toString()
        image_view.setOnClickListener{
            if (limpiadora){
                if (habitacionpulsada.limpia){

                    ivlimpiadetailhabitacionlibre.setImageResource(R.drawable.nolimpio)
                }else{
                    ivlimpiadetailhabitacionlibre.setImageResource(R.drawable.limpio)
                }
                habitacionpulsada.limpia = !habitacionpulsada.limpia
                updateLimpia()
            }
        }
        if (habitacionpulsada.limpia){
            image_view.setImageResource(R.drawable.limpio)
            tvexpdetallehabitacionlibre.text = getString(R.string.fraselimpio)
        }else{
            image_view.setImageResource(R.drawable.nolimpio)
            tvexpdetallehabitacionlibre.text = getString(R.string.frasenolimpio)
        }
        db = FirebaseFirestore.getInstance()
        comprobarLimpiadora()
    }
    private fun comprobarLimpiadora() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            setListener(it.uid)
        }
    }

    private fun setListener(uid: String) {
        val docRef = db.collection("limpieza")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(Constraints.TAG, "Listen limpieza failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty()) {
                documentToList(snapshot.documents, uid)
            } else {
                Log.d(Constraints.TAG, "Current data limpieza: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>, uidDocumento: String) {
        documents.forEach { d ->
            if (d.id.equals(uidDocumento)){
                limpiadora = true
            }
        }
    }
    fun updateLimpia(){
        db.collection("plantas").document(plantapulsada).collection("habitaciones").document(habitacionpulsada.idhabitacion).update("limpia" , "${habitacionpulsada.limpia}".toBoolean())
    }
}
