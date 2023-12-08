package com.dicoding.carebymom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.carebymom.R
import com.dicoding.carebymom.data.Tips

class ListTipsAdapter(private val listips: ArrayList<Tips>) : RecyclerView.Adapter<ListTipsAdapter.ListViewHolder>() {

    private lateinit var onItemCallback : OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.ivTips)
        val tvName: TextView = itemView.findViewById(R.id.tvTips)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.tips_item,parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listips.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, photo) = listips[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.itemView.setOnClickListener{onItemCallback.onItemClicked(listips[holder.adapterPosition])}
    }

    interface OnItemClickCallback{
        fun onItemClicked(data : Tips)
    }
}