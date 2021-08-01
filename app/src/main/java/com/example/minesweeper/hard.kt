package com.example.minesweeper

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class hard : AppCompatActivity() {
    private lateinit var gameBoard: GameBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hard)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        gameBoard = GameBoard(this.applicationContext)
        gameBoard.setMode("hard")

    }

    override fun onDestroy() {
        gameEnd = false
        super.onDestroy()

    }
}