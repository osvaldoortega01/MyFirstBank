package com.example.myfirstbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import java.sql.*

class UltimosMovimientos : AppCompatActivity() {
    private var connectSQL = ConnectSQL()
    var actualDescription : String = ""
    var actualType : String = ""
    var actualQuantity : String = ""
    var actualDate: String = ""
    var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimos_movimientos)

        val iduser = MyFirstBank.prefs.getId()
        val tvDescriptionView: TextView = findViewById(R.id.tvDescripcion)
        val tvTipoView: TextView = findViewById(R.id.tvTipo)
        val tvQuantityView: TextView = findViewById(R.id.tvCantidad)
        val tvDateView: TextView = findViewById(R.id.tvFecha)
        try{
            val movimentosDescription: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Description FROM movimientos WHERE UserID = ? ORDER BY MovementID DESC")!!
            movimentosDescription.setString(1,iduser)
            val tvDescription: ResultSet = movimentosDescription.executeQuery()
            i=0
            while(!movimentosDescription.resultSet.isLast and (i<5)) {
                tvDescription.next()
                actualDescription = "\t"+tvDescription.getString(1)+"\n"+actualDescription
                i++
            }
            val movimentosType: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Type FROM movimientos WHERE UserID = ? ORDER BY MovementID DESC")!!
            movimentosType.setString(1,iduser)
            val tvType: ResultSet = movimentosType.executeQuery()
            i=0
            while(!movimentosType.resultSet.isLast and (i<5)) {
                tvType.next()
                actualType = "\t"+tvType.getString(1)+"\n"+actualType
                i++
            }
            val movimientosQuantity: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Quantity FROM movimientos WHERE UserID = ? ORDER BY MovementID DESC")!!
            movimientosQuantity.setString(1,iduser)
            val tvQuantity: ResultSet = movimientosQuantity.executeQuery()
            i=0
            while(!movimientosQuantity.resultSet.isLast and (i<5)) {
                tvQuantity.next()
                actualQuantity = "\t$"+tvQuantity.getString(1)+"\n"+actualQuantity
                i++
            }
            val movimientosDate: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Date FROM movimientos WHERE UserID = ? ORDER BY MovementID DESC")!!
            movimientosDate.setString(1,iduser)
            val tvDate: ResultSet = movimientosDate.executeQuery()
            i=0
            while(!movimientosDate.resultSet.isLast and (i<5)) {
                tvDate.next()
                actualDate = "\t"+tvDate.getString(1)+"\n"+actualDate
                i++
            }
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
        tvDescriptionView.text = actualDescription
        tvTipoView.text = actualType
        tvQuantityView.text = actualQuantity
        tvDateView.text = actualDate
    }
}