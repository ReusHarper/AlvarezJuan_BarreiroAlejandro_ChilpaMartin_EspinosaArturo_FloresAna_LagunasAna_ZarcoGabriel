package com.example.proyectorelampago

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectorelampago.R.layout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.emailTextView
import kotlinx.android.synthetic.main.activity_home.logOutbutton
import kotlinx.android.synthetic.main.activity_menu.*

enum class ProviderType {
    BASIC,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?: "--")
    }

    private fun setup(email: String) {

        title = "Inicio"
        emailTextView.text = email

        camaraButton.setOnClickListener {

        }

        calendarioButton.setOnClickListener {

        }

        juegosButton.setOnClickListener {

        }

        buzonButton.setOnClickListener {

        }

        logOutbutton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
    
}
