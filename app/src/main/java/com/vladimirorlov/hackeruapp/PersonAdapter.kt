package com.vladimirorlov.hackeruapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(private val personList: ArrayList<Person>)
    : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = itemView.findViewById(R.id.person_name)
        val imageView : ImageView = itemView.findViewById(R.id.person_image)
        val removeBtn : ImageButton = itemView.findViewById(R.id.remove_button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.persons_layout,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = personList[position]

        holder.textView.text = item.name
        holder.imageView.setImageResource(item.image)

        holder.removeBtn.setOnClickListener{
            personList.removeAt(holder.layoutPosition)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return personList.size
    }

}