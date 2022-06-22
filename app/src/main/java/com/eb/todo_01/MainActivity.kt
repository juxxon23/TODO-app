package com.eb.todo_01

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eb.todo_01.adapter.ToDoAdapter
import com.eb.todo_01.model.ToDoModel
import com.eb.todo_01.utils.DBHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), DialogCloseListener {

    private val db = DBHandler(this)
    private lateinit var taskAdp: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        db.openDatabase()

        val fab = findViewById<FloatingActionButton>(R.id.fabCreateTask)
        fab.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
            }
        })
        initRecyclerView(db)
    }

    private fun initRecyclerView(db: DBHandler) {
        val taskRecyclerView = findViewById<RecyclerView>(R.id.rvTask)
        taskAdp = ToDoAdapter(db, this)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskRecyclerView.adapter = taskAdp

        //falta aplicar reverse a la lista
        var taskList: MutableList<ToDoModel> = db.getAllTasks()
        taskAdp.setTasks(taskList)

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(taskAdp))
        itemTouchHelper.attachToRecyclerView(taskRecyclerView)
    }

    override fun handleDialogClose(dialog: DialogInterface) {
        val taskList: MutableList<ToDoModel> = db.getAllTasks()
        taskAdp.setTasks(taskList)
        taskAdp.notifyDataSetChanged()
    }


}
