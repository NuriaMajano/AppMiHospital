package net.azarquiel.mihospital.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header_main.*
import net.azarquiel.mihospital.R
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loginShare: SharedPreferences
    private lateinit var userShare: SharedPreferences
    private var encontrado = false
    private lateinit var myintent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        myintent = Intent(this, MainActivity::class.java)
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        btnAceptarLogin.setOnClickListener{
            login()
        }
        btnolvidarpass.setOnClickListener {
            olvidarpass()
        }
        initShare()
        if (comprobarLogin()){
            action()
        }
    }

    private fun olvidarpass() {
        alert{
            title = getString(R.string.cambiarcontraseña)
            customView {
                verticalLayout {
                    lparams(width = wrapContent, height = wrapContent)
                        etEmail = editText {
                        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        hint = getString(R.string.email)
                        padding = dip(16)
                    }
                    positiveButton(getString(R.string.aceptar)) {
                        if (etEmail.text.toString().isEmpty())
                            toast(getString(R.string.camposobligatorios))
                        else {
                            enviarCorreo()
                        }
                    }
                    negativeButton(getString(R.string.cancelar)) {}
                }
            }
        }.show()

    }

    private fun enviarCorreo() {
        mAuth.sendPasswordResetEmail(etEmail.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, getString(R.string.correorestablecercontraseña), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initShare() {
        loginShare = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        userShare = this.getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    private fun action(){
        startActivity(myintent)

    }

    private fun login() {
            val email:String=edEmailLogin.text.toString()
            val pass:String=edPassLogin.text.toString()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
                progressBar.visibility= View.VISIBLE

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this){
                        task ->
                    if (task.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.let {
                            val edit= loginShare.edit()
                            edit.putString("${it.email}", Gson().toJson(it))
                            edit.apply()
                            setListenerLimpieza(it.uid)
                        }
                    }else{
                        Toast.makeText(this, getString(R.string.erroriniciosesion), Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun setListenerLimpieza(uid: String) {
        val docRef = db.collection("limpieza")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(Constraints.TAG, "Listen limpieza failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents, uid)
            } else {
                Log.d(Constraints.TAG, "Current data limpieza : null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>, uidDocumento: String) {
        documents.forEach { d ->
            if (d.id == uidDocumento){
                encontrado = true
            }
        }
        var user: String
        if (encontrado) {
            user = "limpieza"
        }else {
            user = "sanitarios"
        }
        val edit= userShare.edit()
        edit.putString("usuario", user)
        edit.apply()
        action()
    }

    private fun comprobarLogin():Boolean {
        return loginShare.all.isNotEmpty()
    }
}
