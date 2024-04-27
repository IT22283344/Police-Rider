package com.example.police

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity(),Gametask {
    lateinit var rootLayout :LinearLayout
    lateinit var header :LinearLayout
    lateinit var  startbtn :ImageButton
    lateinit var mGameview : Gameview
    lateinit var score: TextView
    lateinit var Highscore: TextView
    lateinit var restartbtn: ImageButton
    lateinit var homebtn: ImageButton
    lateinit var closebtn: ImageButton

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startbtn = findViewById(R.id.startbtn)
        restartbtn = findViewById(R.id.restartbtn)
        homebtn = findViewById(R.id.homebtn)
        closebtn = findViewById(R.id.closebtn)
        rootLayout = findViewById(R.id.rootLayout)
        header = findViewById(R.id.header)
        score = findViewById(R.id.score)
        Highscore = findViewById(R.id.highscore)
        mGameview = Gameview(this, this)

        sharedPreferences = getSharedPreferences("HighScore", Context.MODE_PRIVATE)


        startbtn.setOnClickListener{
            mGameview.setBackgroundResource(R.drawable.roadhighway)
            rootLayout.addView(mGameview)
            startbtn.visibility = View.GONE
            restartbtn.visibility = View.GONE
            score.visibility = View.GONE
            Highscore.visibility =View.GONE
            header.visibility =View.GONE
        }

        restartbtn.setOnClickListener {
            rootLayout.removeView(mGameview)
            mGameview = Gameview(this, this)
            rootLayout.addView(mGameview)
            mGameview.setBackgroundResource(R.drawable.roadhighway)
            score.text = "Score : 0"
            startbtn.visibility = View.GONE
            score.visibility = View.GONE
            restartbtn.visibility = View.GONE
            Highscore.visibility =View.GONE
            header.visibility =View.GONE
        }

        homebtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        closebtn.setOnClickListener {
            finishAffinity()
        }


        val highScore = sharedPreferences.getInt("highScore", 0)
        Highscore.text = "High Score: $highScore"


    }

    override fun closeGame(mScore: Int){
        score.text = "Score : $mScore"
        rootLayout.removeView(mGameview)
        score.visibility = View.VISIBLE
        restartbtn.visibility = View.VISIBLE

        val highScore = sharedPreferences.getInt("highScore", 0)
        if (mScore > highScore) {
            // Save new high score
            with(sharedPreferences.edit()) {
                putInt("highScore", mScore)
                apply()
            }
            Highscore.text = "High Score : $mScore"
        }
        Highscore.visibility =View.VISIBLE
        header.visibility =View.VISIBLE
    }
}

