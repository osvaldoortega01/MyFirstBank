package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Parental_Control_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parental_control)

        var btn_Aplicar_Cambios_CP: Button = findViewById(R.id.btn_Aplicar_Cambios_CP)
        btn_Aplicar_Cambios_CP.setOnClickListener{ openMainMenu() }
    }

    fun openMainMenu(){
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}