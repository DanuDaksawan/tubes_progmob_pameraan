package com.example.intentandactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class KaryaAdapter(private val karyaList: List<Karya>) :
    RecyclerView.Adapter<KaryaAdapter.KaryaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KaryaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karya, parent, false)
        return KaryaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KaryaViewHolder, position: Int) {
        val karya = karyaList[position]

//        holder.textJudulKarya.text = karya.getJudul()

        // Memuat gambar menggunakan Glide dari URL yang diberikan oleh objek Karya
        karya.getUrl()?.let { url ->
            Glide.with(holder.itemView.context)
                .load(url)
                .placeholder(R.drawable.default_profil) // Placeholder image jika URL kosong atau gambar gagal dimuat
                .error(R.drawable.default_profil) // Gambar error jika gambar gagal dimuat
                .into(holder.imageKarya)
        }
    }

    override fun getItemCount(): Int {
        return karyaList.size
    }

    inner class KaryaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageKarya: ImageView = itemView.findViewById(R.id.imageKarya)
//        var textJudulKarya: TextView = itemView.findViewById(R.id.textJudulKarya)
    }
}
