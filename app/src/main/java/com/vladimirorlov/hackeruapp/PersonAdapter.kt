package com.vladimirorlov.hackeruapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class PersonAdapter(
    private val personList: ArrayList<Person>,
//    var onItemClick: (Person) -> Unit
) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val personCard: LinearLayout = itemView.findViewById(R.id.person_card_recycler)
        val textView: TextView = itemView.findViewById(R.id.item_name)
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val removeBtn: ImageButton = itemView.findViewById(R.id.remove_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.persons_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = personList[position]

        holder.textView.text = item.name
        holder.imageView.setImageResource(item.image)

        holder.personCard.setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(it.rootView.context)
            val dialogView : View = LayoutInflater.from(it.rootView.context).inflate(R.layout.person_fragment, null)

            val dialogProfileImage: ImageView = dialogView.findViewById(R.id.fragment_person_image)
            val dialogProfileTitle: TextView = dialogView.findViewById(R.id.fragment_person_details)
            dialogProfileTitle.text = item.name
            dialogProfileImage.setImageResource(item.image)


            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.show()

        }

        holder.removeBtn.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Delete item")
            builder.setMessage("Are you sure you want to delete?")
            builder.setPositiveButton("Confirm") { dialog, which ->
                personList.removeAt(holder.layoutPosition)
                notifyItemRemoved(position)
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
            }
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return personList.size
    }
}