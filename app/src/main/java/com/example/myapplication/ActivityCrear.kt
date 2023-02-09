package com.example.myapplication


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityAddUpdateBinding
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
datosNoVacios()
        autocomplete()
        // cargarLista()
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


    fun datosNoVacios(){

        binding.etTitulo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false

                if (input.isNotEmpty()){

                    binding.Titulo.error = null

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etDescripcion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false

                if (input.isNotEmpty()){

                    binding.Descripcion.error = null

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }



    fun autocomplete() {
        var listaVisto = listOf<String>("Visto", "No visto", "Viendolo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listaVisto)
        binding.autocompleteVisto.setAdapter(adapter)
        binding.autocompleteVisto.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            binding.autocompleteVisto.setText(selected)
        }

        binding.autocompleteVisto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false
                for (item in listaVisto) {
                    if (item == input) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    binding.Visto.helperText = "Error de formato"
                    binding.Visto.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.red)
                } else {
                    binding.Visto.helperText = "Required*"
                    binding.Visto.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.green)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        var listaCategoria = listOf<String>("Anime", "Manga")
        val adapter2 =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listaCategoria)
        binding.autocompleteCategoria.setAdapter(adapter2)
        binding.autocompleteCategoria.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            binding.autocompleteCategoria.setText(selected)
        }

        binding.autocompleteCategoria.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false
                for (item in listaCategoria) {
                    if (item == input) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    binding.Categoria.helperText = "Esa categoria no existe"
                    binding.Categoria.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.red)
                } else {
                    binding.Categoria.helperText = "Required*"
                    binding.Categoria.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.green)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }


    private fun cogerDatos() {
        val datos = intent.extras
        if (datos != null) {
            editar = true
            binding.btnCrear.text = "EDITAR"
            val anime = datos.getSerializable("ANIMES") as Animes
            id = anime.id
            binding.etTitulo.setText(anime.titulo)
            //binding.etCategoria.setText(anime.categoria)
            binding.etDescripcion.setText(anime.descripcion)

            binding.autocompleteCategoria.setText(anime.categoria)

            binding.autocompleteVisto.setText(anime.visto)


            binding.etStar.rating = anime.estrellas

            val byteArray = anime.imagen // tu array de bytes
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.imageView.setImageBitmap(bitmap)


        }
    }

    private fun crearRegistro() {

        titulo = binding.etTitulo.text.toString().trim()
        descripcion = binding.etDescripcion.text.toString().trim()
        categoria = binding.autocompleteCategoria.text.toString().trim()
        estrellas = binding.etStar.rating

        visto = binding.autocompleteVisto.text.toString().trim()


        val imageView: ImageView = binding.imageView
        if (imageView.drawable != null) {
            val drawable = imageView.getDrawable()
            val bitmap = (drawable as BitmapDrawable).bitmap

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()


            if (binding.etTitulo.text!!.isEmpty()) {

               binding.Titulo.error = "El titulo no puede estar vacio"


            } else if (binding.etDescripcion.text!!.isEmpty()) {

                binding.Descripcion.error = "La descripcion no puede estar vacia"

            } else if(binding.autocompleteVisto.text.isEmpty()){


                binding.Visto.error = "Este campo no puede estar vacio"

            }else if(binding.autocompleteCategoria.text.isEmpty()){

                binding.Categoria.error = "Este campo no puede estar vacio"

            }else{

                if (!editar) {
                    val anime = Animes(1, titulo, descripcion, categoria, estrellas, byteArray, visto)
                    if (conexion.crear(anime) > -1) {
                        finish()
                    } else {
                        Toast.makeText(this, "No se pudo guardar el registro!!!", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    val anime = Animes(1, titulo, descripcion, categoria, estrellas, byteArray, visto)
                    if (conexion.update(anime) > -1) {
                        finish()
                    } else {
                        Toast.makeText(this, "No se pudo editar el registro!!!", Toast.LENGTH_SHORT)
                            .show()
                    }

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