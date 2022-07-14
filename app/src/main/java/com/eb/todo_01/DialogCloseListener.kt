package com.eb.todo_01

import android.content.DialogInterface

interface DialogCloseListener {
    public fun handleDialogClose(dialog: DialogInterface)
}