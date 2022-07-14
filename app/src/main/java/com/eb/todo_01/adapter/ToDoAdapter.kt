package com.eb.todo_01.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.eb.todo_01.AddNewTask
import com.eb.todo_01.MainActivity
import com.eb.todo_01.R
import com.eb.todo_01.model.ToDoModel
import com.eb.todo_01.utils.DBHandler

class ToDoAdapter(private val db: DBHandler, activity: MainActivity) : RecyclerView.Adapter<ToDoAdapter.ToDoHolder>() {

    private lateinit var toDoList: MutableList<ToDoModel>
    private val db1 = db
    public val activity1 = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ToDoHolder(inflater.inflate(R.layout.task_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ToDoHolder, position: Int) {
        holder.bind(toDoList[position], db1)
    }

    override fun getItemCount(): Int = toDoList.size

    fun setTasks(todoList: MutableList<ToDoModel>){
        toDoList = todoList
        notifyDataSetChanged()
    }

    fun editItem(position: Int) {
        val item = toDoList[position]
        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("task", item.task)
        val fragment = AddNewTask()
        fragment.arguments = bundle
        fragment.show(activity1.supportFragmentManager, AddNewTask.TAG)
    }

    fun deleteItem(position: Int) {
        val item = toDoList[position]
        db.deleteTask(item.id)
        toDoList.removeAt(position)
        notifyItemRemoved(position)
    }

    class ToDoHolder(view: View): RecyclerView.ViewHolder(view) {

        private val task = view.findViewById<CheckBox>(R.id.cbTask)

        fun bind(itemTask: ToDoModel, db1: DBHandler){
            task.text = itemTask.task
            task.isChecked = Int.toBoolean(itemTask.status)
            task.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if(isChecked){
                        println(itemTask)
                        db1.updateStatus(itemTask.id, 1)
                    } else {
                        println(itemTask)
                        db1.updateStatus(itemTask.id, 0)
                    }
                }
            })
        }
    }
}

private fun Int.Companion.toBoolean(status: Int): Boolean {
    return status == 1
}