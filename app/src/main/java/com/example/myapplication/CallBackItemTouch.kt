package com.example.myapplication

import androidx.recyclerview.widget.RecyclerView

interface CallBackItemTouch {

    fun itemTouchOnMode(oldPosition :Int , newPosition:Int){
    }


    fun onSwipped(viewHolder: RecyclerView.ViewHolder, position:Int, ){}
}