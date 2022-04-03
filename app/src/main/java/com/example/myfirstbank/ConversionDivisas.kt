package com.example.myfirstbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.myfirstbank.databinding.ActivityAhorroBinding
import com.example.myfirstbank.databinding.ActivityConversionDivisasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ConversionDivisas : AppCompatActivity() {
    private lateinit var binding: ActivityConversionDivisasBinding
    var latestConcurrencyValue : Float = 0f
    var currencyConverted: Float = 0f
    var nombreMonedaSeleccionada : String = ""
    //val vistaValor: TextView = findViewById(R.id.textView12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion_divisas)
        binding = ActivityConversionDivisasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val convertirButton: Button = findViewById(R.id.buttonConvertir)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val lista = resources.getStringArray(R.array.lista_divisas)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)
        spinner.adapter = adaptador

        spinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                    when(position){
                        0-> nombreMonedaSeleccionada="USD"
                        1-> nombreMonedaSeleccionada="EUR"
                        2-> nombreMonedaSeleccionada="BTC"
                        3-> nombreMonedaSeleccionada="ETC" //Remover Ethereum pues no lo incluye el API
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        convertirButton.setOnClickListener {
            getCurrencyLatestValue(nombreMonedaSeleccionada)
        }



    }


    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.exchangerate.host/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Métodos que trabajan mediante corrutinas
    private fun getCurrencyLatestValue(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getLatestValues("convert?from=MXN&to=$query")
            val currencyValue = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    if (currencyValue != null) {
                        latestConcurrencyValue=0f
                        currencyConverted=0f

                        latestConcurrencyValue=currencyValue.result
                        println(latestConcurrencyValue)
                        //Aqui se multiplicará el elelemnto que contiene la cantidad de dinero ahorrado
                        currencyConverted=10500.30f*latestConcurrencyValue
                        println(currencyConverted)
                    }
                }
                else{
                    showError()
                }
            }
        }
    }

    private fun showError(){
        Toast.makeText(this,"Ha ocurrido un error", Toast.LENGTH_SHORT)
            .show()
    }

}