package com.example.myfirstbank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import com.example.myfirstbank.MyFirstBank.Companion.prefs
import com.example.myfirstbank.databinding.ActivityAhorroBinding
import java.sql.PreparedStatement
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

    var actual = Calendar.getInstance()
    var calendar = Calendar.getInstance()
    private val minutos = 0
    private val hora = 0
    private val dia = 0
    private val mes = 0
    private val anio = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAhorroBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.sbDuracion.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {//Imprimo progreso del slide bar
                binding.etDuracion.setText(progress.toString())

                try {
                    val et_meta: EditText = findViewById(R.id.et_Meta_ahorro)
                    val metaAhorro: Int = et_meta.text.toString().toInt()
                    val ahorroProgramado: Double
                    if(progress.toInt() != 0){
                        ahorroProgramado= metaAhorro / progress.toDouble()
                    } else{
                        ahorroProgramado = 0.0
                    }

                    binding.etAhorroGenerado.setText(String.format("%.3f",ahorroProgramado))
                    val interes: Double = ahorroProgramado*0.00095

                    binding.etBonus.setText(String.format("%.3f",interes))
                } catch (ex: Exception){
                    Toast.makeText(null, ex.message.toString(), Toast.LENGTH_SHORT).show()
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

        val btn_regresar: Button = findViewById(R.id.btn_regresar)
        btn_regresar.setOnClickListener{ openMainMenu() }

        val btn_ahorrar: Button = findViewById(R.id.btn_ahorrar)
        btn_ahorrar.setOnClickListener{createAhorro()}

        val btn_prueba: Button = findViewById(R.id.btn_prueba)
        btn_prueba.setOnClickListener{prueba()}
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
                fechaFin= fechaCreacion.plusDays(addWeeks)
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
        try{
            val insertAhorro: PreparedStatement = connectSQL.dbConn()?.prepareStatement("INSERT INTO ahorros VALUES(?,?,?,?,?)")!!
            insertAhorro.setInt(1, userId)
            insertAhorro.setInt(2, metaAhorro)
            insertAhorro.setString(3, item)
            insertAhorro.setString(4, fechaFin.toString())
            insertAhorro.setString(5, fechaCreacion.toString())
            insertAhorro.executeUpdate()
            Toast.makeText(this, "Ahorro Creado Existosamente", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }

        try{
            val insertMovimiento: PreparedStatement = connectSQL.dbConn()?.prepareStatement("INSERT INTO movimientos VALUES(?,?,?,?,?)")!!
            insertMovimiento.setInt(1, userId)
            insertMovimiento.setString(2, "Ahorro 1 - "+fechaCreacion.toString())
            insertMovimiento.setString(3, item)
            insertMovimiento.setDouble(4, binding.etAhorroGenerado.text.toString().toDouble())
            insertMovimiento.setString(5, LocalDateTime.now().toString())
            insertMovimiento.executeUpdate()
            Toast.makeText(this, "Movimiento Creado Existosamente", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
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

    fun openMainMenu() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun prueba(){
        val et_meta: EditText = findViewById(R.id.et_Meta_ahorro)
        val metaAhorro: Int = et_meta.text.toString().toInt()
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
                fechaFin= fechaCreacion.plusDays(addWeeks)
            } else if(item == "Mensual"){
                val addMonths: Long = binding.etDuracion.text.toString().toLong()
                fechaFin = fechaCreacion.plusMonths(addMonths)
            }
        } catch (ex: Exception){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }
        val tag = generatekey()
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 3);
        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        val alertTime = calendar.timeInMillis - System.currentTimeMillis()
        val random = (Math.random()*50+1).toInt ()
        val data = EnviarData("Plaso de ahorro finalizado", "El plaso del ahorro a finalizado, consulta los detalles", random)
        Worknoti.GuardarNoti(alertTime, data, "tag1")
        Toast.makeText( this,"Notificacion Guardada.", Toast.LENGTH_SHORT).show()
    }
}