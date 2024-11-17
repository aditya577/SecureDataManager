package com.example.securedatamanager.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.securedatamanager.utils.EncryptionUtil

@Dao
interface PasswordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: Password)

    @Query("SELECT * FROM passwords")
    suspend fun getAllPasswords(): List<Password> {
        return getPasswordsRaw().map {
            it.copy(password = EncryptionUtil.decrypt(it.password))
        }
    }

    @Query("SELECT * FROM passwords")
    suspend fun getPasswordsRaw(): List<Password>

    @Delete
    suspend fun deletePassword(password: Password)
}
