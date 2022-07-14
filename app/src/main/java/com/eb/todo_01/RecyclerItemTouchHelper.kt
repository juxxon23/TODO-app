package com.eb.todo_01

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.eb.todo_01.adapter.ToDoAdapter

class RecyclerItemTouchHelper(adapter: ToDoAdapter, dragDirs: Int = 0, swipeDirs: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT):
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private val adp = adapter

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        print("val context: " + adp.activity1)
        val position: Int = viewHolder.bindingAdapterPosition
        if(direction == ItemTouchHelper.LEFT) {
            val builder = AlertDialog.Builder(adp.activity1)
            builder.setTitle("Delete Task")
            builder.setMessage("Are you sure you want to delete this task?")
            builder.setPositiveButton("Confirm", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    adp.deleteItem(position)
                }
            })
            builder.setNegativeButton(android.R.string.cancel, object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    adp.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                }
            })
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            adp.editItem(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val icon: Drawable?
        val background: ColorDrawable
        val itemView: View = viewHolder.itemView
        val backgroundCornerOffset = 20

        if (dX > 0) {
            icon = ContextCompat.getDrawable(adp.activity1, R.drawable.ic_baseline_edit)
            background = ColorDrawable(ContextCompat.getColor(adp.activity1, R.color.teal_200))
        } else {
            icon = ContextCompat.getDrawable(adp.activity1, R.drawable.ic_baseline_delete_24)
            background = ColorDrawable(Color.RED)
        }
        val icHeight = icon?.intrinsicHeight
        val iconMargin = (itemView.height - icHeight!!)/2
        val iconTop = itemView.top + (itemView.height - icHeight!!)/2
        val iconBottom = iconTop + icHeight

        if (dX > 0) {
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icHeight
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(itemView.left, itemView.top,
                (itemView.left + dX + backgroundCornerOffset).toInt(), itemView.bottom)
        } else if (dX < 0) {
            val iconLeft = itemView.right - iconMargin - icHeight
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(
                (itemView.right + dX - backgroundCornerOffset).toInt(), itemView.top,
                itemView.right, itemView.bottom)
        } else {
            background.setBounds(0,0,0,0)
        }
        background.draw(c)
        icon.draw(c)
    }
}