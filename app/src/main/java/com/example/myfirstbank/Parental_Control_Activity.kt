package com.example.myfirstbank

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstbank.MyFirstBank.Companion.prefs
import com.example.myfirstbank.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class Parental_Control_Activity : AppCompatActivity() {
    private var connectSQL = ConnectSQL()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parental_control)

        val txtcantidadmax: TextInputEditText = findViewById(R.id.et_cantidadmaxima)

        val txtcodigparent: TextInputEditText = findViewById(R.id.et_codigoparental)

        val iduser = prefs.getId()

        try{
            val txtcmax: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT MaxCashOut FROM usuarios WHERE UserID = ?")!!
            txtcmax.setString(1, iduser)
            val ticmax: ResultSet = txtcmax.executeQuery()
            ticmax.next()
            txtcantidadmax.setText(ticmax.getString(1))
            val txtcodpar: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT ParentalKey FROM usuarios WHERE UserID = ?")!!
            txtcodpar.setString(1, iduser)
            val ticod: ResultSet = txtcodpar.executeQuery()
            ticod.next()
            txtcodigparent.setText(ticod.getString(1))
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

        var btn_Aplicar_Cambios_CP: Button = findViewById(R.id.btn_Aplicar_Cambios_CP)
        btn_Aplicar_Cambios_CP.setOnClickListener{ controlparental() }
    }


    fun controlparental() {
        val iduser = prefs.getId()
        var etcantidadmaxima: TextInputEditText = findViewById(R.id.et_cantidadmaxima)
        var etcodigocontrolparental: TextInputEditText = findViewById(R.id.et_codigoparental)
        print(iduser)
        try {
            val controlparental: PreparedStatement = connectSQL.dbConn()
                ?.prepareStatement("UPDATE usuarios SET ParentalKey = ?, MaxCashOut = ? WHERE UserID = ?")!!
            controlparental.setString(1, etcodigocontrolparental.text.toString())
            controlparental.setString(2, etcantidadmaxima.text.toString())
            controlparental.setString(3, iduser)
            controlparental.executeUpdate()
            Toast.makeText(this, "Datos Actualizados Correctamente", Toast.LENGTH_SHORT).show()
            openMainMenu()
        } catch (ex: SQLException) {
            Toast.makeText(this, "Fallo al ingresar los datos", Toast.LENGTH_SHORT).show()
        }
    }
    fun openMainMenu() {
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

}