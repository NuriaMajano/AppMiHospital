package net.azarquiel.mihospital.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_sanitario.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.model.Sanitario

class DetailSanitarioActivity : AppCompatActivity() {

    private lateinit var sanitariopulsado: Sanitario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_sanitario)
        sanitariopulsado = intent.getSerializableExtra("sanitario") as Sanitario
        fab2.setOnClickListener {enviarEmail()}

        tvnombredetallesanitario.text = sanitariopulsado.nombre
        tvnumcolegiadodetallesanitario.text = sanitariopulsado.numcolegiado
        tvroldetallesanitario.text = sanitariopulsado.rol
        tvespecialidaddetallesanitario.text = sanitariopulsado.especialidad
        tvemaildetallepaciente.text = sanitariopulsado.email
    }

    private fun enviarEmail() {
        var emailIntent = Intent (Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(sanitariopulsado.email))
        startActivity(Intent.createChooser(emailIntent, "Enviar Correo..."))
    }
}
