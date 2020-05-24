package net.azarquiel.mihospital.views


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.adapter.PlantaAdapter
import net.azarquiel.mihospital.model.Planta

class PlantaFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapterplanta: PlantaAdapter
    private var plantas: ArrayList<Planta> = ArrayList()
    private lateinit var rvplantas: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planta, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        rvplantas = view.findViewById(R.id.rvplantas) as RecyclerView
        initRV()
        setListener()
    }

    private fun setListener() {
        val docRef = db.collection("plantas")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(Constraints.TAG, "Listen plantas failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapterplanta.setPlantas(plantas)
            } else {
                Log.d(Constraints.TAG, "Current data plantas: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        plantas.clear()
        documents.forEach { d ->
            val nombre = d["nombre"] as String
            val codplanta = d["codplanta"] as String
            plantas.add(Planta(nombre = nombre, codplanta = codplanta))
        }
    }

    private fun initRV() {
        adapterplanta = PlantaAdapter(activity!!.baseContext, R.layout.rowplantas)
        rvplantas.adapter = adapterplanta
        rvplantas.layoutManager = LinearLayoutManager(activity)
    }

}
