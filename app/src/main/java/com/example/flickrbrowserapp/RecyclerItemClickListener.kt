package com.example.flickrbrowserapp

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val listener: RecyclerItemClickListener)
    : RecyclerView.SimpleOnItemTouchListener() {

        private val TAG = "RecyclerItemClickListen"

    interface OnRecyclerClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG,".onInterceptTouchEvent: starts $e")
//        return super.onInterceptTouchEvent(rv, e)
        return true
    }
}