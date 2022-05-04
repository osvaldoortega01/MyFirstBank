package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.myfirstbank.databinding.ActivityAhorroBinding
import com.example.myfirstbank.databinding.ActivityMainBinding

class AhorroActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding: ActivityAhorroBinding
    var startpoint = 0
    var endpoint = 0
    var item = "Semanal"

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

        with(binding.autotvTipoAhorro){
            setAdapter(adapter)
            onItemClickListener = this@AhorroActivity
        }

        binding.sbDuracion.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {//Imprimo progreso del slide bar
                binding.etDuracion.setText(progress.toString())
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
    }

    private fun createAhorro(){
        val textview_meta: TextView = findViewById(R.id.tv_Meta)
        var metaAhorro: Int = textview_meta.text.toString().toInt()

        var tipoAhorro: String = item
        var duracion: Int = binding.etDuracion.text.toString().toInt()
    }

    fun openMainMenu() {
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}