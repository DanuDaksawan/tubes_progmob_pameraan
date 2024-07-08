package com.example.intentandactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intentandactivity.model.Karya
import com.example.intentandactivity.R

class KaryaAdapter(
    private val karyaList: List<Karya>,
    private val onItemCLick: (Karya) -> Unit
) : RecyclerView.Adapter<KaryaAdapter.KaryaViewHolder>() {

    inner class KaryaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var idKarya: TextView = itemView.findViewById(R.id.idKarya)
        var imageKarya: ImageView = itemView.findViewById(R.id.imageKarya)
        var judulKarya: TextView = itemView.findViewById(R.id.judulkarya)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaryaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karya, parent, false)
        return KaryaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KaryaViewHolder, position: Int) {
        val karya = karyaList[position]

        // Memuat gambar menggunakan Glide dari URL yang diberikan oleh objek Karya
        karya.url?.let { url ->
            Glide.with(holder.itemView.context)
                .load(url)
                .placeholder(R.drawable.default_profil) // Placeholder image jika URL kosong atau gambar gagal dimuat
                .error(R.drawable.default_profil) // Gambar error jika gambar gagal dimuat
                .into(holder.imageKarya)
        }

        // Menampilkan judul Karya
        holder.judulKarya.text = karya.title
//        if (karya.id == null) {
//            holder.idKarya.text = "Tidak ada"
//        } else {
//            holder.idKarya.text = karya.id
//        }

        holder.itemView.setOnClickListener{
            onItemCLick(karya)
        }

    }

    override fun getItemCount(): Int {
        return karyaList.size
    }
}
