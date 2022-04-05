package com.example.myfirstbank

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstbank.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener { validateLogin() }
        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener{ openRegister() }

    }
    private fun validateLogin(){
        var usernameEditText: EditText = findViewById(R.id.usernameEditText)
        var passwordEditText: EditText = findViewById(R.id.passwordEditText)
        if (usernameEditText.text.toString() == "admin" && passwordEditText.text.toString() == "admin"){
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            openMainMenu()
        }
        else{
            Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show()
        }
    }
    private fun openRegister(){
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun openMainMenu(){
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}