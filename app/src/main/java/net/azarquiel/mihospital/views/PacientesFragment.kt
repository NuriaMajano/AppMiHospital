package net.azarquiel.mihospital.views


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.adapter.PacienteAdapter
import net.azarquiel.mihospital.model.Paciente

class PacientesFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapterPacientes: PacienteAdapter
    private var pacientes: ArrayList<Paciente> = ArrayList()
    private lateinit var rvpacientes: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pacientes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        rvpacientes = view.findViewById(R.id.rvpacientes) as RecyclerView
        initRV()
        getSanitario()
    }

    private fun getSanitario() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            setListener(it.uid)
        }
    }

    private fun setListener(uid: String) {
        val docRef = db.collection("pacientes")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen pacientes failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents, uid)
                adapterPacientes.setPaciente(pacientes)
            } else {
                Log.d(TAG, "Current data pacientes: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>, uid: String) {
        pacientes.clear()
        documents.forEach { d ->
            val nombre = d["nombre"] as String
            val numsegsocial = d["numsegsocial"] as String
            val codhab = d["codhab"] as String
            val estadopaciente = d["estadopaciente"] as String
            val descestadopaciente = d["descestadopaciente"] as String
            val iddoctor = d["iddoctor"] as String
            val idenfermera = d["idenfermera"] as String
            val idpaciente = d["idpaciente"] as String

            if (uid == idenfermera || uid == iddoctor) {
                pacientes.add(Paciente(nombre = nombre,numsegsocial = numsegsocial, codhab = codhab, estadopaciente=estadopaciente, descestadopaciente=descestadopaciente, iddoctor=iddoctor, idenfermera=idenfermera, idpaciente=idpaciente))

            }
        }
    }

    private fun initRV() {
        adapterPacientes = PacienteAdapter(activity!!.baseContext, R.layout.rowpaciente)
        rvpacientes.adapter = adapterPacientes
        rvpacientes.layoutManager = LinearLayoutManager(activity)
    }

}
