package com.fk.file_api.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fk.file_api.R
import com.fk.file_api.entity.Item

class ItemListAdapter(
    private val onClick: (Item) -> Unit,
    private val onLongClick: (Item) -> Unit
) :
    ListAdapter<Item, ItemListAdapter.UserViewHolder>(ItemDiffCallback) {

    class UserViewHolder(
        val view: View
    ) :
        RecyclerView.ViewHolder(view) {
        private val itemName: TextView = view.findViewById(R.id.itemName)
        private val modificationDate: TextView = view.findViewById(R.id.modificationDate)

        fun bind(item: Item, onClick: (Item) -> Unit, onLongClick: (Item) -> Unit) {
            itemName.text = item.name
            modificationDate.text = item.modificationDate
            view.setOnClickListener { onClick.invoke(item) }
            view.setOnLongClickListener { onLongClick.invoke(item); true }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int) {
        val user = getItem(position)
        viewHolder.bind(user, onClick, onLongClick)
    }
}

object ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}