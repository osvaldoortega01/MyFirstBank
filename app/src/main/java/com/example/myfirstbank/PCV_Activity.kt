package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfirstbank.databinding.ActivityMainBinding
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstbank.MyFirstBank.Companion.prefs
import com.google.android.material.textfield.TextInputEditText
import java.sql.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime

class PCV_Activity : AppCompatActivity() {

    private var connectSQL = ConnectSQL()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pcv)
        var btn_IngresarCodigo: Button = findViewById(R.id.btn_IngresarCodigo)
        btn_IngresarCodigo.setOnClickListener{ verificarpc() }

    }
    fun verificarpc (){
        val bundle = intent.extras
        val iduser = prefs.getId()
        val idpcv = bundle?.getString("idpcv")
        val tiet_pcv: TextInputEditText = findViewById(R.id.tiet_pcv)
        if(idpcv=="1"){
            try{
                val codigoparentarl: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT ParentalKey FROM usuarios WHERE UserID = ?")!!
                codigoparentarl.setString(1, iduser)
                val codigoparentaru: ResultSet = codigoparentarl.executeQuery()
                codigoparentaru.next()
                if (tiet_pcv.text.toString() == codigoparentaru.getString(1)) {
                    Toast.makeText(this, "Codigo parental correcto", Toast.LENGTH_SHORT).show()
                    openControlParental()
                }else{
                    Toast.makeText(this, "Codigo parental incorrecto", Toast.LENGTH_LONG).show()
                }
            }catch(ex: SQLException){
                Toast.makeText(this, "${ex.message}", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            if(idpcv=="2"){
                try{
                    val codigoparentarl: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT ParentalKey FROM usuarios WHERE UserID = ?")!!
                    codigoparentarl.setString(1, iduser)
                    val codigoparentaru: ResultSet = codigoparentarl.executeQuery()
                    codigoparentaru.next()
                    if (tiet_pcv.text.toString() == codigoparentaru.getString(1)) {
                        Toast.makeText(this, "Codigo parental correcto", Toast.LENGTH_SHORT).show()
                        openPerfil()
                    }else{
                        Toast.makeText(this, "Codigo parental incorrecto", Toast.LENGTH_LONG).show()
                    }
                }catch(ex: SQLException){
                    Toast.makeText(this, "${ex.message}", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Opcion no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun openControlParental(){
        var intent = Intent(this, Parental_Control_Activity::class.java)
        startActivity(intent)
    }
    fun openPerfil(){
        var intent = Intent(this, Profile_Activity::class.java)
        startActivity(intent)
    }
}