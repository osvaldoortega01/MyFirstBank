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

class IngresarDineroActivity : AppCompatActivity() {

    private var connectSQL = ConnectSQL()
    lateinit var saldoActual: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresar_dinero)

        val iduser = MyFirstBank.prefs.getId()
        val ingresarButton: Button = findViewById(R.id.btnIngresarDinero)

        try{
            val txtsaldo: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Cash FROM usuarios WHERE UserID = ?")!!
            txtsaldo.setString(1, iduser)
            val tvsaldo: ResultSet = txtsaldo.executeQuery()
            tvsaldo.next()
            saldoActual = tvsaldo.getString(1)
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

        var etCantidadDinero: EditText = findViewById(R.id.editTextIngresarDinero)

        ingresarButton.setOnClickListener {
            if(etCantidadDinero.text.isNotEmpty()){
                ingresarDinero()
            }else {
                Toast.makeText(this, "La cantidad no esta permitida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun ingresarDinero(){
        var etCantidadDinero: EditText = findViewById(R.id.editTextIngresarDinero)
        var nuevoSaldo: Float = etCantidadDinero.text.toString().toFloat() + saldoActual.toFloat()
        val fechaCreacion: LocalDate = LocalDate.now()
        val userID: String = MyFirstBank.prefs.getId()
        //Actualizar DB con el monto ingresado
        try {
            val actualilzarCash: PreparedStatement = connectSQL.dbConn()
                ?.prepareStatement("UPDATE usuarios SET Cash = ? WHERE UserID = ?")!!
            actualilzarCash.setString(1, nuevoSaldo.toString())
            actualilzarCash.setString(2, userID)
            actualilzarCash.executeUpdate()
            Toast.makeText(this, "Transacción Exitosa", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Toast.makeText(this, "Fallo al realizar la Transacción", Toast.LENGTH_SHORT).show()
        }
        //Registrar movimiento de ingreso en DB
        try{
            val insertMovimiento: PreparedStatement = connectSQL.dbConn()?.prepareStatement("INSERT INTO movimientos VALUES(?,?,?,?,?)")!!
            insertMovimiento.setInt(1, userID.toInt())
            insertMovimiento.setString(2, "Ingreso - "+fechaCreacion.toString())
            insertMovimiento.setString(3, "Ingreso")
            insertMovimiento.setDouble(4, etCantidadDinero.text.toString().toDouble())
            insertMovimiento.setString(5, LocalDateTime.now().toString())
            insertMovimiento.executeUpdate()
            Toast.makeText(this, "Movimiento Creado Existosamente", Toast.LENGTH_SHORT).show()
            openMainMenu()
        } catch (ex: SQLException){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    fun openMainMenu(){
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}