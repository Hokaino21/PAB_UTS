package com.example.project_uts.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_uts.R
import com.example.project_uts.data.AppDatabase178
import com.example.project_uts.data.AppExecutors178
import com.example.project_uts.data.SessionManager178
import com.example.project_uts.data.dao.BookDao178
import com.example.project_uts.data.entity.Book178
import com.example.project_uts.databinding.ActivityBookFormBinding

class BookFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookFormBinding
    private lateinit var session: SessionManager178
    private lateinit var bookDao: BookDao178

    private var bookId178: Long = NEW_BOOK
    private var existingBook178: Book178? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager178(this)
        if (!session.isLoggedIn()) {
            openLoginAndClearTask()
            return
        }

        binding = ActivityBookFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdgeInsets(binding.root, includeIme = true)

        bookDao = AppDatabase178.get(this).bookDao178()
        bookId178 = intent.getLongExtra(EXTRA_BOOK_ID, NEW_BOOK)

        binding.toolbarForm.setNavigationOnClickListener { finish() }
        binding.btnSave.setOnClickListener { saveBook() }

        if (bookId178 > 0L) {
            binding.toolbarForm.title = getString(R.string.book_form_edit_title)
            loadBook()
        } else {
            binding.toolbarForm.title = getString(R.string.book_form_add_title)
        }
    }

    private fun loadBook() {
        val userId = session.getUserId()
        AppExecutors178.diskIO.execute {
            val book = bookDao.findById(bookId178, userId)
            runOnUiThread {
                if (book == null) {
                    Toast.makeText(
                        this,
                        R.string.msg_book_not_found,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                    return@runOnUiThread
                }
                existingBook178 = book
                binding.etJudul.setText(book.judul178)
                binding.etPengarang.setText(book.pengarang178)
                binding.etTahun.setText(book.tahun178.toString())
            }
        }
    }

    private fun saveBook() {
        val judul = binding.etJudul.text?.toString()?.trim().orEmpty()
        val pengarang = binding.etPengarang.text?.toString()?.trim().orEmpty()
        val tahunText = binding.etTahun.text?.toString()?.trim().orEmpty()

        binding.tilJudul.error = null
        binding.tilPengarang.error = null
        binding.tilTahun.error = null

        if (judul.isEmpty()) {
            binding.tilJudul.error = getString(R.string.error_book_title_empty)
            return
        }
        if (pengarang.isEmpty()) {
            binding.tilPengarang.error = getString(R.string.error_book_author_empty)
            return
        }
        if (tahunText.isEmpty()) {
            binding.tilTahun.error = getString(R.string.error_book_year_empty)
            return
        }

        val tahun = tahunText.toIntOrNull()
        if (tahun == null || tahun < MIN_YEAR || tahun > MAX_YEAR) {
            binding.tilTahun.error = getString(R.string.error_book_year_invalid)
            return
        }

        val userId = session.getUserId()
        val book = Book178(
            id = existingBook178?.id ?: 0L,
            userId178 = userId,
            judul178 = judul,
            pengarang178 = pengarang,
            tahun178 = tahun
        )

        binding.btnSave.isEnabled = false
        AppExecutors178.diskIO.execute {
            if (existingBook178 == null) {
                bookDao.insert(book)
            } else {
                bookDao.update(book)
            }
            runOnUiThread {
                binding.btnSave.isEnabled = true
                Toast.makeText(this, R.string.msg_save_success, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_BOOK_ID = "extra_book_id_178"

        private const val NEW_BOOK = -1L
        private const val MIN_YEAR = 1000
        private const val MAX_YEAR = 2100
    }
}
