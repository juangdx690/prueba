package com.example.myapplication


import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.RowManusBinding

class AnimesViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
    //  private val miBinding=UsuariosLayoutBinding.bind(vista)
    private val miBinding = RowManusBinding.bind(vista)
    fun inflar(
        anime: Animes,
        onItemDelete:(Int)->Unit,
        onItemUpdate:(Animes)->Unit)
     {

         miBinding.btnBorrar.setOnClickListener{
             onItemDelete(adapterPosition)
         }

         miBinding.btnEditar.setOnClickListener{
             onItemUpdate(anime)
         }

         itemView.setOnClickListener{

             val intent = Intent(itemView.context, ItemActivity::class.java)
             intent.putExtra("TITULO", anime.titulo)
             intent.putExtra("CATEGORIA", anime.categoria)
             intent.putExtra("VISTO", anime.visto)
             intent.putExtra("ESTRELLAS", anime.estrellas)
             intent.putExtra("DESCRIPCION", anime.descripcion)
             intent.putExtra("IMAGEN", anime.imagen)
             ContextCompat.startActivity(itemView.context, intent, null)

         }

        if (anime.titulo.length < 25){

            miBinding.tvTitle.text = anime.titulo

        }else{

            miBinding.tvTitle.text = anime.titulo.take(23)+" ..."
            miBinding.tvTitle.maxLines = 2

        }

        miBinding.tvCategory.text = anime.categoria
        val descripcion = anime.descripcion
        val descripcionLimited = descripcion.split(" ").take(12).joinToString(" ")
        miBinding.tvDescription.text = descripcionLimited+" ..."
        miBinding.tvNumEstrellas.text = anime.estrellas.toString()
        miBinding.tvStars.rating = anime.estrellas
        val byteArray = anime.imagen // tu array de bytes
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        miBinding.ivMain.setImageBitmap(bitmap)
        miBinding.tvVisto.text = anime.visto

        if (anime.visto.equals("Visto")){

            miBinding.tvVisto.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
        }
        if (anime.visto.equals("No visto")){

            miBinding.tvVisto.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
        }
        if (anime.visto.equals("Viendolo")){

            miBinding.tvVisto.setTextColor(ContextCompat.getColor(itemView.context, R.color.naranjaBrillante))
        }


    }



}
