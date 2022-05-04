package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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

class MainMenuActivity : AppCompatActivity(){

    private var connectSQL = ConnectSQL()

    lateinit var binding: ActivityMainBinding

       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

           val bundle = intent.extras
           val iduser = bundle?.getString("iduser")
           val tvsaldoDig: TextView= findViewById(R.id.TV_SaldoDig)
           try{
               val txtsaldo: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Cash FROM usuarios WHERE UserID = ?")!!
               txtsaldo.setString(1, iduser.toString())
               val tvsaldo: ResultSet = txtsaldo.executeQuery()
               tvsaldo.next()
               tvsaldoDig.setText(tvsaldo.getString(1))
           }catch(ex: SQLException){
               Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
           }

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
        val bundle = intent.extras
        val iduser = bundle?.getString("iduser")
        var intent = Intent(this, Movimientos::class.java)
        intent.putExtra("iduser", iduser.toString())
        startActivity(intent)
    }
    fun openControlParental(){
        val bundle = intent.extras
        val iduser = bundle?.getString("iduser")
        val idpcv = 1
        var intent = Intent(this, PCV_Activity::class.java)
        intent.putExtra("idpcv", idpcv.toString())
        intent.putExtra("iduser", iduser.toString())
        startActivity(intent)
    }
    fun openAhorros(){
        val bundle = intent.extras
        val iduser = bundle?.getString("iduser")
        var intent = Intent(this, AhorroActivity::class.java)
        intent.putExtra("iduser", iduser.toString())
        startActivity(intent)
    }
    fun openPerfil(){
        val bundle = intent.extras
        val iduser = bundle?.getString("iduser")
        val idpcv = 2
        var intent = Intent(this, PCV_Activity::class.java)
        intent.putExtra("idpcv", idpcv.toString())
        intent.putExtra("iduser", iduser.toString())
        startActivity(intent)
    }

}