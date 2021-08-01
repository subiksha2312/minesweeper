package com.example.minesweeper

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class intermediate : AppCompatActivity() {

    private lateinit var gameBoard: GameBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intermediate)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        gameBoard = GameBoard(this.applicationContext)
        gameBoard.setMode("intermediate")


    }


    override fun onDestroy() {
        gameEnd = false
        super.onDestroy()

    }
}