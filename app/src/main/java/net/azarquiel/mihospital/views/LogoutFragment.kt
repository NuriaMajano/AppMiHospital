package net.azarquiel.mihospital.views


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.nav_header_main.*

import net.azarquiel.mihospital.R

class LogoutFragment : Fragment() {

    private lateinit var btnSalir: Button
    private lateinit var loginShare: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShare()
        btnSalir = view.findViewById(R.id.btnSalir) as Button
        btnSalirOnClick()
    }

    private fun initShare() {
        loginShare= activity!!.getSharedPreferences("login", Context.MODE_PRIVATE)
    }
    private fun btnSalirOnClick() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            deleteLogin(it.email.toString())
        }
        Toast.makeText(activity, getString(R.string.sesioncerrada), Toast.LENGTH_SHORT).show()
    }

    private fun deleteLogin(nombreusuario: String) {
        val edit = loginShare.edit()
        edit.remove(nombreusuario)
        edit.apply()
        activity!!.startActivity(Intent(activity, LoginActivity::class.java))
    }
}
