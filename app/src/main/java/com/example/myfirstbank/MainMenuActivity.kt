package com.example.myfirstbank

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.myfirstbank.MyFirstBank.Companion.prefs
import java.sql.*
import java.util.*
import kotlin.math.sqrt

class MainMenuActivity : AppCompatActivity(){
    //Variables para trabajar el acelerometro
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    var mediaPlayer : MediaPlayer? = null
    //Fin variables para trabajar el acelerometro

    private var connectSQL = ConnectSQL()

       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

           val iduser = prefs.getId()
           val tvsaldoDig: TextView= findViewById(R.id.TV_SaldoDig)
           try{
               val txtsaldo: PreparedStatement = connectSQL.dbConn()?.prepareStatement("SELECT Cash FROM usuarios WHERE UserID = ?")!!
               txtsaldo.setString(1, iduser)
               val tvsaldo: ResultSet = txtsaldo.executeQuery()
               tvsaldo.next()
               tvsaldoDig.setText("$ "+tvsaldo.getString(1)+" mxn")
           }catch(ex: SQLException){
               Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
           }

        var btn_MenuMovimientos: Button = findViewById(R.id.btn_MenuMovimientos)
        btn_MenuMovimientos.setOnClickListener{ openMenuMovimientos() }


        var btn_MenuControlParental: Button = findViewById(R.id.btn_MenuControlParental)
        btn_MenuControlParental.setOnClickListener{ openControlParental() }

        var btn_Divisas: Button = findViewById(R.id.btn_Divisas)
        btn_Divisas.setOnClickListener { openDivisas() }

        var btn_ahorros: ExtendedFloatingActionButton = findViewById(R.id.btn_ahorros)
           btn_ahorros.setOnClickListener{ openAhorros() }

        var btn_perfil: ExtendedFloatingActionButton = findViewById(R.id.btn_perfil)
           btn_perfil.setOnClickListener{ openPerfil() }

           // Instancia del acelerometro
           sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
           Objects.requireNonNull(sensorManager)!!
               .registerListener(sensorListener, sensorManager!!
                   .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
           acceleration = 10f
           currentAcceleration = SensorManager.GRAVITY_EARTH
           lastAcceleration = SensorManager.GRAVITY_EARTH
           //Fin instancia del acelerómetro
    }



    fun openMenuMovimientos(){
        var intent = Intent(this, Movimientos::class.java)
        startActivity(intent)
    }
    fun openControlParental(){
        val idpcv = 1
        var intent = Intent(this, PCV_Activity::class.java)
        intent.putExtra("idpcv", idpcv.toString())
        startActivity(intent)
    }
    fun openDivisas(){
        val intent = Intent(this, ConversionDivisas::class.java)
        startActivity(intent)
    }
    fun openAhorros(){
        var intent = Intent(this, AhorroActivity::class.java)
        startActivity(intent)
    }
    fun openPerfil(){
        val idpcv = 2
        var intent = Intent(this, PCV_Activity::class.java)
        intent.putExtra("idpcv", idpcv.toString())
        startActivity(intent)
    }
    //Métodos para reproducir sonido y de configuraciones para el acelerometro
    fun playSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.coins_sound)
            mediaPlayer!!.start()
        } else {
            mediaPlayer!!.start()
        }
    }
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
                //Aqui ponemos el evento que queremos que se desencadene al agitar el teléfono
                playSound()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }
    //Fin de métodos para reproducir sonido y de configuraciones para el acelerometro
}