package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SearchView.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var rvMain: RecyclerView

    private lateinit var binding: MainActivityBinding
    private lateinit var conexion: BaseDatosAnimeManga
    private lateinit var miAdapter: AnimesAdapter
    private var lista = mutableListOf<Animes>()

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner

    private lateinit var searchView: androidx.appcompat.widget.SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        rvMain = findViewById(R.id.rvMain)




        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        conexion = BaseDatosAnimeManga(this)
        searchView = binding.searchview
        searchView.clearFocus()
        spinners()

//pruebaLista()
        listeners()
        setRecycler()
    }


    fun spinners() {

        var listaVisto = listOf<String>("TODO", "VISTO", "NO VISTO", "VIENDOLO")
        var listaCategoria = listOf<String>("TODO", "ANIME", "MANGA")
        spinner1 = binding.spVisto
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaVisto)
        spinner1.adapter = adapter

// escuchar selección del Spinner
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var seleccion = parent?.getItemAtPosition(position).toString()

                var listaCopia = mutableListOf<Animes>()

                if (seleccion.equals("TODO")){

                    miAdapter.setList(lista)

                }else{

                    for (anime in lista) {

                        if (anime.visto.toLowerCase().equals(seleccion.toLowerCase())) {
                            listaCopia.add(anime)
                        }

                    }

                    if (listaCopia.isEmpty()) {

                        binding.tvNo.visibility = View.VISIBLE

                    } else {

                        miAdapter.setList(listaCopia)

                    }

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // acción cuando no se selecciona ningún elemento
            }
        }

         spinner2 = binding.spCategoria
        val adapter2 =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaCategoria)
        spinner2.adapter = adapter2

// escuchar selección del Spinner
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var seleccion = parent?.getItemAtPosition(position).toString()

                var listaCopia = mutableListOf<Animes>()

                if (seleccion.equals("TODO")){

                    miAdapter.setList(lista)

                }else{

                    for (anime in lista) {

                        if (anime.categoria.toLowerCase().equals(seleccion.toLowerCase())) {
                            listaCopia.add(anime)
                        }

                    }

                    if (listaCopia.isEmpty()) {

                        binding.tvNo.visibility = View.VISIBLE

                    } else {

                        miAdapter.setList(listaCopia)

                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // acción cuando no se selecciona ningún elemento
            }
        }


    }

    fun listeners() {

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, ActivityCrear::class.java))
        }
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the submit event here
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val spiner1Sel = spinner1.selectedItem.toString()
                val spiner2Sel = spinner2.selectedItem.toString()
                filterList(newText, spiner1Sel, spiner2Sel)
                return true
            }
        })


    }

    private fun filterList(text: String?, textoSpinner1: String, textoSpinner2:String) {

        var listaCopia = mutableListOf<Animes>()

        for (anime in lista) {

           if (textoSpinner1.equals("TODO") && textoSpinner2.equals("TODO")){
               if (anime.titulo.toLowerCase().contains(text.toString().toLowerCase())) {
                   listaCopia.add(anime)
               }
           }else if(!textoSpinner1.equals("TODO") && textoSpinner2.equals("TODO")){

               if (anime.titulo.toLowerCase().contains(text.toString().toLowerCase()) && (anime.visto.toLowerCase().equals(textoSpinner1.toLowerCase()) )) {
                   listaCopia.add(anime)
               }

           }else if(textoSpinner1.equals("TODO") && !textoSpinner2.equals("TODO")){

               if (anime.titulo.toLowerCase().contains(text.toString().toLowerCase()) && (anime.categoria.toLowerCase().equals(textoSpinner2.toLowerCase()) )) {
                   listaCopia.add(anime)
               }

           }else if(!textoSpinner1.equals("TODO") && !textoSpinner2.equals("TODO")){

               if (anime.titulo.toLowerCase().contains(text.toString().toLowerCase())  && anime.categoria.toLowerCase().equals(textoSpinner2.toLowerCase()) &&  anime.visto.toLowerCase().equals(textoSpinner1.toLowerCase())) {
                   listaCopia.add(anime)
               }

           }

        }
        if (listaCopia.isEmpty()) {



           Toast.makeText(this, "No se ha encontrado ninguna opcion con ese nombre", Toast.LENGTH_LONG)

        } else {

            miAdapter.setList(listaCopia)

        }

    }

    override fun onResume() {
        setRecycler()
        searchView.setQuery("", false)
        super.onResume()
    }

    private fun setRecycler() {
        lista = conexion.leerTodos()
        binding.tvNo.visibility = View.INVISIBLE
        if (lista.size == 0) {
            binding.tvNo.visibility = View.VISIBLE
            return
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvMain.layoutManager = layoutManager
        miAdapter = AnimesAdapter(lista, { onItemDelete(it) }) { anime -> onItemUpdate(anime) }
        binding.rvMain.adapter = miAdapter

    }


    private fun onItemDelete(position: Int) {
        val usuario = lista[position]
        conexion.borrar(usuario.id)
        //borramos de la lista e indicamos al adapter que hemos
        //eliminado un registro
        lista.removeAt(position)
        if (lista.size == 0) {
            binding.tvNo.visibility = View.VISIBLE
        }
        miAdapter.notifyItemRemoved(position)
    }

    private fun onItemUpdate(anime: Animes) {
        val i = Intent(this, ActivityCrear::class.java).apply {
            putExtra("ANIMES", anime)
        }
        startActivity(i)
    }

}
