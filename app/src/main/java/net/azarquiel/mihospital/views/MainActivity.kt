package net.azarquiel.mihospital.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
//import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import net.azarquiel.mihospital.R
import net.azarquiel.mihospital.model.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private var logout: Boolean = false
    private lateinit var db: FirebaseFirestore
    private lateinit var loginShare: SharedPreferences
    private lateinit var userShare: SharedPreferences
    private lateinit var user: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        nav_view.setNavigationItemSelectedListener(this)
        nav_viewBottom.setOnNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        db = FirebaseFirestore.getInstance()
        initShare()
        user = userShare.getString("usuario", "nousuario") as String
        updateHeader()
        comprobarUser()
        nav_viewBottom.setBackgroundColor(resources.getColor(R.color.colorAzuk))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_inicio -> {
                fragment = InicioFragment()
                nav_viewBottom.visibility = View.INVISIBLE
            }
            R.id.nav_pacientes -> {
                fragment = PacientesFragment()
                nav_viewBottom.visibility = View.INVISIBLE
            }
            R.id.nav_materialsanitario -> {
                fragment = MaterialSanitarioFragment()
                nav_viewBottom.visibility = View.INVISIBLE
            }
            R.id.nav_habitacioneslibres -> {
                fragment = PlantaFragment()
                nav_viewBottom.visibility = View.INVISIBLE
            }
            R.id.nav_citas -> {
                fragment = CitasFragment()
                nav_viewBottom.visibility = View.INVISIBLE
            }
            R.id.nav_salir -> {
                logoutUsuario()
                nav_viewBottom.visibility = View.INVISIBLE
            }
            R.id.nav_mapa -> {
                fragment = PlanosFragment()
                nav_viewBottom.visibility = View.VISIBLE
            }
            R.id.navigation_mapa -> {
                fragment = MapaFragment()
                nav_viewBottom.visibility = View.VISIBLE
            }
            R.id.navigation_planos -> {
                fragment = PlanosFragment()
                nav_viewBottom.visibility = View.VISIBLE
            }
        }
        if (!logout) replaceFragment(fragment!!)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun comprobarUser() {
        when (user){
            "sanitarios" -> {
                nav_view.menu.findItem(R.id.nav_inicio).isVisible = true
                nav_view.menu.findItem(R.id.nav_pacientes).isVisible = true
                nav_view.menu.findItem(R.id.nav_citas).isVisible = true
                setInitialFragment("sanitarios")
            }
            "limpieza" -> {
                setInitialFragment("limpieza")
            }
        }
    }

    private fun logoutUsuario() {
        logout = true
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            deleteLogin(it.email.toString())
        }
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, getString(R.string.sesioncerrada), Toast.LENGTH_SHORT).show()
    }
    private fun initShare() {
        loginShare= this.getSharedPreferences("login", Context.MODE_PRIVATE)
        userShare= this.getSharedPreferences("user", Context.MODE_PRIVATE)
    }
    private fun deleteLogin(nombreusuario: String) {
        val edit = loginShare.edit()
        edit.remove(nombreusuario)
        edit.apply()
        this.startActivity(Intent(this, LoginActivity::class.java))
    }


    private fun setInitialFragment(user: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (user){
            "sanitarios" -> {
                fragmentTransaction.add(R.id.frame,PacientesFragment())
            }
            "limpieza" -> {
                fragmentTransaction.add(R.id.frame,MaterialSanitarioFragment())
            }
        }
        nav_viewBottom.visibility = View.INVISIBLE
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun onClickPaciente(v: View) {
        val pacientepulsado = v.tag as Paciente
        val intent = Intent(this, DetailPacienteActivity::class.java)
        intent.putExtra("paciente", pacientepulsado)
        startActivity(intent)
    }

    fun onClickPlanta(v: View) {
        val plantapulsada = v.tag as Planta
        val intent = Intent(this, HabitacionLibreActivity::class.java)
        intent.putExtra("planta", plantapulsada)
        startActivity(intent)
    }

    fun onClickSanitario(v: View) {
        val sanitariopulsado = v.tag as Sanitario
        val intent = Intent(this, DetailSanitarioActivity::class.java)
        intent.putExtra("sanitario", sanitariopulsado)
        startActivity(intent)
    }

    fun onClickCita(v: View){
        val citapulsada = v.tag as Citas
        val intent = Intent(this, DetailCitaActivity::class.java)
        intent.putExtra("cita", citapulsada)
        startActivity(intent)
    }

    private fun updateHeader() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            setListener(it.uid)
        }
    }

    private fun setListener(uid: String) {
        val docRef = db.collection(user)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(Constraints.TAG, "Listen habitaciones libres failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents, uid)
            } else {
                Log.d(Constraints.TAG, "Current data habitaciones libres : null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>, uidDocumento: String) {
        when(user) {
            "sanitarios" -> {
                documents.forEach { d ->
                    if (d.id.equals(uidDocumento)){
                        val nombre = d["nombre"] as String
                        val numcolegiado = d["numcolegiado"] as String
                        val nombrepersonal = nav_view.getHeaderView(0).tvnombrepersonal
                        val numerocolegiado = nav_view.getHeaderView(0).tvnumcolegiado
                        nombrepersonal.text = nombre
                        numerocolegiado.text = numcolegiado
                    }
                }
            }
            else -> {
                documents.forEach { d ->
                    if (d.id.equals(uidDocumento)){
                        val nombre = d["nombre"] as String
                        val nombrepersonal = nav_view.getHeaderView(0).tvnombrepersonal
                        nombrepersonal.text = nombre
                    }
                }
            }
        }
    }
}