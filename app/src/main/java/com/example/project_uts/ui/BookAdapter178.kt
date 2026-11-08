package com.example.project_uts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uts.R
import com.example.project_uts.data.entity.Book178
import com.example.project_uts.databinding.ItemBook178Binding

class BookAdapter178(
    private val onEdit: (Book178) -> Unit,
    private val onDelete: (Book178) -> Unit
) : ListAdapter<Book178, BookAdapter178.BookViewHolder178>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder178 {
        val binding = ItemBook178Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder178(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder178, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder178(
        private val binding: ItemBook178Binding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book178) {
            binding.tvJudul.text = book.judul178
            binding.tvMeta.text = binding.root.context.getString(
                R.string.book_item_meta,
                book.pengarang178,
                book.tahun178
            )
            binding.root.setOnClickListener { onEdit(book) }
            binding.btnEdit.setOnClickListener { onEdit(book) }
            binding.btnDelete.setOnClickListener { onDelete(book) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book178>() {
            override fun areItemsTheSame(oldItem: Book178, newItem: Book178): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Book178, newItem: Book178): Boolean =
                oldItem == newItem
        }
    }
}
