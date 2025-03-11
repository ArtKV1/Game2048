package com.example.game2048

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Player(
    val id: Long,
    val name: String,
    val score: Int
)

class DbAdapter(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Удаляем старую таблицу и создаем новую
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertData(name: String?, score: Int): Long {
        val db = this.writableDatabase

        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_SCORE),
            "$COLUMN_NAME = ?",
            arrayOf(name),
            null, null, null
        )

        var result: Long = -1

        if (cursor.moveToFirst()) {
            val currentScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE))

            if (score > currentScore) {
                val values = ContentValues()
                values.put(COLUMN_SCORE, score)
                result = db.update(TABLE_NAME, values, "$COLUMN_NAME = ?", arrayOf(name)).toLong()
            } else {
                result = -1
            }
        } else {
            val values = ContentValues()
            values.put(COLUMN_NAME, name)
            values.put(COLUMN_SCORE, score)
            result = db.insert(TABLE_NAME, null, values)
        }

        cursor.close()
        return result
    }

    @SuppressLint("Range")
    fun getAllPlayers(): List<String> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        val players = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE))
                players.add(name + " - " + score)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return players
    }

    fun updateData(id: Int, name: String?, score: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_SCORE, score)
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", arrayOf(id.toString()))
    }

    fun deleteData(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", arrayOf(id.toString()))
    }

    companion object {
        private const val DATABASE_NAME = "game2048.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME: String = "game2048"
        const val COLUMN_ID: String = "id"
        const val COLUMN_NAME: String = "name"
        const val COLUMN_SCORE: String = "score"

        private const val SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_SCORE + " INTEGER)"
    }
}