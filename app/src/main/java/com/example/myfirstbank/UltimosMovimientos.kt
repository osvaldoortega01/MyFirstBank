package com.example.myfirstbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import java.sql.*

class UltimosMovimientos : AppCompatActivity() {
    private var connectSQL = ConnectSQL()
    var tUltimosMovimientos: TableLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimos_movimientos)

        val iduser = MyFirstBank.prefs.getId()

        tUltimosMovimientos = findViewById(R.id.tUltimosMovimientos)
        try{
            val movimentosDescription: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT TOP (5) * FROM movimientos WHERE UserID = ? ORDER BY MovementID DESC")!!
            movimentosDescription.setString(1,iduser)
            val tvDescription: ResultSet = movimentosDescription.executeQuery()

            while (tvDescription.next())
            {
                val registro = LayoutInflater.from(this).inflate(R.layout.item_table_layout_os, null, false)
                val tvDescripcion = registro.findViewById<View>(R.id.tvDescripcion) as TextView
                val tvTipo = registro.findViewById<View>(R.id.tvTipo) as TextView
                val tvCantidad = registro.findViewById<View>(R.id.tvCantidad) as TextView
                val tvFecha = registro.findViewById<View>(R.id.tvFecha) as TextView

                tvDescripcion.setText(tvDescription.getString(3))
                tvTipo.setText(tvDescription.getString(4))
                tvCantidad.setText("$"+tvDescription.getString(5))
                tvFecha.setText(tvDescription.getString(6))
                tUltimosMovimientos?.addView(registro)
            }

        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
    }
}