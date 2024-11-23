package com.secureapps.datamanager.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val folderUUID: String,
    val fileUUID: String,
    val timestamp: Long
)
