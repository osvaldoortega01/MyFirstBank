package com.example.myfirstbank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import com.example.myfirstbank.MyFirstBank.Companion.prefs
import com.example.myfirstbank.databinding.ActivityAhorroBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AhorroActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding: ActivityAhorroBinding
    var startpoint = 0
    var endpoint = 0
    var item = "Semanal"
    private var connectSQL = ConnectSQL()

    //var actual = Calendar.getInstance()
    //var calendar = Calendar.getInstance()
    //    private val minutos = 0
    //    private val hora = 0
    //    private val dia = 0
    //    private val mes = 0
    //    private val anio = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAhorroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Drop Down List Tipos de ahorro
        val tiposAhorro = resources.getStringArray(R.array.tipos_ahorro)

        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_item,
            tiposAhorro
        )

        val userID: String = prefs.getId()
        binding.tvCuenta.setText(userID.toString())

        with(binding.autotvTipoAhorro){
            setAdapter(adapter)
            onItemClickListener = this@AhorroActivity
        }

        binding.etMetaAhorro.setText("0")

        binding.sbDuracion.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {//Imprimo progreso del slide bar
                binding.etDuracion.setText(progress.toString())
                val et_meta: EditText = findViewById(R.id.et_Meta_ahorro)
                if(et_meta.text.toString() != ""){
                    try {
                        val metaAhorro: Int = et_meta.text.toString().toInt()
                        val ahorroProgramado: Double
                        if(progress != 0){
                            if(item == "Diario"){
                                ahorroProgramado = ((metaAhorro*0.07) / 365.0) * progress
                            } else if(item == "Semanal"){
                                ahorroProgramado = ((metaAhorro*0.07) / 52.0) * progress
                            } else {
                                ahorroProgramado = ((metaAhorro*0.07) / 12.0) * progress
                            }
                        } else{
                            ahorroProgramado = 0.0
                        }

                        binding.etBonus.setText(String.format("%.3f",ahorroProgramado))

                    } catch (ex: Exception){
                        Toast.makeText(null, ex.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(null, "Primero Digite una meta de ahorro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {//Recupero punto de inicio
                if(seekBar != null){
                    startpoint = seekBar.progress
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {//Recupero punto final
                if(seekBar != null){
                    endpoint = seekBar.progress
                }
            }

        })

        val btn_ahorrar: ExtendedFloatingActionButton = findViewById(R.id.btn_ahorrar)
        btn_ahorrar.setOnClickListener{ createAhorro() }

        val btn_prueba: Button = findViewById(R.id.btn_prueba)
        btn_prueba.setOnClickListener{prueba()}
    }

    private fun validarMeta(metaCantidad: Int): Boolean{
        val userID: String = prefs.getId()
        var userCash: Double = 0.0
        try{
            val consultaUsuario: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT * FROM usuarios WHERE UserID = ?")!!
            consultaUsuario.setString(1,userID)
            val resultadoConsulta: ResultSet =  consultaUsuario.executeQuery()
            while (resultadoConsulta.next())
            {
                userCash = resultadoConsulta.getString(7).toDouble()
            }
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
        if(userCash >= metaCantidad){
            return true
        } else{
            return false
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {//Recupero item seleccionado
       item = parent?.getItemAtPosition(position).toString()
        try{
            if(item == "Diario"){
                binding.sbDuracion.max = 365
            } else if(item == "Semanal"){
                binding.sbDuracion.max = 52
            } else if(item == "Mensual"){
                binding.sbDuracion.max = 12
            }
        } catch (ex: Exception){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun createAhorro(){
        val et_meta: EditText = findViewById(R.id.et_Meta_ahorro)
        val metaAhorro: Int = et_meta.text.toString().toInt()
        if(validarMeta(metaAhorro)){
            var tipoAhorro: String = item
            val fechaCreacion: LocalDate = LocalDate.now()
            val userId: Int = binding.tvCuenta.text.toString().toInt()
            var fechaFin: LocalDate = LocalDate.now()
            try{
                if(item == "Diario"){
                    val addDays: Long = binding.etDuracion.text.toString().toLong()
                    fechaFin = fechaCreacion.plusDays(addDays)
                } else if(item == "Semanal"){
                    val addWeeks: Long = binding.etDuracion.text.toString().toLong()
                    fechaFin= fechaCreacion.plusWeeks(addWeeks)
                } else if(item == "Mensual"){
                    val addMonths: Long = binding.etDuracion.text.toString().toLong()
                    fechaFin = fechaCreacion.plusMonths(addMonths)
                }
            } catch (ex: Exception){
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }

            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.parse(fechaFin.toString())
            val cal = Calendar.getInstance()
            cal.time = date
            print(cal)
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR, 1);
            cal.set(Calendar.AM_PM, Calendar.AM);
            print(cal)
            try{
                val insertAhorro: PreparedStatement = connectSQL.dbConn()?.prepareStatement("INSERT INTO ahorros VALUES(?,?,?,?,?)")!!
                insertAhorro.setInt(1, userId)
                insertAhorro.setInt(2, metaAhorro)
                insertAhorro.setString(3, item)
                insertAhorro.setString(4, fechaFin.toString())
                insertAhorro.setString(5, fechaCreacion.toString())
                insertAhorro.executeUpdate()
                Toast.makeText(this, "Ahorro creado con éxito", Toast.LENGTH_SHORT).show()
            } catch (ex: SQLException){
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }

            try{
                var currentCash: Double = 0.0
                val consultaUsuarios: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT * FROM usuarios WHERE UserID = ?")!!
                consultaUsuarios.setString(1,userId.toString())
                val resultadoConsulta: ResultSet =  consultaUsuarios.executeQuery()
                while (resultadoConsulta.next()){
                    currentCash = resultadoConsulta.getString(7).toDouble()
                }

                val nuevoCash: Double = currentCash - metaAhorro
                val retirarCash: PreparedStatement = connectSQL.dbConn()?.prepareStatement("UPDATE usuarios SET Cash = ? WHERE UserID = ?")!!
                retirarCash.setDouble(1, nuevoCash)
                retirarCash.setInt(2, userId)
                retirarCash.executeUpdate()
                Toast.makeText(this, "Retiro Efectuado", Toast.LENGTH_SHORT).show()
            } catch (ex: SQLException){
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }

            try{
                var idAhorro: Int = 0
                val consultaAhorros: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT MAX(SavingID) FROM ahorros")!!
                val resultadoConsulta: ResultSet =  consultaAhorros.executeQuery()
                while (resultadoConsulta.next()){
                    idAhorro = resultadoConsulta.getString(1).toInt()
                }

                val insertMovimiento: PreparedStatement = connectSQL.dbConn()?.prepareStatement("INSERT INTO movimientos VALUES(?,?,?,?,?)")!!
                insertMovimiento.setInt(1, userId)
                insertMovimiento.setString(2, "Ahorro #"+idAhorro.toString()+" - INICIADO (Plan: "+item+" ("+binding.etDuracion.text.toString()+"))")
                insertMovimiento.setString(3, "Retiro")
                insertMovimiento.setDouble(4, metaAhorro.toDouble())
                insertMovimiento.setString(5, LocalDateTime.now().toString())
                insertMovimiento.executeUpdate()
                Toast.makeText(this, "Movimiento creado con éxito", Toast.LENGTH_SHORT).show()
            } catch (ex: SQLException){
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }

            val tag = generatekey()
            val alertTime = cal.timeInMillis - System.currentTimeMillis()
            val random = (Math.random()*50+1).toInt ()
            val data = EnviarData("Plazo de ahorro finalizado", "El plazo del ahorro a finalizado, consulte los detalles", random)
            Worknoti.GuardarNoti(alertTime, data, "tag")
            Toast.makeText( this,"Notificacion Guardada.", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText( this,"No tiene dinero suficiente, increse una cantidad menor...", Toast.LENGTH_SHORT).show()
        }

    }

    private fun generatekey (): String {
        return UUID.randomUUID().toString()
    }
    private fun EnviarData(titulo:String, detalle:String, id_noti:Int): Data {
        return Data. Builder ()
            .putString("Titulo", titulo)
            .putString("Detalle", detalle)
            .putInt("idnoti", id_noti). build()
    }

    fun prueba(){
        val tag = generatekey()
//        val alertTime = calendar.timeInMillis - System.currentTimeMillis()
        val alertTime = 30000.toLong()
        val random = (Math.random()*50+1).toInt ()
        val data = EnviarData("Plazo de ahorro finalizado", "El plazo del ahorro a finalizado, consulte los detalles", random)
        Worknoti.GuardarNoti(alertTime, data, "tag1")
        Toast.makeText( this,"Notificación Guardada.", Toast.LENGTH_SHORT).show()
    }
}