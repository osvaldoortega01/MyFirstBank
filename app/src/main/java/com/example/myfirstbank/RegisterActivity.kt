package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var btn_Register: Button = findViewById(R.id.btn_Register)
        btn_Register.setOnClickListener{ openParental() }
    }

    fun openParental() {
        var intent = Intent(this, Parental_Control_Activity::class.java)
        startActivity(intent)
    }
}