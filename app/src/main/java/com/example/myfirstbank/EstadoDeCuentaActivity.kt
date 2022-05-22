package com.example.myfirstbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.Toast
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class EstadoDeCuentaActivity : AppCompatActivity() {

    private var connectSQL = ConnectSQL()
    var tUltimosMovimientos: TableLayout?=null
    var nombreMesSeleccionado : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estado_de_cuenta)
        val spinner = findViewById<Spinner>(R.id.sp_meses)
        val et_Ano = findViewById<EditText>(R.id.et_Ano)
        val btn_AplicarFiltro: Button = findViewById(R.id.btn_AplicarFiltro)
        val btn_Clear: Button = findViewById(R.id.btn_Clear)

        val lista = resources.getStringArray(R.array.combo_meses)
        val iduser = MyFirstBank.prefs.getId()

        tUltimosMovimientos = findViewById(R.id.tUltimosMovimientos)

        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)



        spinner.adapter=adaptador
        spinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                when(position){
                    0-> nombreMesSeleccionado="1"
                    1-> nombreMesSeleccionado="2"
                    2-> nombreMesSeleccionado="3"
                    3-> nombreMesSeleccionado="4"
                    4-> nombreMesSeleccionado="5"
                    5-> nombreMesSeleccionado="6"
                    6-> nombreMesSeleccionado="7"
                    7-> nombreMesSeleccionado="8"
                    8-> nombreMesSeleccionado="9"
                    9-> nombreMesSeleccionado="10"
                    10-> nombreMesSeleccionado="11"
                    11-> nombreMesSeleccionado="12"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@EstadoDeCuentaActivity, "Seleccione un mes para continuar", Toast.LENGTH_LONG).show()
            }
        }
        btn_AplicarFiltro.setOnClickListener{
            var ano: String = et_Ano.text.toString()
            dataQuery(iduser, nombreMesSeleccionado, ano)
        }
            btn_Clear.setOnClickListener{
            tUltimosMovimientos?.removeAllViewsInLayout()
        }

    }
    fun dataQuery(iduser: String, nombreMesSeleccionado: String, et_Ano: String){

        try{
            val movimentosDescription: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT * FROM movimientos WHERE UserID = ? AND MONTH(Date) = ? AND YEAR(Date) = ?")!!
            movimentosDescription.setString(1,iduser)
            movimentosDescription.setString(2, nombreMesSeleccionado)
            movimentosDescription.setString(3, et_Ano)
            val tvDescription: ResultSet = movimentosDescription.executeQuery()

            while (tvDescription.next())
            {
                val registro = LayoutInflater.from(this).inflate(R.layout.item_table_layout_os, null, false)
                val tvDescripcion = registro.findViewById<View>(R.id.tvDescripcion) as TextView
                val tvTipo = registro.findViewById<View>(R.id.tvTipo) as TextView
                val tvCantidad = registro.findViewById<View>(R.id.tvCantidad) as TextView
                val tvFecha = registro.findViewById<View>(R.id.tvFecha) as TextView

                tvDescripcion.setText(tvDescription.getString(3))
                tvTipo.setText(tvDescription.getString(4))
                tvCantidad.setText("$"+tvDescription.getString(5))
                tvFecha.setText(tvDescription.getString(6))
                tUltimosMovimientos?.addView(registro)
            }

        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
    }

}