package com.example.project_uts.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabel pengguna. Tiga digit NIM 178 dipakai pada nama tabel dan kolom.
 */
@Entity(
    tableName = "users_178",
    indices = [Index(value = ["username_178"], unique = true)]
)
data class User178(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "username_178")
    val username178: String,

    @ColumnInfo(name = "password_178")
    val password178: String
)
