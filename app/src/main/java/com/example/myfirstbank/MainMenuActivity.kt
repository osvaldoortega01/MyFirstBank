package com.example.myfirstbank

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainMenuActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private val mp: MediaPlayer = MediaPlayer.create(this, R.raw.coins_sound)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        var btn_MenuMovimientos: Button = findViewById(R.id.btn_MenuMovimientos)
        btn_MenuMovimientos.setOnClickListener{ openMenuMovimientos() }

        var btn_MenuControlParental: Button = findViewById(R.id.btn_MenuControlParental)
        btn_MenuControlParental.setOnClickListener{ openControlParental() }

        var ibtn_ahorros: ImageButton = findViewById(R.id.ibtn_ahorros)
        ibtn_ahorros.setOnClickListener{ openAhorros() }

        var ibtn_perfil: ImageButton = findViewById(R.id.ibtn_perfil)
        ibtn_perfil.setOnClickListener{ openPerfil() }

        detectaMovimiento()
    }

    fun detectaMovimiento(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also{
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides: Float =event.values[0]

            if(sides < -3){
                if(sides > 3){
                    mp.start()
                }
            } else if(sides > 3){
                if(sides < -3){
                    mp.start()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() { //Liberamos la memoria del sensor una vez se cierre la app
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    fun openMenuMovimientos(){
        var intent = Intent(this, Movimientos::class.java)
        startActivity(intent)
    }
    fun openControlParental(){
        var intent = Intent(this, Parental_Control_Activity::class.java)
        startActivity(intent)
    }
    fun openAhorros(){
        var intent = Intent(this, AhorroActivity::class.java)
        startActivity(intent)
    }
    fun openPerfil(){
        var intent = Intent(this, Profile_Activity::class.java)
        startActivity(intent)
    }

}