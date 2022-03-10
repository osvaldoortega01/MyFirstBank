package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        var btn_MenuMovimientos: Button = findViewById(R.id.btn_MenuMovimientos)
        btn_MenuMovimientos.setOnClickListener{ openMenuMovimientos() }

        var btn_MenuControlParental: Button = findViewById(R.id.btn_MenuControlParental)
        btn_MenuControlParental.setOnClickListener{ openControlParental() }

        var ibtn_ahorros: ImageButton = findViewById(R.id.ibtn_ahorros)
        ibtn_ahorros.setOnClickListener{ openAhorros() }

        var ibtn_perfil: ImageButton = findViewById(R.id.ibtn_perfil)
        ibtn_perfil.setOnClickListener{ openPerfil() }


    }


    fun openMenuMovimientos(){
        var intent = Intent(this, Movimientos::class.java)
        startActivity(intent)
    }
    fun openControlParental(){
        var intent = Intent(this, Parental_Control_Activity::class.java)
        startActivity(intent)
    }
    fun openAhorros(){
        var intent = Intent(this, AhorroActivity::class.java)
        startActivity(intent)
    }
    fun openPerfil(){
        var intent = Intent(this, Profile_Activity::class.java)
        startActivity(intent)
    }

}