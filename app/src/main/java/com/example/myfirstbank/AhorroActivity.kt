package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.myfirstbank.databinding.ActivityAhorroBinding
import com.example.myfirstbank.databinding.ActivityMainBinding
import java.lang.Exception
import java.sql.PreparedStatement
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AhorroActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding: ActivityAhorroBinding
    var startpoint = 0
    var endpoint = 0
    var item = "Semanal"
    private var connectSQL = ConnectSQL()

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

        val userID: Int = 1
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

    fun openMainMenu() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}