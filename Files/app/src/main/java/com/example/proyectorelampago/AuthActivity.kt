package com.example.proyectorelampago

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the FirebaseAnalytics instance.
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()

        // Analytics events
        bundle.putString("message", "Integracion de Firebase completa")
        analytics.logEvent("InitScreen", bundle)


    }
}
