package com.example.game2048

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout

class DifficultyScene : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.difficultyscene)

        val button: Button = findViewById(R.id.my_button)
        val button2: Button = findViewById(R.id.my_button2)

        button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            Toast.makeText(this, "Нажата кнопка: ${button.text}", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        button2.setOnClickListener {
            Toast.makeText(this, "Нажата кнопка: ${button2.text}", Toast.LENGTH_SHORT).show()
        }
    }
}