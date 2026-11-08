package com.example.project_uts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_uts.data.entity.User178

@Dao
interface UserDao178 {

    @Insert
    fun insert(user: User178): Long

    @Query("SELECT * FROM users_178 WHERE username_178 = :username AND password_178 = :password LIMIT 1")
    fun login(username: String, password: String): User178?

    @Query("SELECT COUNT(*) FROM users_178 WHERE username_178 = :username")
    fun countByUsername(username: String): Int

    @Query("SELECT * FROM users_178 WHERE id = :id LIMIT 1")
    fun findById(id: Long): User178?
}
