package com.eb.todo_01.model

import java.util.*

data class ToDoModel(
    var id: Int = getAutoId(),
    var status: Int = 0,
    var task: String = ""
) {
    companion object {
        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(100)
        }
    }
}