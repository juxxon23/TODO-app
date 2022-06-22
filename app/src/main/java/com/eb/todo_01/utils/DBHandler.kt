package com.eb.todo_01.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.eb.todo_01.model.ToDoModel
import java.lang.Exception

class DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "todo.db"
        private const val TBL_TODO = "todo"
        private const val ID = "id"
        private const val TASK = "task"
        private const val STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblTodo = ("CREATE TABLE " + TBL_TODO + "("
                + ID + " INTEGER PRIMARY KEY, " + TASK + " TEXT, "
                + STATUS + " INTEGER);")
        db?.execSQL(createTblTodo)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Drop older tables
        db!!.execSQL("DROP TABLE IF EXISTS " + TBL_TODO)
        //Create tables again
        onCreate(db)
    }

    fun openDatabase(){
        val db = this.writableDatabase
    }

    fun insertTask (task: ToDoModel): Long {
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(ID, task.id)
        cv.put(TASK, task.task)
        cv.put(STATUS, task.status)

        val success = db.insert(TBL_TODO, null, cv)
        db.close()

        return success
    }

    @SuppressLint("Range")
    fun getAllTasks(): MutableList<ToDoModel> {
        val taskList: MutableList<ToDoModel> = ArrayList()
        val selectQuery = "SELECT * FROM " + TBL_TODO
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var task: String
        var status: Int
        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(ID))
                task = cursor.getString(cursor.getColumnIndex(TASK))
                status = cursor.getInt(cursor.getColumnIndex(STATUS))

                val it = ToDoModel(id, status, task)
                taskList.add(it)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return taskList
    }

    fun updateStatus(id: Int, status: Int): Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(STATUS, status)
        val success = db.update(TBL_TODO, cv, "$ID=?", arrayOf(id.toString())).toLong()
        db.close()
        return success
    }

    fun updateTask(id: Int, task: String): Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(TASK, task)
        val success = db.update(TBL_TODO, cv, "$ID=?", arrayOf(id.toString())).toLong()
        db.close()
        return success
    }

    fun deleteTask(id: Int): Long {
        val db = this.writableDatabase
        val success = db.delete(TBL_TODO, "$ID=?", arrayOf(id.toString())).toLong()
        db.close()
        return success
    }

}