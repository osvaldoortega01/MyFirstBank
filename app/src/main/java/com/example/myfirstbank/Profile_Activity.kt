package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Profile_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var btn_CerrarSesion: Button = findViewById(R.id.btn_CerrarSesion)
        btn_CerrarSesion.setOnClickListener{ openMain() }

        var btn_EstadoDeCuenta: Button = findViewById(R.id.btn_EstadoDeCuenta)
        btn_EstadoDeCuenta.setOnClickListener{ openEstado() }

        var btn_AplicarCambios: Button = findViewById(R.id.btn_AplicarCambios)
        btn_AplicarCambios.setOnClickListener{ openMainMenu() }
    }

    fun openMain(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun openEstado() {
        var intent = Intent(this, EstadoDeCuentaActivity::class.java)
        startActivity(intent)
    }

    fun openMainMenu(){
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}