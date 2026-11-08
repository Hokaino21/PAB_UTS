package com.example.project_uts.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_uts.R
import com.example.project_uts.data.AppDatabase178
import com.example.project_uts.data.AppExecutors178
import com.example.project_uts.data.SessionManager178
import com.example.project_uts.data.dao.BookDao178
import com.example.project_uts.data.entity.Book178
import com.example.project_uts.databinding.ActivityBookListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookListBinding
    private lateinit var session: SessionManager178
    private lateinit var bookDao: BookDao178
    private lateinit var adapter: BookAdapter178

    private val keyword178 = MutableLiveData("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager178(this)
        if (!session.isLoggedIn()) {
            openLoginAndClearTask()
            return
        }

        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdgeInsets(binding.root)

        bookDao = AppDatabase178.get(this).bookDao178()

        adapter = BookAdapter178(
            onEdit = { book -> openForm(book.id) },
            onDelete = { book -> confirmDelete(book) }
        )
        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = adapter

        binding.toolbarBooks.setNavigationOnClickListener { finish() }
        binding.fabAdd.setOnClickListener { openForm(NEW_BOOK) }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            keyword178.value = text?.toString().orEmpty()
        }

        val userId = session.getUserId()
        keyword178.switchMap { keyword ->
            val query = keyword.trim()
            if (query.isEmpty()) {
                bookDao.observeByUser(userId)
            } else {
                bookDao.searchByUser(userId, query)
            }
        }.observe(this) { books ->
            showBooks(books)
        }
    }

    private fun showBooks(books: List<Book178>) {
        adapter.submitList(books)
        val isEmpty = books.isEmpty()
        binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.tvEmpty.setText(
            if (keyword178.value.isNullOrBlank()) R.string.empty_books else R.string.empty_search
        )
    }

    private fun openForm(bookId: Long) {
        val intent = Intent(this, BookFormActivity::class.java)
        intent.putExtra(BookFormActivity.EXTRA_BOOK_ID, bookId)
        startActivity(intent)
    }

    private fun confirmDelete(book: Book178) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_title)
            .setMessage(getString(R.string.delete_message, book.judul178))
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                AppExecutors178.diskIO.execute {
                    bookDao.delete(book)
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            R.string.msg_delete_success,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .show()
    }

    companion object {
        private const val NEW_BOOK = -1L
    }
}
