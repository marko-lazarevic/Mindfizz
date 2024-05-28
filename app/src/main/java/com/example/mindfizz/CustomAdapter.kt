package com.example.mindfizz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val mList: List<ItemsViewModel>,
                    private val onItemClick: (position: Int) -> Unit,
                    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.textView.text = itemsViewModel.text
        holder.number.text = (position+1).toString()+"."

        holder.imDelete.setOnClickListener {
            onDeleteClick(position)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick(getAbsoluteAdapterPosition())
                // onItemClick(adapterPosition)
            }
        }

        val textView: TextView = itemView.findViewById(R.id.textView)
        val imDelete: ImageView = itemView.findViewById(R.id.imDelete)
        val number:TextView = itemView.findViewById(R.id.number)
    }
}