package com.example.intentandactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

class KeranjangAdapter(
    private val keranjangItems: List<KeranjangItem>,
    private val onDeleteClickListener: (KeranjangItem) -> Unit
) : RecyclerView.Adapter<KeranjangAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.judulkarya)
        val hargaTextView: TextView = itemView.findViewById(R.id.hargakarya)
        val imageView: ImageView = itemView.findViewById(R.id.imageKarya)
        val deleteButton: FloatingActionButton = itemView.findViewById(R.id.fab_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = keranjangItems[position]
        item.imageUrl.let { url ->
            Glide.with(holder.itemView.context)
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.gambar_1)
                        .error(R.drawable.gambar_1)
                )
                .into(holder.imageView)
        } ?: holder.imageView.setImageResource(R.drawable.gambar_1)

        holder.titleTextView.text = item.title
        holder.hargaTextView.text = item.harga

        holder.deleteButton.setOnClickListener {
            onDeleteClickListener(item)
        }
    }

    override fun getItemCount() = keranjangItems.size
}
