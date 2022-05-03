package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstbank.databinding.ActivityMainBinding
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.time.LocalDate



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
        var etcompletename: EditText = findViewById(R.id.ti_CompleteName)
        var etusername: EditText = findViewById(R.id.ti_Username)
        var etpassword: EditText = findViewById(R.id.ti_Password)
        var etconfirmpassword: EditText = findViewById(R.id.ti_ConfirmPassword)
        var etemailaddress: EditText = findViewById(R.id.ti_EmailAddress)
        var fecha = LocalDate.now()

        val pas1 = etpassword.text.toString()
        val cpas1 = etconfirmpassword.text.toString()


        if (pas1 == cpas1){
            try{
                val nuevoUsuario: PreparedStatement = connectSQL.dbConn()?.prepareStatement("insert into usuarios values (?,?,?,?,?,?,?,?)")!!
                nuevoUsuario.setString(1, etcompletename.text.toString())
                nuevoUsuario.setString(2, etusername.text.toString())
                nuevoUsuario.setString(3, etpassword.text.toString())
                nuevoUsuario.setString(4, etemailaddress.text.toString())
                nuevoUsuario.setString(5,"0")
                nuevoUsuario.setDouble(6, 0.0)
                nuevoUsuario.setInt(7, 10)
                nuevoUsuario.setDate(8, Date(fecha.toEpochDay()))
                nuevoUsuario.executeUpdate()
                Toast.makeText(this, "Cuenta Creada Existosamente", Toast.LENGTH_SHORT).show()
                openParental()
            }catch (ex:SQLException){
                Toast.makeText(this, "Error al crear cuenta", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
        }
    }


    fun openParental() {
        var intent = Intent(this, Parental_Control_Activity::class.java)
        startActivity(intent)
    }
}