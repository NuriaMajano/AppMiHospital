package net.azarquiel.mihospital.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.constraintlayout.widget.Constraints
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_paciente.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.model.Paciente
import org.jetbrains.anko.*

class DetailPacienteActivity : AppCompatActivity() {

    private lateinit var pacientepulsado: Paciente
    private lateinit var db: FirebaseFirestore
    private lateinit var etDesc: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_paciente)
        db = FirebaseFirestore.getInstance()
        setListener()
        pacientepulsado = intent.getSerializableExtra("paciente") as Paciente
        edit.setOnClickListener {alertDesc()}
        tvdesestadopacientedetallepaciente.text = pacientepulsado.descestadopaciente
        tvnombrepacientedetalle.text = pacientepulsado.nombre
    }

    private fun alertDesc() {
        alert{
            title = getString(R.string.cambiardescpaciente)
            customView {
                verticalLayout {
                    lparams(width = wrapContent, height = wrapContent)
                    etDesc = editText {
                        inputType = InputType.TYPE_CLASS_TEXT
                        hint = getString(R.string.descripcion)
                        padding = dip(16)
                    }
                    val spinner = spinner {
                        adapter = ArrayAdapter(context, android.R.layout.select_dialog_singlechoice, arrayListOf("Leve", "Bien", "Grave"))
                    }
                    positiveButton(getString(R.string.aceptar)) {
                        if (etDesc.text.toString().isEmpty())
                            toast(getString(R.string.camposobligatorios))
                        else {
                            updateDesc(etDesc.text.toString(), spinner.selectedItem.toString())
                        }
                    }
                    negativeButton(getString(R.string.cancelar)) {}
                }
            }
        }.show()
    }

    private fun updateDesc(nuevaDesc: String, nuevoEstado: String) {
        db.collection("pacientes").document(pacientepulsado.idpaciente).update("descestadopaciente", nuevaDesc, "estadopaciente", nuevoEstado)
    }

    private fun setListener() {
        val docRef = db.collection("pacientes")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(Constraints.TAG, "Listen pacientes failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
            } else {
                Log.d(Constraints.TAG, "Current data pacientes: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        documents.forEach { d ->
            val nombre = d["nombre"] as String
            val numsegsocial = d["numsegsocial"] as String
            val codhab = d["codhab"] as String
            val estadopaciente = d["estadopaciente"] as String
            val descestadopaciente = d["descestadopaciente"] as String
            val iddoctor = d["iddoctor"] as String
            val idenfermera = d["idenfermera"] as String
            val idpaciente = d["idpaciente"] as String
            pacientepulsado = Paciente(nombre=nombre,numsegsocial=numsegsocial,codhab=codhab,estadopaciente=estadopaciente,descestadopaciente=descestadopaciente,iddoctor=iddoctor,idenfermera=idenfermera,idpaciente=idpaciente)
            tvdesestadopacientedetallepaciente.text = pacientepulsado.descestadopaciente
        }
    }
}
