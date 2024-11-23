package com.secureapps.datamanager.ui.documents

import android.content.Context
import java.io.File

fun deleteEncryptedFile(context: Context, folderUUID: String, fileUUID: String) {
    val subFolder = File(context.filesDir, folderUUID)
    val encryptedFile = File(subFolder, fileUUID)
    if (encryptedFile.exists()) {
        encryptedFile.delete()
        // If the subfolder is empty after deletion, delete the subfolder as well
        if (subFolder.listFiles()?.isEmpty() == true) {
            subFolder.delete()
        }
    }
}
