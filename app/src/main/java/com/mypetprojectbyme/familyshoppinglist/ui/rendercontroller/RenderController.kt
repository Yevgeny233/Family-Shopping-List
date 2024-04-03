package com.mypetprojectbyme.familyshoppinglist.ui.rendercontroller

import android.content.res.Resources
import android.graphics.Paint
import android.widget.CheckBox
import android.widget.TextView
import com.mypetprojectbyme.familyshoppinglist.R

object RenderController {

    fun setClickedState(nameOfNote: TextView, checkboxNote: CheckBox, resources: Resources) {
        nameOfNote.paintFlags =
            checkboxNote.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        nameOfNote.setTextColor(resources.getColor(R.color.grey))
    }

    fun setUnClickedState(nameOfNote: TextView, checkboxNote: CheckBox, resources: Resources) {
        nameOfNote.paintFlags =
            nameOfNote.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        nameOfNote.setTextColor(resources.getColor(R.color.black))
    }
}