package com.example.project_uts.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project_uts.R
import com.example.project_uts.data.AppDatabase178
import com.example.project_uts.data.AppExecutors178
import com.example.project_uts.data.SessionManager178
import com.example.project_uts.data.dao.BookDao178
import com.example.project_uts.data.dao.UserDao178
import com.example.project_uts.databinding.ActivityDashboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var session: SessionManager178
    private lateinit var userDao: UserDao178
    private lateinit var bookDao: BookDao178

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager178(this)
        if (!session.isLoggedIn()) {
            openLoginAndClearTask()
            return
        }

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdgeInsets(binding.root)

        val database = AppDatabase178.get(this)
        userDao = database.userDao178()
        bookDao = database.bookDao178()

        binding.btnManageBooks.setOnClickListener {
            startActivity(Intent(this, BookListActivity::class.java))
        }
        binding.btnLogout.setOnClickListener { confirmLogout() }

        loadUser()
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized) {
            refreshBookCount()
        }
    }

    private fun loadUser() {
        val userId = session.getUserId()
        AppExecutors178.diskIO.execute {
            val user = userDao.findById(userId)
            runOnUiThread {
                if (user == null) {
                    session.logout()
                    openLoginAndClearTask()
                    return@runOnUiThread
                }
                binding.tvGreeting.text = getString(
                    R.string.dashboard_greeting,
                    user.username178
                )
            }
        }
    }

    private fun refreshBookCount() {
        val userId = session.getUserId()
        AppExecutors178.diskIO.execute {
            val total = bookDao.countByUser(userId)
            runOnUiThread {
                binding.tvBookCount.text = total.toString()
            }
        }
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_logout) { _, _ ->
                session.logout()
                openLoginAndClearTask()
            }
            .show()
    }
}
