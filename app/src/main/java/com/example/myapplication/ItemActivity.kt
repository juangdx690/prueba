package com.example.myapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.LayoutItemBinding
import com.example.myapplication.databinding.MainActivityBinding
import kotlin.math.roundToInt

class ItemActivity : AppCompatActivity() {

    lateinit var binding: LayoutItemBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cogerDatos()
    }

    fun cogerDatos() {
        val datos = intent.extras
        if (datos != null) {

            binding.etTitulo.text = datos.getString("TITULO")
            binding.etCategoria.text = datos.getString("CATEGORIA")
            binding.etVisto.text = datos.getString("VISTO")

            binding.etEstrellas.rating = datos.getFloat("ESTRELLAS")

            binding.etDescripcion.text = datos.getString("DESCRIPCION")

            val byteArray = datos.getByteArray("IMAGEN") // tu array de bytes
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
            binding.etImg.setImageBitmap(bitmap)

            if (binding.etVisto.text.equals("Visto")) {

                binding.etVisto.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
            if (binding.etVisto.text.equals("No visto")) {

                binding.etVisto.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
            if (binding.etVisto.text.equals("Viendolo")) {

                binding.etVisto.setTextColor(ContextCompat.getColor(this, R.color.naranjaBrillante))
            }


        }


    }


}
