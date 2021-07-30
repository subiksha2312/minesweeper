package com.example.minesweeper

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EasyMode : AppCompatActivity() {

    private lateinit var gameBoard: GameBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_easy)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        gameBoard = GameBoard(this.applicationContext)

    }


}