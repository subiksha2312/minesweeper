package com.example.minesweeper

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar


var touchingsquares = 0
var gameloss= 0
lateinit var soundPoolTouchingsquares : SoundPool
lateinit var soundPoolFailure : SoundPool

class OpeningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening_activity)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.hide()

        if (Build.VERSION.SDK_INT >= 21) {
            val audioAttrib = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val builder = SoundPool.Builder()
            builder.setAudioAttributes(audioAttrib).setMaxStreams(6)  //for audio effects

            soundPoolFailure = builder.build()
            soundPoolTouchingsquares = builder.build()

        } else {
            soundPoolTouchingsquares = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
            soundPoolFailure = SoundPool(6, AudioManager.STREAM_MUSIC, 0)

        }

        touchingsquares = soundPoolTouchingsquares.load(this, R.raw.touchingmine, 1)
        gameloss = soundPoolFailure.load(this, R.raw.gameloss, 1)

        val button1=findViewById<Button>(R.id.button1) //finding the button to be clicked to start intent
        button1.setOnClickListener{
            val intent= Intent(this,EasyMode::class.java) //Creating an intent so that when the button is clicked,it moves onto another activity
            startActivity(intent)  //starting said intent
        }
        val button2=findViewById<Button>(R.id.button2)
        button2.setOnClickListener{
            val intent= Intent(this,intermediate::class.java)
            startActivity(intent)  //starting said intent
        }
        val button3=findViewById<Button>(R.id.button3)
        button3.setOnClickListener{
            val intent= Intent(this,hard::class.java)
            startActivity(intent)  //starting said intent
        }
    }
}