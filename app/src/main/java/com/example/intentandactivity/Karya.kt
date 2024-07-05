package com.example.intentandactivity

class Karya(judul: String, deskripsi: String?, url: String?) {
    private var judul: String = judul
    private var deskripsi: String? = deskripsi
    private var url: String? = url

    fun getJudul(): String {
        return judul
    }

    fun getDeskripsi(): String? {
        return deskripsi
    }

    fun getUrl(): String? {
        return url
    }
}
