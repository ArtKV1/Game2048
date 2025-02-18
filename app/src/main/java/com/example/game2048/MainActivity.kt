package com.example.game2048

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainscene)

        val button: Button = findViewById(R.id.my_button)

        button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Нажата кнопка: ${button.text}", Toast.LENGTH_SHORT).show()
        }

        val button2: Button = findViewById(R.id.my_button45)

        button2.setOnClickListener {
            val intent = Intent(this, TopPlayersActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Нажата кнопка: ${button2.text}", Toast.LENGTH_SHORT).show()
        }
    }
}