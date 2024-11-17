package com.example.securedatamanager.ui.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.securedatamanager.data.dao.DocumentDao
import com.example.securedatamanager.data.database.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentViewModel(private val documentDao: DocumentDao) : ViewModel() {

    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents: StateFlow<List<Document>> get() = _documents

    init {
        loadDocuments()
    }

    private fun loadDocuments() {
        viewModelScope.launch {
            _documents.value = documentDao.getAllDocuments()
        }
    }

    fun addDocument(document: Document) {
        viewModelScope.launch {
            documentDao.insertDocument(document)
            loadDocuments()
        }
    }

    fun deleteDocument(document: Document) {
        viewModelScope.launch {
            documentDao.deleteDocument(document)
            loadDocuments()
        }
    }
}
