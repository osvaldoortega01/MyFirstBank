package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstbank.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import java.sql.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime


class RegisterActivity : AppCompatActivity() {

    private var connectSQL = ConnectSQL()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var btn_Register: Button = findViewById(R.id.btn_Register)
        btn_Register.setOnClickListener{ registerUser() }
    }

    private fun registerUser(){
        var etcompletename: TextInputEditText = findViewById(R.id.tiet_CompleteName)
        var etusername: TextInputEditText = findViewById(R.id.tiet_Username)
        var etpassword: TextInputEditText = findViewById(R.id.tiet_Password)
        var etconfirmpassword: TextInputEditText = findViewById(R.id.tiet_ConfirmPassword)
        var etemailaddress: TextInputEditText = findViewById(R.id.tiet_EmailAddress)
        var fecha = LocalDateTime.now()

        val pas1 = etpassword.text.toString()
        val cpas1 = etconfirmpassword.text.toString()
        try {
            // Se encapsula todos los peticiones SQL con un try catch, para que no crasheé la app
            val norepeatuser: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT UserID FROM usuarios WHERE Username = ?")!!
            norepeatuser.setString(1, etusername.text.toString())
            val verifuser: ResultSet = norepeatuser.executeQuery()

            if(!verifuser.next()){
                if (pas1==cpas1){
                    try{
                        val nuevoUsuario: PreparedStatement = connectSQL.dbConn()?.prepareStatement("insert into usuarios values (?,?,?,?,?,?,?,?)")!!
                        nuevoUsuario.setString(1, etcompletename.text.toString())
                        nuevoUsuario.setString(2, etusername.text.toString())
                        nuevoUsuario.setString(3, etpassword.text.toString())
                        nuevoUsuario.setString(4, etemailaddress.text.toString())
                        nuevoUsuario.setString(5,"0")
                        nuevoUsuario.setDouble(6, 0.0)
                        nuevoUsuario.setInt(7, 10)
                        nuevoUsuario.setString(8, fecha.toString())
                        nuevoUsuario.executeUpdate()
                        Toast.makeText(this, "Cuenta Creada Existosamente", Toast.LENGTH_SHORT).show()
                        openParental()
                    }catch (ex:SQLException){
                        Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_LONG).show()
            }

        }
        catch (ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

    }


    fun openParental() {
        var intent = Intent(this, Parental_Control_Activity::class.java)
        startActivity(intent)
    }
}