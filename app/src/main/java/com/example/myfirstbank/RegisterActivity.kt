package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfirstbank.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    //Se necesita cada vez que una actividad requiera hacer manejo de Autorizaciones con Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    //Se nceesita cada vez que una actividad requiera gestionar una Base de datos con Firebase
    private lateinit var firebaseDB: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Solicitar una instancia para manejar autoizaciones
        firebaseAuth = FirebaseAuth.getInstance()
        //Solicitar una instancia para manejar la base de datos
        firebaseDB = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener{
            //Crea un Usuario Nuevo
            //Constantes que contienen los contenidos de cada editText presentes en la actividad
            val nombre = binding.etCompleteName.text.toString()
            val email = binding.etEmailAddress.text.toString()
            val pass = binding.etPassword.text.toString()
            val confirmPass = binding.etConfirmPassword.text.toString()
            val pin = binding.etControlParental.text.toString()
            val confirmPin = binding.etConfirmarControlParental.text.toString()
            //Comprobar que ningun campos no esten solos
            if(nombre.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && pin.isNotEmpty() && confirmPin.isNotEmpty()) {
                //Comporbar que la contraseña y el PIN sean igual a las confirmaciones
                if ((pass==confirmPass) && (pin==confirmPin)){
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                            if(it.isSuccessful){
                                //Guardar la información en la base de datos y luego regresar al LogIn
                                val intent = Intent(this,MainActivity::class.java)

                                firebaseDB.collection("usuarios").document(email).set(
                                    hashMapOf(
                                        "nombre" to nombre,
                                        "pin" to pin,
                                        "ahorro" to 0f //Cada vez que se crea una cuenta el ahorro empieza en ceros, esta cantidad se modificará en el el menú de control parental
                                    )
                                )
                                startActivity(intent)
                            } else{
                                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                } else{
                    Toast.makeText(this,"La contraseña o el PIN no coinciden",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"No se permiten campos vacíos",Toast.LENGTH_SHORT).show()
            }
        }
    }
}