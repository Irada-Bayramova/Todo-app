package com.example.todo

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context



class TaskAdapter(private val taskList: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTextView: TextView = itemView.findViewById(R.id.task_text)
        val checkBox: CheckBox = itemView.findViewById(R.id.task_checkbox)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.taskTextView.text = task.text
        holder.checkBox.isChecked = task.isDone



        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked

            val dbHelper = TodoDatabaseHelper(holder.itemView.context)
            dbHelper.updateTaskStatus(task)

            holder.taskTextView.paintFlags =
                if (isChecked) holder.taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else holder.taskTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }


        holder.deleteButton.setOnClickListener {
            val task = taskList[position]

            val dbHelper = TodoDatabaseHelper(holder.itemView.context)
            dbHelper.deleteTask(task)

            taskList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, taskList.size)
        }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
