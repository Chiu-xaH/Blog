package org.chiuxah.blog.ui.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import org.chiuxah.blog.MyApplication

fun myToast(text : String) {
    Handler(Looper.getMainLooper()).post{ Toast.makeText(MyApplication.context,text,Toast.LENGTH_SHORT).show() }
}