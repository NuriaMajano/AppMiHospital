package net.azarquiel.mihospital.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_cita.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.model.Citas
import android.content.Intent
import android.net.Uri
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.mihospital.model.Agenda
import org.jetbrains.anko.*
import java.text.SimpleDateFormat




class DetailCitaActivity : AppCompatActivity() {

    private lateinit var id: String
    private lateinit var mesS: String
    private lateinit var hora: String
    private lateinit var fecha: String
    private var mes: String = " "
    private lateinit var db: FirebaseFirestore
    private lateinit var citapulsada: Citas
    lateinit var dp: DatePicker
    lateinit var tp: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_cita)
        db = FirebaseFirestore.getInstance()
        getSanitario()
        citapulsada = intent.getSerializableExtra("cita") as Citas
        btnhoradetailcita.setOnClickListener { btnhoradetailcitaOnClick() }
        btnfechadetailcita.setOnClickListener {  btnfechadetailcitaOnClick()}
        btnaceptardetailcita.setOnClickListener { btnaceptardetailcitaOnClick() }
        llamar.setOnClickListener {llamarCita(citapulsada.respuestas[7])}
        tvnombredetallecita.text = citapulsada.respuestas[3]
        tvnumsegsocialdetallecita.text= citapulsada.respuestas[0]
        tvcontactodetallecita.text= citapulsada.respuestas[7]
        tvrespuesta1detallecita.text= citapulsada.respuestas[6]
        tvrespuesta2detallecita.text= citapulsada.respuestas[4]
        tvrespuesta3detallecita.text= citapulsada.respuestas[5]
        tvrespuesta4detallecita.text= citapulsada.respuestas[1]
        tvrespuesta5detallecita.text= citapulsada.respuestas[2]
    }

    private fun getSanitario() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            id = it.uid
        }
    }

    private fun btnaceptardetailcitaOnClick() {
        if (tvfechadetailcita.text.isEmpty() && tvhoradetailcita.text.isEmpty()) Toast.makeText(this, getString(R.string.obligatoriofechahora), Toast.LENGTH_LONG).show()
        else{
            mesS=("0"+(dp.month+1).toString())
            when(mesS){
                "01" -> mes = "enero"

                "02" -> mes ="febrero"

                "03" -> mes = "marzo"

                "04" -> mes = "abril"

                "05" -> mes = "mayo"

                "06" -> mes = "junio"

                "07" -> mes = "julio"

                "08" -> mes = "agosto"

                "09" -> mes = "septiembre"

                "10" -> mes = "octubre"

                "11" -> mes = "noviembre"

                "12" -> mes = "diciembre"
            }
            db.collection("agenda").document(id).collection("${dp.year}").document("mes").collection(mes).document("horario").collection(fecha).add(Agenda(citapulsada.respuestas[3],hora,getString(R.string.citasCoronavirus)))
            eliminarCita()
        }
    }

    private fun eliminarCita() {
        db.collection("citas").document(citapulsada.id).delete()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun btnfechadetailcitaOnClick() {
        alert{
            title = getString(R.string.seleccionarfecha)
            customView {
                verticalLayout {
                    dp = datePicker {
                        minDate = System.currentTimeMillis()
                    }
                    positiveButton(getString(R.string.aceptar)) {
                        tvfechadetailcita.text = SimpleDateFormat("dd-MM-YYYY").format(SimpleDateFormat("dd-MM-yyyy").parse("${dp.dayOfMonth}-${dp.month+1}-${dp.year}"))
                        fecha = tvfechadetailcita.text.toString()
                    }
                    negativeButton(getString(R.string.cancelar)) {}
                }
            }
        }.show()
    }

    private fun btnhoradetailcitaOnClick() {
        alert{
            title = getString(R.string.seleccionarfecha)
            customView {
                verticalLayout {
                    tp = timePicker {
                            setIs24HourView(true)
                    }
                    positiveButton(getString(R.string.aceptar)) {
                        tvhoradetailcita.text =   SimpleDateFormat("HH:mm").format(SimpleDateFormat("HH:mm").parse("${tp.hour}:${tp.minute}"))
                        hora = tvhoradetailcita.text.toString()

                    }
                    negativeButton(getString(R.string.cancelar)) {}
                }
            }
        }.show()
    }

    private fun llamarCita(telef: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +  Uri.encode(telef)))
        startActivity(intent)
    }

}

