package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OpeningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening_activity)

        val button1=findViewById<Button>(R.id.button1) //finding the button to be clicked to start intent
        button1.setOnClickListener{
            val intent= Intent(this,EasyMode::class.java) //Creating an intent so that when the button is clicked,it moves onto another activity
            startActivity(intent)  //starting said intent
        }
        val button2=findViewById<Button>(R.id.button2)  //finding the button to be clicked to start intent
        button2.setOnClickListener{
            val intent= Intent(this,intermediate::class.java)  //Creating an intent so that when the button is clicked,it moves onto another activity
            startActivity(intent)  //starting said intent
        }
        val button3=findViewById<Button>(R.id.button3)  //finding the button to be clicked to start intent
        button3.setOnClickListener{
            val intent= Intent(this,hard::class.java)  //Creating an intent so that when the button is clicked,it moves onto another activity
            startActivity(intent)  //starting said intent
        }
    }
}