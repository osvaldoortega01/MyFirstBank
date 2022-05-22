package com.example.myfirstbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.myfirstbank.databinding.ActivityAhorroBinding
import com.example.myfirstbank.databinding.ActivityConversionDivisasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class ConversionDivisas : AppCompatActivity() {
    private lateinit var binding: ActivityConversionDivisasBinding
    var latestConcurrencyValue : Float = 0f
    var currencyConverted: Float = 0f
    var nombreMonedaSeleccionada : String = ""
    private var connectSQL = ConnectSQL()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion_divisas)
        binding = ActivityConversionDivisasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtiene el saldo de la cuenta a traves del UserID
        val iduser = MyFirstBank.prefs.getId()
        val tvsaldoDig: TextView= findViewById(R.id.textView9)
        try{
            val txtsaldo: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Cash FROM usuarios WHERE UserID = ?")!!
            txtsaldo.setString(1, iduser)
            val tvsaldo: ResultSet = txtsaldo.executeQuery()
            tvsaldo.next()
            tvsaldoDig.setText("$ "+tvsaldo.getString(1)+" mxn")
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

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
                        3-> nombreMonedaSeleccionada="GBP"
                        4-> nombreMonedaSeleccionada="CHF"
                        5-> nombreMonedaSeleccionada="JPY"
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(this@ConversionDivisas, "Seleccione una moneda para continuar", Toast.LENGTH_LONG).show()
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
            val tvValorPesoActual: TextView = findViewById(R.id.textView14)
            val tvValorConversion: TextView = findViewById(R.id.textView12)
            runOnUiThread{
                if (call.isSuccessful){
                    if (currencyValue != null) {
                        latestConcurrencyValue=0f
                        currencyConverted=0f
                        latestConcurrencyValue=currencyValue.result
                        tvValorPesoActual.text="$ "+latestConcurrencyValue.toString()+" "+nombreMonedaSeleccionada
                        //Aqui se multiplicará el elelemnto que contiene la cantidad de dinero ahorrado
                        currencyConverted=10500.30f*latestConcurrencyValue
                        tvValorConversion.text="$ "+currencyConverted.toString()+" "+nombreMonedaSeleccionada
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