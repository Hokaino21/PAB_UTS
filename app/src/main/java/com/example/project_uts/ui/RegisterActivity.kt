package com.example.project_uts.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_uts.R
import com.example.project_uts.data.AppDatabase178
import com.example.project_uts.data.AppExecutors178
import com.example.project_uts.data.dao.UserDao178
import com.example.project_uts.data.entity.User178
import com.example.project_uts.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userDao: UserDao178

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdgeInsets(binding.root, includeIme = true)

        userDao = AppDatabase178.get(this).userDao178()

        binding.btnRegister.setOnClickListener { attemptRegister() }
        binding.tvGoLogin.setOnClickListener { finish() }
    }

    private fun attemptRegister() {
        val username = binding.etUsername.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()
        val confirm = binding.etPasswordConfirm.text?.toString().orEmpty()

        binding.tilUsername.error = null
        binding.tilPassword.error = null
        binding.tilPasswordConfirm.error = null

        if (username.isEmpty()) {
            binding.tilUsername.error = getString(R.string.error_username_empty)
            return
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_password_empty)
            return
        }
        if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.error_password_short)
            return
        }
        if (confirm != password) {
            binding.tilPasswordConfirm.error = getString(R.string.error_password_mismatch)
            return
        }

        binding.btnRegister.isEnabled = false
        AppExecutors178.diskIO.execute {
            val usernameTaken = userDao.countByUsername(username) > 0
            if (usernameTaken) {
                runOnUiThread {
                    binding.btnRegister.isEnabled = true
                    binding.tilUsername.error = getString(R.string.error_username_taken)
                }
                return@execute
            }

            val newUserId = userDao.insert(
                User178(username178 = username, password178 = password)
            )
            runOnUiThread {
                binding.btnRegister.isEnabled = true
                if (newUserId <= 0L) {
                    binding.tilUsername.error = getString(R.string.error_username_taken)
                    return@runOnUiThread
                }
                Toast.makeText(this, R.string.msg_register_success, Toast.LENGTH_SHORT).show()
                setResult(
                    RESULT_OK,
                    Intent().putExtra(EXTRA_USERNAME, username)
                )
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username_178"
    }
}
