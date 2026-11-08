package com.example.project_uts.data

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Eksekutor tunggal untuk operasi tulis database agar tidak berjalan di main thread.
 */
object AppExecutors178 {
    val diskIO: Executor = Executors.newSingleThreadExecutor()
}
