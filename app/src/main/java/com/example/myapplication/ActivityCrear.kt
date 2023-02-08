package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityAddUpdateBinding
import com.example.myapplication.databinding.MainActivityBinding
import java.io.ByteArrayOutputStream

class ActivityCrear : AppCompatActivity() {

    var titulo = ""
    var descripcion = ""
    var categoria = ""
    var estrellas = 0.0f
    var img = null
    var id: Int? = null
    var visto = ""
    var editar = false

    lateinit var binding: ActivityAddUpdateBinding
    lateinit var conexion: BaseDatosAnimeManga
    lateinit var miAdapter: AnimesAdapter
    var lista = mutableListOf<Animes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        conexion = BaseDatosAnimeManga(this)


        binding.imageView.setImageResource(R.drawable.nophoto)

        cogerDatos()
        setListeners()
    }


    private fun setListeners() {
        binding.imageView.setOnClickListener {

            openGallery()
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnCrear.setOnClickListener {
            crearRegistro()
        }
    }

    private fun cogerDatos() {
        val datos = intent.extras
        if (datos != null) {
            editar = true
            binding.btnCrear.text = "EDITAR"
            val anime = datos.getSerializable("ANIMES") as Animes
            id = anime.id
            binding.etTitulo.setText(anime.titulo)
            binding.etCategoria.setText(anime.categoria)
            binding.etDescripcion.setText(anime.descripcion)

            if (anime.visto.equals("Visto")) {

                binding.cbVisto.isChecked = true

            }

            if (anime.visto.equals("Viendolo")) {

                binding.cbViendolo.isChecked = true

            }

            if (anime.visto.equals("No visto")) {

                binding.cbNovisto.isChecked = true

            }

            binding.etStar.rating = anime.estrellas

            val byteArray = anime.imagen // tu array de bytes
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.imageView.setImageBitmap(bitmap)


        }
    }

    private fun crearRegistro() {

        titulo = binding.etTitulo.text.toString().trim()
        descripcion = binding.etDescripcion.text.toString().trim()
        categoria = binding.etCategoria.text.toString().trim()
        estrellas = binding.etStar.rating

        if (binding.cbVisto.isChecked) {
            visto = "Visto"

        }
        if (binding.cbNovisto.isChecked) {
            visto = "No visto"

        }
        if (binding.cbViendolo.isChecked) {
            visto = "Viendolo"

        }

        val imageView: ImageView = binding.imageView
        if (imageView.drawable != null) {
            val drawable = imageView.getDrawable()
            val bitmap = (drawable as BitmapDrawable).bitmap
            // obtener la imagen como un Bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()


            if (!editar){
                val anime = Animes(1, titulo, descripcion, categoria, estrellas, byteArray, visto)
                if (conexion.crear(anime) > -1) {
                    finish()
                } else {
                    Toast.makeText(this, "No se pudo guardar el registro!!!", Toast.LENGTH_SHORT).show()
                }

            }else{
                val anime = Animes(1, titulo, descripcion, categoria, estrellas, byteArray, visto)
                if (conexion.update(anime) > -1) {
                    finish()
                } else {
                    Toast.makeText(this, "No se pudo editar el registro!!!", Toast.LENGTH_SHORT).show()
                }

            }

        } else {
            println("eror")
        }


    }

    companion object {

        const val REQUEST_CODE_GALLERY = 1
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            binding.imageView.setImageURI(imageUri)

        }
    }


}