package com.example.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    val taskList = mutableListOf<String>()
    lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(taskList)
        recyclerView.adapter = taskAdapter

        val fab: FloatingActionButton = findViewById(R.id.addbutton)
        fab.setOnClickListener {
            showTaskDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun showTaskDialog() {
        val editText = EditText(this)
        editText.hint = "Enter your task"

        val dialog = AlertDialog.Builder(this)
            .setTitle("New Task")
            .setMessage("Enter the task name:")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val task = editText.text.toString().trim()
                if (task.isNotEmpty()) {
                    taskList.add(task)

                    taskAdapter.notifyItemInserted(taskList.size - 1)

                    Toast.makeText(this, "Task Added: $task", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

}
