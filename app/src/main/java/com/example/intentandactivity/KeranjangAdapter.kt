package com.example.intentandactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KeranjangAdapter(private val keranjangItems: List<KeranjangItem>) : RecyclerView.Adapter<KeranjangAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.judulkarya)
        val hargaTextView: TextView = itemView.findViewById(R.id.hargakarya)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = keranjangItems[position]
        holder.titleTextView.text = item.title
        holder.hargaTextView.text = item.harga
    }

    override fun getItemCount() = keranjangItems.size
}
