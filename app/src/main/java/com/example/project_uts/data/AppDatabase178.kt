package com.example.project_uts.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project_uts.data.dao.BookDao178
import com.example.project_uts.data.dao.UserDao178
import com.example.project_uts.data.entity.Book178
import com.example.project_uts.data.entity.User178

@Database(
    entities = [User178::class, Book178::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase178 : RoomDatabase() {

    abstract fun userDao178(): UserDao178

    abstract fun bookDao178(): BookDao178

    companion object {
        private const val DB_NAME = "bookapp_178.db"

        @Volatile
        private var instance: AppDatabase178? = null

        fun get(context: Context): AppDatabase178 {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase178::class.java,
                    DB_NAME
                ).build().also { instance = it }
            }
        }
    }
}
