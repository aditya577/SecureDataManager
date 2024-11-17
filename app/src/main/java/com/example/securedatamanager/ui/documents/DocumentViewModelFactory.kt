package com.example.securedatamanager.ui.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.securedatamanager.data.dao.DocumentDao

class DocumentViewModelFactory(private val documentDao: DocumentDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocumentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DocumentViewModel(documentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
