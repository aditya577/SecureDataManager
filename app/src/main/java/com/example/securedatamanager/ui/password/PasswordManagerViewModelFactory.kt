package com.example.securedatamanager.ui.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.securedatamanager.data.database.PasswordDao

class PasswordManagerViewModelFactory(private val passwordDao: PasswordDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordManagerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordManagerViewModel(passwordDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}