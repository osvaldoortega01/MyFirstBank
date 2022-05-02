package com.example.myfirstbank

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstbank.databinding.ActivityMainBinding
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class MainActivity : AppCompatActivity() {

    private var connectSQL = ConnectSQL()

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

        try{

            val usuario: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT UserPassword FROM usuarios WHERE Username = ?")!!
            usuario.setString(1, usernameEditText.text.toString())
            val contrasena: ResultSet = usuario.executeQuery()
            contrasena.next()
            Log.e("Error: ", contrasena.getString(1))
            if (passwordEditText.text.toString() == contrasena.getString(1)){
                Toast.makeText(this, "Buen inicio de sesión", Toast.LENGTH_SHORT).show()
                openMainMenu()
            }
            else{
                Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }
        }
        catch (ex: SQLException){
            Toast.makeText(this, "${ex.message}", Toast.LENGTH_SHORT).show()
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