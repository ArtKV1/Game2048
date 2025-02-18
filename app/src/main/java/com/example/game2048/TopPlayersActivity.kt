package com.example.game2048

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TopPlayersActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topplayrsscene)

        val button: Button = findViewById(R.id.button33)

        button.setOnClickListener {
            finish()
            Toast.makeText(this, "Нажата кнопка: ${button.text}", Toast.LENGTH_SHORT).show()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            "Игрок 1 - 5555",
            "Игрок 2 - 666",
            "Игрок 3 - 888",
            "Игрок 4 - 888",
            "Игрок 5 - 4548",
            "Игрок 6 - 777")
        val adapter = TopPlayersAdapter(items)
        recyclerView.adapter = adapter
    }
}