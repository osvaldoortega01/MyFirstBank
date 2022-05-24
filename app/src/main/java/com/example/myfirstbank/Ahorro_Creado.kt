package com.example.myfirstbank

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstbank.databinding.ActivityAhorroBinding
import com.example.myfirstbank.databinding.ActivityAhorroCreadoBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId


class Ahorro_Creado : AppCompatActivity() {
    private lateinit var binding: ActivityAhorroCreadoBinding
    private var connectSQL = ConnectSQL()
    private var ahorroID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAhorroCreadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_ahorro_creado)
        try {
            cargarAhorro()
        } catch (ex: Exception){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

        val btn_finalizarAhorro: ExtendedFloatingActionButton = findViewById(R.id.btn_FinalizarAhorro)
        btn_finalizarAhorro.setOnClickListener{ finalizarAhorro() }
    }

    private fun cargarAhorro(){
        val iduser: String = MyFirstBank.prefs.getId()
        val fechaActual: LocalDate = LocalDate.now()

        var metaAhorro: Int = 0
        var tipo: String = ""
        var fechaFin: Date = Date(2020, 2, 1)
        var fechaInicio: Date = Date(2020, 2, 1)
        var bonus: Double = 0.0

        try{
            val consultaAhorros: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT * FROM ahorros WHERE UserID = ? AND FinalDate > ?")!!
            consultaAhorros.setString(1,iduser)
            consultaAhorros.setString(2, fechaActual.toString())

            val resultadoConsulta: ResultSet =  consultaAhorros.executeQuery()

            while (resultadoConsulta.next())
            {
                ahorroID = resultadoConsulta.getString(1).toInt()
                metaAhorro = resultadoConsulta.getString(3).toInt()
                tipo = resultadoConsulta.getString(4)
                fechaFin = resultadoConsulta.getDate(5)
                fechaInicio = resultadoConsulta.getDate(6)
                if(tipo == "Diario"){
                    bonus = ((metaAhorro*0.07) / 365.0) * tiempoAhorro(tipo, fechaInicio, fechaFin)
                } else if(tipo == "Semanal"){
                    bonus = ((metaAhorro*0.07) / 52.0) * tiempoAhorro(tipo, fechaInicio, fechaFin)
                } else {
                    bonus = ((metaAhorro*0.07) / 12.0) * tiempoAhorro(tipo, fechaInicio, fechaFin)
                }

            }
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

        binding.tvAhorroCantidad.text = metaAhorro.toString() + ".00"
        binding.tvInicioFechaAhorro.text = fechaInicio.toString()
        binding.tvFinFechaAhorro.text = fechaFin.toString()
        binding.tvBonusAhorro.text = "$ "+bonus.toString()
        binding.tvPlan.text = tipo
    }

    private fun tiempoAhorro(tipo: String, fechaInicio: Date, fechaFin: Date): Double{
        val diff: Period = Period.between(fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        val dias: Double = diff.days.toDouble()
        val semanas: Double = Math.floor(dias/7)
        val meses: Double = diff.months.toDouble()

        if(tipo == "Diario"){
            return dias
        } else if(tipo == "Semanal"){
            return semanas
        } else {
            return meses
        }
    }

    private fun finalizarAhorro(){
        try{
            val iduser: String = MyFirstBank.prefs.getId()
            var currentCash: Double = 0.0
            val consultaUsuarios: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT * FROM usuarios WHERE UserID = ?")!!
            consultaUsuarios.setString(1,iduser)
            val resultadoConsulta: ResultSet =  consultaUsuarios.executeQuery()
            while (resultadoConsulta.next()){
                currentCash = resultadoConsulta.getString(7).toDouble()
            }

            val nuevoCash: Double = currentCash + binding.tvAhorroCantidad.text.toString().toDouble()
            val retirarCash: PreparedStatement = connectSQL.dbConn()?.prepareStatement("UPDATE usuarios SET Cash = ? WHERE UserID = ?")!!
            retirarCash.setDouble(1, nuevoCash)
            retirarCash.setInt(2, iduser.toInt())
            retirarCash.executeUpdate()
            Toast.makeText(this, "Deposito Efectuado", Toast.LENGTH_SHORT).show()

            val insertMovimiento: PreparedStatement = connectSQL.dbConn()?.prepareStatement("INSERT INTO movimientos VALUES(?,?,?,?,?)")!!
            insertMovimiento.setInt(1, iduser.toInt())
            insertMovimiento.setString(2, "Ahorro #"+ahorroID.toString()+" - CANCELADO (Plan: "+binding.tvPlan.text.toString()+", Fecha Fin Original: "+binding.tvFinFechaAhorro.text.toString()+")")
            insertMovimiento.setString(3, "Deposito")
            insertMovimiento.setDouble(4, binding.tvAhorroCantidad.text.toString().toDouble())
            insertMovimiento.setString(5, LocalDateTime.now().toString())
            insertMovimiento.executeUpdate()
            Toast.makeText(this, "Movimiento creado con éxito", Toast.LENGTH_SHORT).show()

            val currentDate: LocalDate = LocalDate.now()
            val finalizarAhorro: PreparedStatement = connectSQL.dbConn()?.prepareStatement("UPDATE ahorros SET FinalDate = ? WHERE SavingID = ?")!!
            finalizarAhorro.setString(1, currentDate.toString())
            finalizarAhorro.setInt(2, ahorroID)
            finalizarAhorro.executeUpdate()

            Toast.makeText( this,"Ahorro eliminado exitosamente.", Toast.LENGTH_SHORT).show()
            Toast.makeText( this,"Perdiste los intereses :(.", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}