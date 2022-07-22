package com.example.qissa.interfaces

interface OnObjectListInteractionListener<T> {

    fun onClick(position: Int, dataObject: T, value: Boolean)

    fun onLongClick(position: Int, dataObject: T)

    fun showEmptyView()

    fun hideEmptyView()
}
