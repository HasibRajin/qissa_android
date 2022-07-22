package com.example.qissa.interfaces

interface SecondaryObjectListener<T> {

    fun onSecObjClick(position: Int, dataObject: T)

    fun showSecObjEmptyView()

    fun hideSecObjEmptyView()
}
