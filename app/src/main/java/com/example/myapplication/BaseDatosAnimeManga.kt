package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatosAnimeManga(contexto: Context):SQLiteOpenHelper(contexto,DATABASE,null,VERSION){
    companion object{
        const val VERSION=1
        const val DATABASE="otakuBD.sql"
        const val TABLA="otaku_tb"
    }

    override fun onCreate(bd: SQLiteDatabase?) {
        val q = "CREATE TABLE $TABLA(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "descripcion TEXT NOT NULL, "+
                "categoria TEXT NOT NULL," +
                "estrellas FLOAT NOT NULL,"+
                "imagen BLOB NOT NULL," +
                "visto TEXT NOT NULL, " +
                "UNIQUE (titulo, categoria))"


        bd?.execSQL(q)
           }

    override fun onUpgrade(bd: SQLiteDatabase?, p1: Int, p2: Int) {
        val q="DROP TABLE IF EXISTS $TABLA"
        bd?.execSQL(q)
        onCreate(bd)
    }
    //CRUD create, read, update, delete
    //Crear un registro
    fun crear(anime: Animes) :Long{
        val conexion=this.writableDatabase
        val valores = ContentValues().apply {
            put("TITULO", anime.titulo)
            put ("DESCRIPCION",anime.descripcion)
            put ("CATEGORIA",anime.categoria)
            put("ESTRELLAS", anime.estrellas)
            put("IMAGEN", anime.imagen)
            put("VISTO", anime.visto)

        }
        val cod=conexion.insert(TABLA, null, valores)
        conexion.close()
        return cod
    }
    @SuppressLint("Range")
    fun leerTodos(): MutableList<Animes>{ //ver todos los registros
        val lista = mutableListOf<Animes>()
        val conexion = this.readableDatabase
        val consulta="SELECT * FROM $TABLA ORDER BY estrellas DESC, titulo"

        try{
            val cursor = conexion.rawQuery(consulta, null)
            if(cursor.moveToFirst()){
                do{
                    val anime=Animes(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("titulo")),
                        cursor.getString(cursor.getColumnIndex("descripcion")),
                        cursor.getString(cursor.getColumnIndex("categoria")),
                        cursor.getFloat(cursor.getColumnIndex("estrellas")),
                        cursor.getBlob(cursor.getColumnIndex("imagen")),
                        cursor.getString(cursor.getColumnIndex("visto"))
                        )
                    lista.add(anime)
                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
        conexion.close()
        return lista
    }
    //metodo para comprobar que el email es unico
    fun existeEmail(titulo: String, id: Int?): Boolean{
        val consulta = if(id==null) "SELECT id from $TABLA where titulo='$titulo'" else
            "SELECT id from $TABLA where titulo='$titulo' AND id!=$id"
        val conexion = this.readableDatabase
        var filas=0
        try{
            val cursor = conexion.rawQuery(consulta, null)
            filas =cursor.count
            cursor.close()
        }catch(e: Exception){
            e.printStackTrace()
        }
        conexion.close()
        return (filas!=0)
    }
    fun borrar(id: Int?){
        val q="DELETE FROM $TABLA WHERE id=$id"
        val conexion= this.writableDatabase
        conexion.execSQL(q)
        conexion.close()
    }
    fun update(anime: Animes): Int{
        //val q="UPDATE $TABLA SET nombre='${usuario.nombre}', email='${usuario.email}' where id=${usuario.id}"
        val conexion=this.writableDatabase
        val valores = ContentValues().apply {
            put("TITULO", anime.titulo)
            put ("DESCRIPCION",anime.descripcion)
            put ("ESTRELLAS",anime.estrellas)
            put("CATEGORIA", anime.categoria)
            put("IMAGEN", anime.imagen)
            put("VISTO", anime.visto)

        }
        val update = conexion.update(TABLA, valores, "titulo=? and categoria=?", arrayOf(anime.titulo, anime.categoria))
        conexion.close()
        return  update
    }
    fun borrarTodo(){
        val q="DELETE FROM $TABLA"
        val conexion = this.writableDatabase
        conexion.execSQL(q)
        conexion.close()
    }

}
