package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstbank.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

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
        val iduser = MyFirstBank.prefs.getId()
        var etCantidadDinero: EditText = findViewById(R.id.editTextIngresarDinero)
        var nuevoSaldo: Float = etCantidadDinero.text.toString().toFloat() + saldoActual.toFloat()

        try {
            val actualilzarCash: PreparedStatement = connectSQL.dbConn()
                ?.prepareStatement("UPDATE usuarios SET Cash = ? WHERE UserID = ?")!!
            actualilzarCash.setString(1, nuevoSaldo.toString())
            actualilzarCash.setString(2, iduser)
            actualilzarCash.executeUpdate()
            Toast.makeText(this, "Transacción Exitosa", Toast.LENGTH_SHORT).show()
            openMainMenu()
        } catch (ex: SQLException) {
            Toast.makeText(this, "Fallo al realizar la Transacción", Toast.LENGTH_SHORT).show()
        }
    }

    fun openMainMenu(){
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}