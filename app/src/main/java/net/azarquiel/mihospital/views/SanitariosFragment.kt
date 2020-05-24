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
import net.azarquiel.mihospital.adapter.SanitariosAdapter
import net.azarquiel.mihospital.model.Sanitario

class SanitariosFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adaptersanitario: SanitariosAdapter
    private var sanitarios: ArrayList<Sanitario> = ArrayList()
    private lateinit var rvsanitarios: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sanitarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        rvsanitarios = view.findViewById(R.id.rvsanitarios) as RecyclerView
        initRV()
        setListener()
    }

    private fun setListener() {
        val docRef = db.collection("sanitarios")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(Constraints.TAG, "Listen sanitarios failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adaptersanitario.setSanitario(sanitarios)
            } else {
                Log.d(Constraints.TAG, "Current data sanitarios: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        sanitarios.clear()
        documents.forEach { d ->
            val email = d["email"] as String
            val especialidad = d["especialidad"] as String
            val nombre = d["nombre"] as String
            val numcolegiado = d["numcolegiado"] as String
            val rol = d["rol"] as String
            sanitarios.add(Sanitario(nombre = nombre, email = email, especialidad = especialidad, numcolegiado = numcolegiado, rol = rol))
        }
    }

    private fun initRV() {
        adaptersanitario = SanitariosAdapter(activity!!.baseContext, R.layout.rowsanitarios)
        rvsanitarios.adapter = adaptersanitario
        rvsanitarios.layoutManager = LinearLayoutManager(activity)
    }
}
