package com.example.anew.utils

import android.app.Activity
import android.app.AlertDialog

import com.example.recipefinder.R

class LoadingUtils(val activity:Activity) {

    lateinit var alertDialog: AlertDialog

    fun show(){

        val builder = AlertDialog.Builder(activity)


        //banako desing attch garn yo garney
        val designView = activity.layoutInflater.
        inflate(
            R.layout.loading,
            null)


        builder.setView(designView)
        builder.setCancelable(false)//app close garna namilney banaauna yo use garney
        alertDialog = builder.create()
        alertDialog.show()

    }

    fun dismiss(){
        alertDialog.dismiss()
    }
}