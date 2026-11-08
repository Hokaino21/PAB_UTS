package com.example.project_uts.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * Menangani edge-to-edge yang dipaksakan sistem pada targetSdk 35 ke atas,
 * agar konten tidak tertutup status bar maupun navigation bar.
 */
fun Activity.applyEdgeToEdgeInsets(root: View, includeIme: Boolean = false) {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    val controller = WindowCompat.getInsetsController(window, window.decorView)
    controller.isAppearanceLightStatusBars = true
    controller.isAppearanceLightNavigationBars = true

    val types = if (includeIme) {
        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
    } else {
        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
    }

    ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
        val bars = insets.getInsets(types)
        view.updatePadding(
            left = bars.left,
            top = bars.top,
            right = bars.right,
            bottom = bars.bottom
        )
        insets
    }
    ViewCompat.requestApplyInsets(root)
}

fun Activity.openLoginAndClearTask() {
    val intent = Intent(this, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}
