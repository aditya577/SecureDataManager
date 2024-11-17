package com.example.securedatamanager.data.dao

import androidx.room.*
import com.example.securedatamanager.data.database.Document

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY timestamp DESC")
    suspend fun getAllDocuments(): List<Document>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document)

    @Delete
    suspend fun deleteDocument(document: Document)
}
