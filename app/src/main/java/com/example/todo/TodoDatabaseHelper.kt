package com.example.todo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class TodoDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "TodoDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            "CREATE TABLE tasks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "isDone INTEGER DEFAULT 0)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun insertTask(title: String) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put("title", title)
            put("isDone", 0)
        }

        db.insert("tasks", null, values)
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM tasks", null)

        if (cursor.moveToFirst()) {
            do {
                val titleIndex = cursor.getColumnIndex("title")
                val isDoneIndex = cursor.getColumnIndex("isDone")

                if (titleIndex >= 0 && isDoneIndex >= 0) {
                    val title = cursor.getString(titleIndex)
                    val isDone = cursor.getInt(isDoneIndex) == 1
                    tasks.add(Task(title, isDone))
                } else {
                    Log.e("SQLiteError", "Column not found in cursor")
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return tasks
    }



    fun deleteTask(task: Task) {
        val db = writableDatabase
        db.delete("tasks", "title = ?", arrayOf(task.text))
        db.close()
    }

    fun updateTaskStatus(task: Task) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("isDone", if (task.isDone) 1 else 0)
        db.update("tasks", values, "title = ?", arrayOf(task.text))
        db.close()
    }


}
