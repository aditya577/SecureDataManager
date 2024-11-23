package com.secureapps.datamanager.data.dao

import androidx.room.*
import com.secureapps.datamanager.data.database.Document


@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY timestamp DESC")
    suspend fun getAllDocuments(): List<Document>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document)

    @Delete
    suspend fun deleteDocument(document: Document)
}
