package com.secureapps.datamanager.ui.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secureapps.datamanager.data.dao.PasswordDao
import com.secureapps.datamanager.data.database.Password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordManagerViewModel(private val passwordDao: PasswordDao) : ViewModel() {

    private val _passwords = MutableStateFlow<List<Password>>(emptyList())
    val passwords: StateFlow<List<Password>> get() = _passwords

    init {
        loadPasswords()
    }

    private fun loadPasswords() {
        viewModelScope.launch {
            _passwords.value = passwordDao.getAllPasswords()
        }
    }

    fun addPassword(password: Password) {
        viewModelScope.launch {
            passwordDao.insertPassword(password)
            loadPasswords()
        }
    }

    fun deletePassword(password: Password) {
        viewModelScope.launch {
            passwordDao.deletePassword(password)
            loadPasswords() // Refresh the list
        }
    }
}
