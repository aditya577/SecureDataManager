package com.secureapps.datamanager.utils

import com.secureapps.datamanager.data.dao.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun migrateExistingNotes(noteDao: NoteDao) {
    CoroutineScope(Dispatchers.IO).launch {
        val notes = noteDao.getAllNotes()
        notes.forEach { note ->
            if (!EncryptionUtil.isBase64(note.content)) { // Encrypt if not already encrypted
                val encryptedContent = EncryptionUtil.encrypt(note.content)
                noteDao.insertNote(note.copy(content = encryptedContent))
            }
        }
    }
}