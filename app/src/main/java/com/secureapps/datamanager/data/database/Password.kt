package com.secureapps.datamanager.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "site_name") val siteName: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String // Encrypted
)
