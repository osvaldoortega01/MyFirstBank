package com.example.myfirstbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner


class EstadoDeCuentaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estado_de_cuenta)
        val spinner = findViewById<Spinner>(R.id.sp_meses)

        val lista = resources.getStringArray(R.array.combo_meses)

        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)

        spinner.adapter=adaptador

    }
}