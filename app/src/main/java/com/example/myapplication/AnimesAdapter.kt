package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AnimesAdapter(
    private var lista: MutableList<Animes>,
    private val onItemDelete:(Int)->Unit,
    private val onItemUpdate:(Animes) -> Unit
) : RecyclerView.Adapter<AnimesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimesViewHolder { //2.
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_manus, parent, false)
        return AnimesViewHolder(v)
    }

    fun setList(lista2:MutableList<Animes>){
        this.lista = lista2
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AnimesViewHolder, position: Int) {
        holder.inflar(lista[position], onItemDelete, onItemUpdate)
    }

    override fun getItemCount() = lista.size  //1

    fun deleteItem(i: Int) {

        lista.removeAt(i)
        notifyItemRemoved(i)

    }


}
