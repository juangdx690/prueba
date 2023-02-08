package com.example.myapplication

data class Animes(
    var id:Int?,
    var titulo:String,
    var descripcion:String,
    var categoria:String,
    var estrellas:Float,
    var imagen: ByteArray,
    var visto: String
):java.io.Serializable

