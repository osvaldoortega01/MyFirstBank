package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myfirstbank.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class Profile_Activity : AppCompatActivity() {

    private var connectSQL = ConnectSQL()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val bundle = intent.extras
        val iduser = bundle?.getString("iduser")
        val txtnombre: TextInputEditText = findViewById(R.id.tie_CompleteName)
        val txtusuario: TextInputEditText = findViewById(R.id.tie_Username)
        val txtcontraseña: TextInputEditText = findViewById(R.id.tie_Password)
        val txtconfcontraseña: TextInputEditText = findViewById(R.id.tie_ConfirmPassword)

        try{
            val txtNOM: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT FullName FROM usuarios WHERE UserID = ?")!!
            txtNOM.setString(1, iduser.toString())
            val tiNOM: ResultSet = txtNOM.executeQuery()
            tiNOM.next()
            txtnombre.setText(tiNOM.getString(1))
            val txtUSE: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Username FROM usuarios WHERE UserID = ?")!!
            txtUSE.setString(1, iduser.toString())
            val tiUSE: ResultSet = txtUSE.executeQuery()
            tiUSE.next()
            txtusuario.setText(tiUSE.getString(1))
            val txtCONT: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT UserPassword FROM usuarios WHERE UserID = ?")!!
            txtCONT.setString(1, iduser.toString())
            val tiCONT: ResultSet = txtCONT.executeQuery()
            tiCONT.next()
            txtcontraseña.setText(tiCONT.getString(1))
            txtconfcontraseña.setText(tiCONT.getString(1))
        }catch(ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }

        var btn_CerrarSesion: Button = findViewById(R.id.btn_CerrarSesion)
        btn_CerrarSesion.setOnClickListener{ openMain() }

        var btn_EstadoDeCuenta: Button = findViewById(R.id.btn_EstadoDeCuenta)
        btn_EstadoDeCuenta.setOnClickListener{ openEstado() }

        var btn_AplicarCambios: Button = findViewById(R.id.btn_AplicarCambios)
        btn_AplicarCambios.setOnClickListener{ editarus() }
    }

    fun editarus(){
        val bundle = intent.extras
        val iduser = bundle?.getString("iduser")
        val ti_nombre: TextInputEditText = findViewById(R.id.tie_CompleteName)
        val ti_tusuario: TextInputEditText = findViewById(R.id.tie_Username)
        val ti_contraseña: TextInputEditText = findViewById(R.id.tie_Password)
        val ti_confcontraseña: TextInputEditText = findViewById(R.id.tie_ConfirmPassword)
        val pas1 = ti_contraseña.text.toString()
        val cpas1 = ti_confcontraseña.text.toString()
        if(pas1==cpas1){
           try{
               val editarperfl: PreparedStatement = connectSQL.dbConn()
                   ?.prepareStatement("UPDATE usuarios SET FullName = ?, Username = ?, UserPassword =? WHERE UserID = ?")!!
               editarperfl.setString(1, ti_nombre.text.toString())
               editarperfl.setString(2, ti_tusuario.text.toString())
               editarperfl.setString(3, ti_contraseña.text.toString())
               editarperfl.setString(4, iduser.toString())
               editarperfl.executeUpdate()
               Toast.makeText(this, "Cambios realizados exitosamente", Toast.LENGTH_SHORT).show()
               openMainMenu()
           }catch(ex: SQLException){
               Toast.makeText(this, "Error al realizar cambios", Toast.LENGTH_SHORT).show()
           }
        }
        else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }
    }

    fun openMain(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun openEstado() {
        val bundle = intent.extras
        val iduser = bundle?.getString("iduser")
        var intent = Intent(this, EstadoDeCuentaActivity::class.java)
        intent.putExtra("iduser", iduser.toString())
        startActivity(intent)
    }

    fun openMainMenu(){
        val bundle = intent.extras
        val iduser = bundle?.getString("iduser")
        var intent = Intent(this, MainMenuActivity::class.java)
        intent.putExtra("iduser", iduser.toString())
        startActivity(intent)
    }
}