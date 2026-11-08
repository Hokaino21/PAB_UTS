package com.example.project_uts.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.project_uts.data.entity.Book178

@Dao
interface BookDao178 {

    @Query("SELECT * FROM books_178 WHERE user_id_178 = :userId ORDER BY judul_178 COLLATE NOCASE ASC")
    fun observeByUser(userId: Long): LiveData<List<Book178>>

    @Query(
        "SELECT * FROM books_178 WHERE user_id_178 = :userId " +
            "AND (pengarang_178 LIKE '%' || :keyword || '%' OR CAST(tahun_178 AS TEXT) LIKE :keyword || '%') " +
            "ORDER BY judul_178 COLLATE NOCASE ASC"
    )
    fun searchByUser(userId: Long, keyword: String): LiveData<List<Book178>>

    @Query("SELECT * FROM books_178 WHERE id = :id AND user_id_178 = :userId LIMIT 1")
    fun findById(id: Long, userId: Long): Book178?

    @Query("SELECT COUNT(*) FROM books_178 WHERE user_id_178 = :userId")
    fun countByUser(userId: Long): Int

    @Insert
    fun insert(book: Book178): Long

    @Update
    fun update(book: Book178)

    @Delete
    fun delete(book: Book178)
}
