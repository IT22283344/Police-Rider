package com.example.police

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.lang.Math.abs
import java.util.ArrayList
import java.util.HashMap

class Gameview(var c:Context, var gametask: Gametask ):View(c)
{
    private  var myPaint:Paint? =null
    private  var speed = 1
    private  var time = 0
    private var score = 0
    private var mycarPosition = 0
    private var highScore = 0
    private val  otherCars =ArrayList<HashMap<String,Any>>()

    var viewWidth = 0
    var viewHeight = 0

    private var mediaPlayer: MediaPlayer? = null
    private var sharedPreferences: SharedPreferences? = null

    init {
        myPaint = Paint()
        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(c, R.raw.sound)

        // Set looping to true to make the sound clip repeat until stopped
        mediaPlayer?.isLooping = true

        sharedPreferences = c.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        // Retrieve high score from SharedPreferences
        highScore = sharedPreferences?.getInt("HighScore", 0) ?: 0


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth =this.measuredWidth
        viewHeight = this.measuredHeight

        if(time % 700 < 10 + speed){
            val map =HashMap<String,Any>()
            map["Lane"] = (0..2).random()
            map["startTime"] = time
            otherCars.add(map)
        }
        time = time + 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.policecar,null)

        d.setBounds(
            mycarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight-2 - carHeight,
            mycarPosition * viewWidth / 3 + viewWidth / 15 + 25 + carWidth - 25,
            viewHeight - 2

        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN


        for (i in otherCars.indices) {
            try {
                val lane = otherCars[i]["Lane"] as Int
                val carX = lane * (viewWidth / 3) + (viewWidth / 15)
                val startTime = otherCars[i]["startTime"] as Int
                val elapsedTime = time - startTime
                val carY = elapsedTime - (viewHeight + carHeight)  // Adjust car position based on time

                if (carY < viewHeight) {
                    val d2 = resources.getDrawable(R.drawable.lambogini, null)

                    d2.setBounds(
                        carX + 25, carY - carHeight, carX + carWidth - 25, carY
                    )
                    d2.draw(canvas)


                    if (lane == mycarPosition && carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                        gametask.closeGame(score)
                    }
                } else {
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + abs(score / 8)
                    if (score > highScore) {
                        highScore = score

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.bangers)
        myPaint?.typeface = typeface

        myPaint!!.color = Color.GREEN
        myPaint!!.textSize = 64f


        canvas.drawText("Score : $score",60f,80f,myPaint!!)
        canvas.drawText("Speed : $speed",320f,80f,myPaint!!)


        invalidate()

    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                val x1 = event.x
                if(x1 < viewWidth/2){
                    if(mycarPosition > 0){
                        mycarPosition--
                    }
                }
                if(x1 > viewWidth/ 2){
                    if(mycarPosition<2){
                        mycarPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mediaPlayer?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mediaPlayer?.release()
        mediaPlayer = null
    }



}
