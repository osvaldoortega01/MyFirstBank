package com.example.myfirstbank

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstbank.MyFirstBank.Companion.prefs
import com.example.myfirstbank.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.sql.*
import java.time.LocalDateTime


class RegisterActivity : AppCompatActivity() {

    private var connectSQL = ConnectSQL()

    private val FILE_NAME = "ContrasenaUsuario.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btn_Register: Button = findViewById(R.id.btn_Register)
        btn_Register.setOnClickListener{
            registerUser()
            save()
        }
    }

    private fun registerUser(){
        val etcompletename: TextInputEditText = findViewById(R.id.tiet_CompleteName)
        val etusername: TextInputEditText = findViewById(R.id.tiet_Username)
        val etpassword: TextInputEditText = findViewById(R.id.tiet_Password)
        val etconfirmpassword: TextInputEditText = findViewById(R.id.tiet_ConfirmPassword)
        val etemailaddress: TextInputEditText = findViewById(R.id.tiet_EmailAddress)
        val fecha = LocalDateTime.now()

        val pas1 = etpassword.text.toString()
        val cpas1 = etconfirmpassword.text.toString()
        try {
            // Se encapsula todos los peticiones SQL con un try catch, para que no crasheé la app
            val norepeatuser: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT UserID FROM usuarios WHERE Username = ?")!!
            norepeatuser.setString(1, etusername.text.toString())
            val verifuser: ResultSet = norepeatuser.executeQuery()

            if(!verifuser.next() ){
                if (pas1==cpas1){
                    if (TextUtils.isEmpty(etusername.text))
                    {
                        etusername.setError("El nombre de usuario es requerido")
                    }
                    else if(TextUtils.isEmpty(etcompletename.text))
                    {
                        etcompletename.setError("El nombre completo es requerido")
                    }
                    else if (TextUtils.isEmpty(etpassword.text))
                    {
                        etpassword.setError("La contraseña es requerida")
                    }
                    else if (TextUtils.isEmpty(etemailaddress.text))
                    {
                        etemailaddress.setError("El correo es requerido")
                    }
                    else {

                        try {
                            val nuevoUsuario: PreparedStatement = connectSQL.dbConn()
                                ?.prepareStatement("insert into usuarios values (?,?,?,?,?,?,?,?)")!!
                            nuevoUsuario.setString(1, etcompletename.text.toString())
                            nuevoUsuario.setString(2, etusername.text.toString())
                            nuevoUsuario.setString(3, etpassword.text.toString())
                            nuevoUsuario.setString(4, etemailaddress.text.toString())
                            nuevoUsuario.setString(5, "0")
                            nuevoUsuario.setDouble(6, 0.0)
                            nuevoUsuario.setInt(7, 10)
                            nuevoUsuario.setString(8, fecha.toString())
                            nuevoUsuario.executeUpdate()
                            Toast.makeText(this, "Cuenta Creada Existosamente", Toast.LENGTH_SHORT)
                                .show()
                            try {
                                val getidusuario: PreparedStatement = connectSQL.dbConn()
                                    ?.prepareStatement("SELECT UserID FROM usuarios WHERE Username = ?")!!
                                getidusuario.setString(1, etusername.text.toString())
                                val iduser: ResultSet = getidusuario.executeQuery()
                                iduser.next()
                                fun accessToDetail() {
                                    if (etusername.text.toString()
                                            .isNotEmpty() && etpassword.text.toString().isNotEmpty()
                                    ) {
                                        prefs.saveUsername(etpassword.text.toString())
                                        prefs.savePassword(etusername.text.toString())
                                        prefs.saveId(iduser.getString(1))
                                        openParental()
                                    } else {

                                    }
                                }
                                accessToDetail()
                            } catch (ex: SQLException) {
                                Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                            }
                        } catch (ex: SQLException) {
                            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_LONG).show()
            }

        }
        catch (ex: SQLException){
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }


    }
    fun openParental() {
        var intent = Intent(this, Parental_Control_Activity::class.java)
        startActivity(intent)
    }

    fun save() {

        val etpassword: TextInputEditText = findViewById(R.id.tiet_Password)
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE)
            fos.write(etpassword.text.toString().toByteArray())

            Toast.makeText(
                this, "Guardado en $filesDir/$FILE_NAME",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


}