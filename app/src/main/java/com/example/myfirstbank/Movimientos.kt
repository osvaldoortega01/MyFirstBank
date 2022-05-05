package com.example.myfirstbank

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.transition.Slide
import android.transition.TransitionManager
import android.widget.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class Movimientos : AppCompatActivity() {

    private var connectSQL = ConnectSQL()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimientos)

        //Botones activos en la pantalla MOVIMIENTOS
        val depositarButton: Button = findViewById(R.id.btn_movIngresar)
        val retirarButton: Button = findViewById(R.id.btn_movRetirar)

        //Obtiene el saldo de la cuenta a traves del UserID
        val iduser = MyFirstBank.prefs.getId()
        val tvsaldoDig: TextView= findViewById(R.id.textView4)
        try{
            val txtsaldo: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Cash FROM usuarios WHERE UserID = ?")!!
            txtsaldo.setString(1, iduser)
            val tvsaldo: ResultSet = txtsaldo.executeQuery()
            tvsaldo.next()
            tvsaldoDig.setText(tvsaldo.getString(1))
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

        depositarButton.setOnClickListener { openIngresar() }
        retirarButton.setOnClickListener { openRetirar() }
    }

    fun openIngresar(){
        var intent = Intent(this, IngresarDineroActivity::class.java)
        startActivity(intent)
    }

    fun openRetirar(){
        var intent = Intent(this, RetirarDineroActivity::class.java)
        startActivity(intent)
    }
}