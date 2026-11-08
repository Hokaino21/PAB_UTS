package com.example.project_uts.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.project_uts.R
import com.example.project_uts.data.AppDatabase178
import com.example.project_uts.data.AppExecutors178
import com.example.project_uts.data.SessionManager178
import com.example.project_uts.data.dao.UserDao178
import com.example.project_uts.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager178
    private lateinit var userDao: UserDao178

    private val registerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.etUsername.setText(
                result.data?.getStringExtra(RegisterActivity.EXTRA_USERNAME).orEmpty()
            )
            binding.etPassword.setText("")
            binding.etPassword.requestFocus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager178(this)
        if (session.isLoggedIn()) {
            openDashboard()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdgeInsets(binding.root, includeIme = true)

        userDao = AppDatabase178.get(this).userDao178()

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.tvGoRegister.setOnClickListener {
            registerLauncher.launch(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val username = binding.etUsername.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()

        binding.tilUsername.error = null
        binding.tilPassword.error = null

        if (username.isEmpty()) {
            binding.tilUsername.error = getString(R.string.error_username_empty)
            return
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_password_empty)
            return
        }

        binding.btnLogin.isEnabled = false
        AppExecutors178.diskIO.execute {
            val user = userDao.login(username, password)
            runOnUiThread {
                binding.btnLogin.isEnabled = true
                if (user == null) {
                    binding.tilPassword.error = getString(R.string.error_login_failed)
                } else {
                    session.saveSession(user.id, user.username178)
                    openDashboard()
                }
            }
        }
    }

    private fun openDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
