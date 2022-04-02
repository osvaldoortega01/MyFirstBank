package com.example.myfirstbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.myfirstbank.databinding.ActivityAhorroBinding
import com.example.myfirstbank.databinding.ActivityConversionDivisasBinding

class ConversionDivisas : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivityConversionDivisasBinding
    var item = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion_divisas)
        binding = ActivityConversionDivisasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val listaDivisas = resources.getStringArray(R.array.lista_divisas)
        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_item,listaDivisas
        )
        with(binding.autotvDivisa){
            setAdapter(adapter)
            onItemClickListener = this@ConversionDivisas
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {//Recupero item seleccionado
        item = parent?.getItemAtPosition(position).toString()
    }
}