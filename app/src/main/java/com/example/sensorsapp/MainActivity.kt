package com.example.sensorsapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.widget.Toast
import androidx.core.content.getSystemService

class MainActivity : AppCompatActivity(),SensorEventListener {

    var sensor:Sensor? = null
    var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT) // for light detection app
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

//    var isRunning = false // for light detection app

    // for nimbuzz shock detection app
    var xold = 0.0
    var yold = 0.0
    var zold = 0.0
    var threshold = 3000.0
    var oldtime:Long = 0

    override fun onSensorChanged(event: SensorEvent?) {
        // for nimbuzz shock detection shaking app
        var x = event!!.values[0]
        var y = event!!.values[1]
        var z = event!!.values[2]
        var currentTime = System.currentTimeMillis()
        if((currentTime-oldtime)>100){
            var timeDiff:Long = currentTime-oldtime
            oldtime = currentTime
            var speed = Math.abs(x+y+z-xold-yold-zold)/timeDiff*10000
            if(speed>threshold){
                var vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vib.vibrate(500)
                Toast.makeText(applicationContext,"Shock Detected",Toast.LENGTH_LONG).show()
            }
        }


        // for light detection app
//        if(event!!.values[0]>30 && isRunning==false){
//            //play music
//            isRunning=true
//            try {
//                var mp = MediaPlayer()
//                mp.setDataSource("http://server6.mp3quran.net/thubti/001.mp3")
//                mp.prepare()
//                mp.start()
//            }
//            catch (ex:java.lang.Exception){ }
//        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}