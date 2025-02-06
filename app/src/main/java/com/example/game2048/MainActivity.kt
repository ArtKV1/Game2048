package com.example.game2048

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainscene)

        val button: Button = findViewById(R.id.my_button)
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayout)

        button.setOnClickListener {
            val intent = Intent(this, DifficultyScene::class.java)
            startActivity(intent)
        }
    }
}