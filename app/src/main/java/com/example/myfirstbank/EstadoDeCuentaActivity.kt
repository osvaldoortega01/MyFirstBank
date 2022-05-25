package com.example.myfirstbank

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.Toast
import androidx.compose.ui.text.Paragraph
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import org.w3c.dom.Document
import java.io.File
import java.io.FileOutputStream
import java.io.IOError
import java.io.IOException
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter


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

        if(checkPermission()) {
            Toast.makeText(this, "Permiso Aceptado", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions();
        }


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

    fun crearPdf() {
        var path = Environment.getExternalStorageDirectory().absolutePath+"/MyFirstBank"

        val dir = File(path)
        if (!dir.exists())
            dir.mkdirs()

        val file = File(dir, "estadoCuenta.pdf")
        val fileOutputStream = FileOutputStream(file)

        val documento = Document()
        PdfWriter.getInstance(documento, fileOutputStream)

        documento.open()

        val titulo = Paragraph(
            "Estado de cuenta \n\n\n",
            FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLUE)
        )

        documento.add(titulo)

        var tabla = PdfPTable(4)
        tabla.addCell("Descripción")
        tabla.addCell("Tipo")
        tabla.addCell("Cantidad")
        tabla.addCell("Fecha")


        documento.add(tabla)

        documento.close()
    }

    private fun checkPermission(): Boolean {
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            200
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 200) {
            if (grantResults.size > 0) {
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

}