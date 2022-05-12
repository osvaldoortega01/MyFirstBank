package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalDateTime

class RetirarDineroActivity : AppCompatActivity() {

    private var connectSQL = ConnectSQL()
    lateinit var saldoActual: String
    lateinit var maxCashOut: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retirar_dinero)

        val iduser = MyFirstBank.prefs.getId()
        val retirarButton: Button = findViewById(R.id.btnRetirarDinero)

        //Obtener Saldo Actual en la cuenta
        try{
            val txtsaldo: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Cash FROM usuarios WHERE UserID = ?")!!
            txtsaldo.setString(1, iduser)
            val tvsaldo: ResultSet = txtsaldo.executeQuery()
            tvsaldo.next()
            saldoActual = tvsaldo.getString(1)
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
        //Obtener Maximo de Retiro Permitido de la cuenta
        try{
            val txtsaldo: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT MaxCashOut FROM usuarios WHERE UserID = ?")!!
            txtsaldo.setString(1, iduser)
            val tvsaldo: ResultSet = txtsaldo.executeQuery()
            tvsaldo.next()
            maxCashOut = tvsaldo.getString(1)
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

        val etCantidadDinero: EditText = findViewById(R.id.editTextRetirarDinero)

        retirarButton.setOnClickListener {
            if(etCantidadDinero.text.isNotEmpty()){
                retirarDinero()
            }else {
                Toast.makeText(this, "La cantidad no esta permitida o no tienes fondos suficientes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun retirarDinero(){
        val etCantidadDinero: EditText = findViewById(R.id.editTextRetirarDinero)
        val iduser = MyFirstBank.prefs.getId()
        val fechaCreacion: LocalDate = LocalDate.now()
        val userID: String = MyFirstBank.prefs.getId()
        //Comporbar que la cantidad a retirar sea menor o igual que el MAX CASH OUT y que el Saldo Actual sea mayor que 0
        if((etCantidadDinero.text.toString().toFloat()<=maxCashOut.toFloat()) && (saldoActual.toFloat()>0f)){
            var nuevoSaldo: Float = saldoActual.toFloat() - etCantidadDinero.text.toString().toFloat()
            try {
                val actualilzarCash: PreparedStatement = connectSQL.dbConn()
                    ?.prepareStatement("UPDATE usuarios SET Cash = ? WHERE UserID = ?")!!
                actualilzarCash.setString(1, nuevoSaldo.toString())
                actualilzarCash.setString(2, iduser)
                actualilzarCash.executeUpdate()
                Toast.makeText(this, "Transacción Exitosa", Toast.LENGTH_SHORT).show()
            } catch (ex: SQLException) {
                Toast.makeText(this, "Fallo al realizar la Transacción", Toast.LENGTH_SHORT).show()
            }
            //Registrar movimiento de retiro en DB
            try{
                val insertMovimiento: PreparedStatement = connectSQL.dbConn()?.prepareStatement("INSERT INTO movimientos VALUES(?,?,?,?,?)")!!
                insertMovimiento.setInt(1, userID.toInt())
                insertMovimiento.setString(2, "Retiro - "+fechaCreacion.toString())
                insertMovimiento.setString(3, "Retiro")
                insertMovimiento.setDouble(4, etCantidadDinero.text.toString().toDouble())
                insertMovimiento.setString(5, LocalDateTime.now().toString())
                insertMovimiento.executeUpdate()
                Toast.makeText(this, "Movimiento Creado Existosamente", Toast.LENGTH_SHORT).show()
                openMainMenu()
            } catch (ex: SQLException){
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "La cantidad no esta permitida o no tienes fondos suficientes", Toast.LENGTH_SHORT).show()
        }
    }

    fun openMainMenu(){
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}