package com.example.myfirstbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.example.myfirstbank.MyFirstBank.Companion.prefs
import com.google.android.material.textfield.TextInputEditText
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class MainActivity : AppCompatActivity() {
    private var connectSQL = ConnectSQL()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton: Button = findViewById(R.id.btn_login)
        loginButton.setOnClickListener { validateLogin() }
        val registerButton: Button = findViewById(R.id.btn_Register)
        registerButton.setOnClickListener{ openRegister() }
        checkUserValues()
    }
    private fun validateLogin(){
        var tiet_Username: TextInputEditText = findViewById(R.id.tiet_Username)
        var tiet_Password: TextInputEditText = findViewById(R.id.tiet_Password)

        if (TextUtils.isEmpty(tiet_Username.text)) {
            tiet_Username.setError("El nombre de usuario es requerido")
        }
        else if (TextUtils.isEmpty(tiet_Password.text)){
            tiet_Password.setError("La contraseña es requerida")
        }
        else {
            try{
                val usuario: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT UserPassword FROM usuarios WHERE Username = ?")!!
                usuario.setString(1, tiet_Username.text.toString())
                val contrasena: ResultSet = usuario.executeQuery()
                contrasena.next()
                if (tiet_Password.text.toString() == contrasena.getString(1)){
                    Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                    try{
                        val getidusuario: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT UserID FROM usuarios WHERE Username = ?")!!
                        getidusuario.setString(1, tiet_Username.text.toString())
                        val iduser: ResultSet = getidusuario.executeQuery()
                        iduser.next()
                        fun accessToDetail(){
                            if(tiet_Username.text.toString().isNotEmpty() && tiet_Password.text.toString().isNotEmpty()){
                                prefs.saveUsername(tiet_Username.text.toString())
                                prefs.savePassword(tiet_Password.text.toString())
                                prefs.saveId(iduser.getString(1))
                                openMainMenu()
                            }else{

                            }
                        }
                        accessToDetail()
                    }catch (ex:SQLException){
                        Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
            }
            catch (ex: SQLException){
                Toast.makeText(this, "${ex.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun openRegister(){
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    fun openMainMenu() {
        var intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun checkUserValues(){
        if(prefs.getUsername().isNotEmpty()){
            openMainMenu()
        }
    }
}