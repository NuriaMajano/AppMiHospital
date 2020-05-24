package net.azarquiel.mihospital.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_habitacion_libre.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.adapter.HabitacionLibreAdapter
import net.azarquiel.mihospital.model.Habitacion
import net.azarquiel.mihospital.model.Planta

class HabitacionLibreActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    lateinit var adapterhabitacionlibre: HabitacionLibreAdapter
    var habitacioneslibres: ArrayList<Habitacion> = ArrayList()
    private lateinit var plantapulsada: Planta


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habitacion_libre)
        plantapulsada = intent.getSerializableExtra("planta") as Planta
        db = FirebaseFirestore.getInstance()
        initRV()
        setListener()
    }

    fun onClickHabitacionLibre(v: View){
        val habitacionlibrepulsada = v.tag as Habitacion
        val intent = Intent(this, DetailHabitacionLibreActivity::class.java)
        intent.putExtra("habitacionlibre", habitacionlibrepulsada)
        intent.putExtra("plantapulsada", plantapulsada.codplanta)
        startActivity(intent)
    }

    private fun setListener() {
        val docRef = db.collection("plantas").document(plantapulsada.codplanta).collection("habitaciones")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(Constraints.TAG, "Listen habitaciones libres failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapterhabitacionlibre.setHabitacion(habitacioneslibres)
            } else {
                Log.d(Constraints.TAG, "Current data habitaciones libres : null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        habitacioneslibres.clear()
        documents.forEach { d ->
            val nombrehabitacion = d["nombrehabitacion"] as String
            val limpia = d["limpia"] as Boolean
            val ocupada = d["ocupada"] as Boolean
            val idhabitacion = d["idhabitacion"] as String
            if (!ocupada) habitacioneslibres.add(Habitacion(nombrehabitacion = nombrehabitacion, limpia = limpia, ocupada=ocupada, idhabitacion=idhabitacion))
        }
    }
    private fun initRV() {
        adapterhabitacionlibre = HabitacionLibreAdapter(this, R.layout.rowhabitacionlibre)
        rvhabitacionlibre.adapter = adapterhabitacionlibre
        rvhabitacionlibre.layoutManager = LinearLayoutManager(this)

    }


}
