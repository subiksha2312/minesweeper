package com.example.minesweeper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import kotlin.random.Random


var gameEnd: Boolean =false

class GameBoard : View {

    var startx = 10f
    var starty = 10f
    private val GRIDSIZE = 8
    private val squareside: Float = 100f
    private var maxsize: Int =0


    private var tPaint: Paint = Paint()
    private var gridcolor: Paint = Paint()
    private var paintEndCard: Paint = Paint()
    private var paintreplay:Paint = Paint()
    private var paintreplaytext:Paint = Paint()
    private var paintminecounttext: Paint = Paint()

    private var minesarray = Array(GRIDSIZE) {IntArray(GRIDSIZE){0} }
    private var minecoord = ArrayList<RectF>()
    private var emptymines :MutableList<Rect> = ArrayList()
    private var minesarraylocate = Array(GRIDSIZE) {IntArray(GRIDSIZE){0} }

    var currentrow =0
    var currentcolumn = 0

    var mMode: String = ""
    lateinit var hspref: SharedPreferences
    lateinit var hsprefhard: SharedPreferences
    lateinit var hsprefinter: SharedPreferences

    var score = 0
    var highscore =0
    var highscorehard =0
    var highscoreinter =0
    var minecount = 0

    private lateinit var tvscore: TextView
    private lateinit var tvhighscoreeasy: TextView
    private lateinit var tvhighscoreinter: TextView
    private lateinit var tvhighscorehard: TextView


    constructor(context: Context) : super(context) {
        //Log.d("random0.01","constructor1")
        //init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        //Log.d("random0.02","constructor2")
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        //Log.d("random0.03","constructor3")
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {


        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.GameBoard, 0, 0)
        mMode = attributeArray.getString(R.styleable.GameBoard_gameMode) ?: "easy"
        Log.d("mode1","$mMode")

        tPaint.color = Color.BLACK
        gridcolor.color = Color.WHITE
        paintEndCard.color = Color.DKGRAY
        paintreplay.color = Color.BLACK
        paintreplaytext.color = Color.WHITE
        paintminecounttext.color = Color.BLACK

        paintreplaytext.textSize = 50F
        paintminecounttext.textSize = 50f


        paintreplaytext.textAlign = Paint.Align.CENTER
        paintreplay.textAlign = Paint.Align.CENTER

        declaringmines()
        computeminecoord()

        if(mMode=="hard") {
            hsprefhard =
                context.getSharedPreferences((R.string.hardHighScoreKey).toString(), Context.MODE_PRIVATE)
            if (hsprefhard.contains((R.string.hardHighScore).toString()) == false) {
                with(hsprefhard.edit()) {
                    putInt((R.string.hardHighScore).toString(), 0)
                    apply()
                    commit()
                }
            }

            highscorehard = hsprefhard.getInt((R.string.hardHighScore).toString(), 0)

        }

        if(mMode=="intermediate") {
            hsprefinter =
                context.getSharedPreferences((R.string.interHighScoreKey).toString(), Context.MODE_PRIVATE)
            if (hsprefinter.contains((R.string.interHighScore).toString()) == false) {
                with(hsprefinter.edit()) {
                    putInt((R.string.interHighScore).toString(), 0)
                    apply()
                    commit()
                }
            }

            highscoreinter = hsprefinter.getInt((R.string.interHighScore).toString(), 0)

        }

        else {
            hspref =
                context.getSharedPreferences(
                    (R.string.easyHighScoreKey).toString(),
                    Context.MODE_PRIVATE
                )
            if (hspref.contains((R.string.easyHighScore).toString()) == false) {
                with(hspref.edit()) {
                    putInt((R.string.easyHighScore).toString(), 0)
                    apply()
                    commit()
                }
            }

            highscore = hspref.getInt((R.string.easyHighScore).toString(), 0)
        }

        attributeArray.recycle()

    }



    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        tvscore = (parent as View).findViewById(R.id.score) as TextView
        tvscore.setText("$score")

        Log.d("mode2","$mMode")


        if(mMode =="hard") {
            tvhighscorehard = (parent as View).findViewById(R.id.highscorehard) as TextView
            tvhighscorehard.setText("High Score is: $highscorehard")
        }

        else if(mMode=="intermediate") {
            tvhighscoreinter = (parent as View).findViewById(R.id.highscoreinter) as TextView
            tvhighscoreinter.setText("High Score is:$highscoreinter")
        }

        else {
            tvhighscoreeasy =  (parent as View).findViewById(R.id.highscore) as TextView
            tvhighscoreeasy.setText("High Score is:$highscore")
        }

    }

    fun numberofmines() {
        if (mMode=="easy") {
            maxsize = 5
        }
        else if (mMode=="intermediate") {
            maxsize = 7
        }
        else {
            maxsize = 10
        }
    }

    fun declaringmines() {

        //Log.d("random0","entering declaring mines")

        minesarray = Array(GRIDSIZE) { IntArray(GRIDSIZE) { 0 } }
        var rowarray = arrayOf(0, 1, 2, 3, 4, 5, 6, 7)

        var minestringarray = Array(2) { IntArray(5) { 0 } }
        var minestringarray2: MutableList<IntArray> = ArrayList()

        numberofmines()

        for (i in 0 until maxsize) {
            var temp = IntArray(2)
            temp[0] = rowarray.random()
            temp[1] = rowarray.random()
            minestringarray2.add(temp)
            minesarray[temp[0]][temp[1]] = 1

        }
        for (i in 0..minestringarray2.size - 1) {
            Log.d("minesarray1", "${minestringarray2.get(i).toList()}")

        }
        for (i in 0 until maxsize) {
            for (j in i + 1 until maxsize) {
                if (minestringarray2[i].contentEquals(minestringarray2[j])) {
                    var temp = IntArray(2)
                    minestringarray2.removeAt(j)
                    temp[0] = rowarray.random()
                    temp[1] = rowarray.random()
                    minestringarray2.add(temp)
                    minesarray[temp[0]][temp[1]] = 1
                }
            }
        }

        for (i in 0..minestringarray2.size - 1) {
            Log.d("minesarray2", "${minestringarray2.get(i).toList()}")

        }

        minestringarray2.clear()

            /*
        minesarray[2][3] = 1
        minesarray[1][2] = 1
        minesarray[5][3] = 1
        minesarray[1][7] = 1
        minesarray[6][4] = 1

         */


    }

    fun setMode(mode: String) {
        mMode = mode
    }


    fun computeminecoord() {

        for (i in 0..GRIDSIZE-1) {
            for (j in 0..GRIDSIZE-1) {
                if (minesarray[i][j] == 1) {

                    var minerecord: RectF = RectF(0f,0f,0f,0f)

                    minerecord.left = startx + i * squareside
                    minerecord.top = starty + j * squareside
                    minerecord.right = minerecord.left + squareside
                    minerecord.bottom = minerecord.top + squareside

                    minecoord.add(minerecord)
                }
            }
        }
    }

    fun locatemines(x: Float, y: Float): Boolean {
        for( i in 0 until minecoord.size) {
            if(minecoord[i].contains(x,y)) {
                return true
            }

        }
       return false
    }






    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

                if (locatemines(event.x, event.y) == true) {
                    resetgame()
                }
               else {

                      if (checkiftouchedbefore(event.x, event.y) == false) {
                           score++
                           tvscore.setText("Your score is = $score")
                           postInvalidate()
                       }

                }

                if(checkPointInReplayRect(event.x , event.y)== true) {
                    gameEnd = false
                    Log.d("random0.2","calling from ontouch")
                    declaringmines()
                    computeminecoord()
                    invalidate()

                    Log.d("mode3","$mMode")
                    if(mMode =="hard") {
                        tvhighscorehard = (parent as View).findViewById(R.id.highscorehard) as TextView
                        tvhighscorehard.setText("High Score is: $highscorehard")
                    }

                    else if(mMode=="intermediate") {
                        tvhighscoreinter = (parent as View).findViewById(R.id.highscoreinter) as TextView
                        tvhighscoreinter.setText("High Score is:$highscoreinter")
                    }

                    else {
                        tvhighscoreeasy =  (parent as View).findViewById(R.id.highscore) as TextView
                        tvhighscoreeasy.setText("High Score is:$highscore")
                    }

                    score = 0
                    tvscore.setText("$score")
                    emptymines.clear()

                }


            }
        }
        return false
    }

    fun checkiftouchedbefore(x:Float, y:Float) : Boolean {

        var mColorRect =Rect(0,0,0,0)

        var currow =0
        var currcol=0

        mColorRect.left = (( x / 100).toInt()) *100 +(startx).toInt()
        mColorRect.top =  (( y / 100).toInt()) *100 +(starty).toInt()
        mColorRect.bottom = mColorRect.top + 100
        mColorRect.right = mColorRect.left +100

        currow = (mColorRect.left) / 100
        currcol = (mColorRect.top) / 100

        if (minesarray[currow][currcol] == 2 ) {
            return true
        }
        else {
            emptymines.add(mColorRect)
            minesarray[currow][currcol] = 2
            locatingsurroundingmines(x,y)
        }

        return false
    }


    fun resetgame() {

        if(mMode=="hard") {
            if (score > highscorehard) {
                highscorehard = score
                with(hsprefhard.edit()) {
                    putInt((R.string.hardHighScore).toString(), highscorehard)
                    apply()
                }

            }
        }

        if(mMode == "intermediate") {
            if (score > highscoreinter) {
                highscoreinter = score
                with(hsprefinter.edit()) {
                    putInt((R.string.interHighScore).toString(), highscoreinter)
                    apply()
                }

            }
        }

        else {
            if (score > highscore) {
                highscore = score
                with(hspref.edit()) {
                    putInt((R.string.easyHighScore).toString(), highscore)
                    apply()
                }

            }
        }

        score = 0
        tvscore.setText("Your score is = $score")
        gameEnd = true
        emptymines.clear()
        var v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            v.vibrate(500)
        }
        invalidate()
    }

    fun checkPointInReplayRect(x: Float, y: Float): Boolean {
        if (x > ((width / 2)-200f) &&
            x < ((width / 2) + 150).toFloat() &&
            y > ((height / 3) + 200).toFloat() &&
            y < ((height / 3) + 225).toFloat()
        ) {

            return true
        }
        return false
    }

    fun locatingsurroundingmines(x:Float, y:Float) {

        minecount = 0

        var mColorRect =Rect(0,0,0,0)


        mColorRect.left = (( x / 100).toInt()) *100 +(startx).toInt()
        mColorRect.top =  (( y / 100).toInt()) *100 +(starty).toInt()
        mColorRect.bottom = mColorRect.top + 100
        mColorRect.right = mColorRect.left +100

        currentrow = (mColorRect.left) / 100
        currentcolumn = (mColorRect.top) / 100

        minesarray[currentrow][currentcolumn]

        if(currentrow in 1..6 && currentcolumn==0 ) {
            if(minesarray[currentrow-1][currentcolumn] ==1) {
                minecount++
            }
            if(minesarray[currentrow-1][currentcolumn+1] ==1) {
                minecount++
            }
            if(minesarray[currentrow][currentcolumn+1] ==1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn] ==1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn+1] ==1) {
                minecount++
            }
        }

        else if(currentrow==7 && currentcolumn in 1..6) {
            if(minesarray[currentrow][currentcolumn-1] ==1) {
                minecount++
            }
            if(minesarray[currentrow-1][currentcolumn-1] ==1) {
                minecount++
            }
            if(minesarray[currentrow-1][currentcolumn] ==1) {
                minecount++
            }
            if(minesarray[currentrow][currentcolumn+1] ==1) {
                minecount++
            }
            if(minesarray[currentrow-1][currentcolumn+1] ==1) {
                minecount++
            }
        }

        else if(currentrow in 1..6 && currentcolumn ==7) {
            if(minesarray[currentrow][currentcolumn-1] ==1) {
                minecount++
            }
            if(minesarray[currentrow-1][currentcolumn] ==1) {
                minecount++
            }
            if(minesarray[currentrow-1][currentcolumn-1] ==1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn-1] ==1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn] ==1) {
                minecount++
            }
        }

        else if(currentrow ==0 && currentcolumn in 1..6) {
            if(minesarray[currentrow][currentcolumn-1] ==1) {
                minecount++
            }
            if(minesarray[currentrow][currentcolumn+1] ==1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn-1] ==1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn] ==1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn+1] ==1) {
                minecount++
            }
        }

        else if(currentrow==0 && currentcolumn==0) {
            if(minesarray[currentrow+1][currentcolumn+1] == 1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn] == 1) {
                minecount++
            }
            if (minesarray[currentrow][currentcolumn+1] == 1) {
                minecount++

            }
        }

        else if(currentrow==7 && currentcolumn==0) {
            if(minesarray[currentrow-1][currentcolumn] == 1) {
                minecount++
            }
            if(minesarray[currentrow][currentcolumn+1] == 1) {
                minecount++
            }
            if (minesarray[currentrow-1][currentcolumn+1] == 1) {
                minecount++

            }
        }

        else if(currentrow==7 && currentcolumn==7) {
            if(minesarray[currentrow][currentcolumn-1] == 1) {
                minecount++
            }
            if(minesarray[currentrow-1][currentcolumn-1] == 1) {
                minecount++
            }
            if (minesarray[currentrow-1][currentcolumn] == 1) {
                minecount++

            }
        }

        else if(currentrow==0 && currentcolumn==7) {
            if(minesarray[currentrow][currentcolumn-1] == 1) {
                minecount++
            }
            if(minesarray[currentrow+1][currentcolumn-1] == 1) {
                minecount++
            }
            if (minesarray[currentrow+1][currentcolumn] == 1) {
                minecount++

            }
        }

        else {
            if(minesarray[currentrow][currentcolumn-1] == 1) {
                minecount++
            }
            if(minesarray[currentrow][currentcolumn+1] == 1) {
                minecount++
            }
            if (minesarray[currentrow-1][currentcolumn] == 1) {
                minecount++

            }
            if(minesarray[currentrow-1][currentcolumn+1] == 1) {
                minecount++
            }
            if(minesarray[currentrow-1][currentcolumn-1] == 1) {
                minecount++
            }
            if (minesarray[currentrow+1][currentcolumn] == 1) {
                minecount++

            }
            if(minesarray[currentrow+1][currentcolumn+1] == 1) {
                minecount++
            }
            if (minesarray[currentrow+1][currentcolumn-1] == 1) {
                minecount++

            }

        }

        minesarraylocate[currentrow][currentcolumn] = minecount

    }

    fun translatecoordinates(currentrect:Rect) : IntArray {

        var translatedcoord: IntArray = IntArray(2)
        translatedcoord[0] = (currentrect.left) / 100
        translatedcoord[1] = (currentrect.top) / 100

        return translatedcoord
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas?.drawRect(0f,0f,width.toFloat(),height.toFloat(),tPaint)

        for(i in 0..GRIDSIZE){
            canvas?.drawLine(startx + (i*squareside),starty,startx+(i*squareside),starty + (8* squareside),gridcolor)
        }

        for(i in 0..GRIDSIZE){
            canvas?.drawLine(starty, startx+(i*squareside),starty + (8* squareside),starty+(i*squareside),gridcolor)
        }



        if (emptymines.size != 0) {
            for (i in 0 until emptymines.size) {
                canvas?.drawRect(
                    emptymines[i],
                    gridcolor
                )

                var displaycoordinate: IntArray = translatecoordinates(emptymines[i])
                Log.d("okayfine1","${displaycoordinate[0]},${displaycoordinate[1]}")
                Log.d("okayfine2","${minesarraylocate[displaycoordinate[0]][displaycoordinate[1]]}")
                canvas?.drawText(
                    "${minesarraylocate[displaycoordinate[0]][displaycoordinate[1]]}",
                    (emptymines[i].left + emptymines[i].width()/2).toFloat(),
                    (emptymines[i].top + emptymines[i].height()/2).toFloat(),
                    paintminecounttext

                )
            }

        }

        if (gameEnd == true) {

            canvas?.drawRect(
                0f,
                (height / 3).toFloat(),
                width.toFloat(),
                ((height / 3) + 300).toFloat(),
                paintEndCard
            )

            canvas?.drawRect(
                ((width / 2) - 200f).toFloat(),
                ((height / 3) + 150).toFloat(),
                ((width / 2) + 200).toFloat(),
                ((height / 3) + 225).toFloat(),
                paintreplay
            )

            canvas?.drawText(
                "Replay!",
                ((width / 2) + 10).toFloat(),
                ((height / 3) + 200).toFloat(),
                paintreplaytext
            )
        }
    }

}

private fun <E> ArrayList<E>.add(element: Array<IntArray>) {

}







