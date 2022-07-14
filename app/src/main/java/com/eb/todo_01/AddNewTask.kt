package com.eb.todo_01

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.eb.todo_01.model.ToDoModel
import com.eb.todo_01.utils.DBHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask: BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ActionBottomDialog"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    private lateinit var newTaskText: EditText
    private lateinit var newTaskSaveButton: Button
    private lateinit var db: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.new_task, container, false)
        // SOFT_INPUT_ADJUST_RESIZE deprecated, but there is a way to implement
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newTaskText = view.findViewById<EditText>(R.id.tvTaskText)
        newTaskSaveButton = view.findViewById<Button>(R.id.btTask)

        db = DBHandler(requireContext())
        db.openDatabase()

        var isUpdate = false
        val bundle: Bundle? = arguments
        if (bundle != null){
            isUpdate = true
            val task = bundle.getString("task")
            newTaskText.setText(task)
            if(task != null && task.isNotEmpty()){
                newTaskSaveButton.setTextColor(Color.BLACK)
            }
        }
        newTaskText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() == ""){
                    newTaskSaveButton.isEnabled = false
                    newTaskSaveButton.setTextColor(Color.GRAY)
                } else {
                    newTaskSaveButton.isEnabled = true
                    newTaskSaveButton.setTextColor(Color.MAGENTA)
                }
            }



        })

        newTaskSaveButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val text = newTaskText.text.toString()
                if(isUpdate){
                    if (bundle != null) {
                        db.updateTask(bundle.getInt("id"), text)
                        dismiss()
                    }
                } else {
                    val newTask = ToDoModel(task = text)
                    db.insertTask(newTask)
                    dismiss()
                }
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val acty: FragmentActivity? = activity
        if (acty is DialogCloseListener) {
            (acty as? DialogCloseListener)?.handleDialogClose(dialog)
        }
    }
}