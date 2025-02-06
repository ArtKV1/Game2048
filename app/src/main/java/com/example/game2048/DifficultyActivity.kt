package com.example.game2048

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout

class DifficultyScene : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.difficultyscene)

        val button: Button = findViewById(R.id.my_button)
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayout)

        button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}