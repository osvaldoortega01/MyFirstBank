package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfirstbank.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

//Esta variable se usará para saber en cualquier momento el email del usuario
lateinit var actualUserEmail : String

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //Se necesita cada vez que una actividad requiera hacer manejo de Autorizaciones con Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.loginButton.setOnClickListener {
            val email = binding.usernameEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainMenuActivity::class.java)
                        actualUserEmail=email
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this, it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else{
                Toast.makeText(this,"No se permiten campos vacíos",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}