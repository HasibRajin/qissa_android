package com.example.qissa.utils.extendFunctions

import android.app.Activity
import android.view.View
import android.widget.TextView

fun customAppBarAction(tvTitle: TextView, imgBack: View, title: String, activity: Activity, tvdescription: TextView, descption:String, ishow:Boolean) {
    tvTitle.text = title

    if(ishow){
        tvdescription.text = descption
        tvdescription.visibility = View.VISIBLE
    }

    imgBack.setOnClickListener {
        activity.onBackPressed()
    }
}