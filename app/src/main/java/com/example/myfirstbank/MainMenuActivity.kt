package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainMenuActivity : AppCompatActivity(){
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        var btn_MenuMovimientos: Button = findViewById(R.id.btn_MenuMovimientos)
        btn_MenuMovimientos.setOnClickListener{ openMenuMovimientos() }

        var btn_MenuControlParental: Button = findViewById(R.id.btn_MenuControlParental)
        btn_MenuControlParental.setOnClickListener{ openControlParental() }

        var btn_ahorros: ExtendedFloatingActionButton = findViewById(R.id.btn_ahorros)
           btn_ahorros.setOnClickListener{ openAhorros() }

        var btn_perfil: ExtendedFloatingActionButton = findViewById(R.id.btn_perfil)
           btn_perfil.setOnClickListener{ openPerfil() }


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