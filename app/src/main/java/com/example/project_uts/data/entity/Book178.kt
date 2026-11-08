package com.example.project_uts.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabel buku. Setiap baris terikat ke satu pengguna lewat user_id_178,
 * sehingga data antar pengguna tidak pernah tercampur.
 */
@Entity(
    tableName = "books_178",
    foreignKeys = [
        ForeignKey(
            entity = User178::class,
            parentColumns = ["id"],
            childColumns = ["user_id_178"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id_178"])]
)
data class Book178(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "user_id_178")
    val userId178: Long,

    @ColumnInfo(name = "judul_178")
    val judul178: String,

    @ColumnInfo(name = "pengarang_178")
    val pengarang178: String,

    @ColumnInfo(name = "tahun_178")
    val tahun178: Int
)
