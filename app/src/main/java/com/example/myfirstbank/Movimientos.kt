package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val ultimosMovButton: Button = findViewById(R.id.btn_movUltimos)

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
        ultimosMovButton.setOnClickListener { openUltimosMov() }
    }

    fun openIngresar(){
        var intent = Intent(this, IngresarDineroActivity::class.java)
        startActivity(intent)
    }

    fun openRetirar(){
        var intent = Intent(this, RetirarDineroActivity::class.java)
        startActivity(intent)
    }

    fun openUltimosMov(){
        var intent = Intent(this, UltimosMovimientos::class.java)
        startActivity(intent)
    }
}