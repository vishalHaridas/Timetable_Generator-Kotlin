package com.threeonefour.timetablegenerator

class Model {

    var isSelected: Boolean = false
    var subName: String? = null

    fun getsubName(): String {
        return this!!.subName.toString()
    }

    fun setsubName(subName: String) {
        this.subName = subName
    }

    fun getSelects(): Boolean {
        return isSelected
    }

    fun setSelects(selected: Boolean) {
        isSelected = selected
    }

}