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
import net.azarquiel.mihospital.adapter.MaterialSanitarioAdapter
import net.azarquiel.mihospital.model.InterfazAdapter
import net.azarquiel.mihospital.model.MaterialSanitario

class MaterialSanitarioFragment : Fragment(), InterfazAdapter {
    private lateinit var db: FirebaseFirestore
    private lateinit var adapterMaterialSanitario: MaterialSanitarioAdapter
    private var materialesSanitarios: ArrayList<MaterialSanitario> = ArrayList()
    private lateinit var rvmaterialsanitario: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_sanitario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        rvmaterialsanitario = view.findViewById(R.id.rvmaterialsanitario) as RecyclerView
        initRV()
        setListener()
    }

    private fun setListener() {
        val docRef = db.collection("materialsanitario")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(Constraints.TAG, "Listen materialsanitario failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapterMaterialSanitario.setMaterialSanitario(materialesSanitarios)
            } else {
                Log.d(Constraints.TAG, "Current data materialsanitario: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        materialesSanitarios.clear()
        documents.forEach { d ->
            val nombre = d["nombre"] as String
            val cantidad = d["cantidad"] as Long
            materialesSanitarios.add(MaterialSanitario(nombre = nombre, cantidad = cantidad))
        }
    }

    private fun initRV() {
        adapterMaterialSanitario = MaterialSanitarioAdapter(activity!!.baseContext, R.layout.rowmaterialsanitario, this)
        rvmaterialsanitario.adapter = adapterMaterialSanitario
        rvmaterialsanitario.layoutManager = LinearLayoutManager(activity)
    }


    override fun onClickMas(item: MaterialSanitario) {
        db.collection("materialsanitario").document("${item.nombre}").update("cantidad" , "${item.cantidad+1}".toLong())
    }

    override fun onClickMenos(item: MaterialSanitario) {
        if (item.cantidad > 0) {
            db.collection("materialsanitario").document("${item.nombre}")
                .update("cantidad", "${item.cantidad - 1}".toLong())
        }
    }
}
