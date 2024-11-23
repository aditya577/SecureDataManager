package com.secureapps.datamanager.ui.commons

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.secureapps.datamanager.data.database.AppDatabase
import com.secureapps.datamanager.ui.documents.DocumentStorageScreen
import com.secureapps.datamanager.ui.documents.DocumentViewModel
import com.secureapps.datamanager.ui.documents.DocumentViewModelFactory
import com.secureapps.datamanager.ui.notes.NoteViewModel
import com.secureapps.datamanager.ui.notes.NoteViewModelFactory
import com.secureapps.datamanager.ui.notes.SecureNotesScreen
import com.secureapps.datamanager.ui.password.PasswordManagerScreen
import com.secureapps.datamanager.ui.password.PasswordManagerViewModel
import com.secureapps.datamanager.ui.password.PasswordManagerViewModelFactory
import com.secureapps.datamanager.utils.migrateExistingNotes

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable("main_screen") { MainScreen(navController) }
        composable("password_manager") {
            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val passwordDao = database.passwordDao()
            val viewModelFactory = PasswordManagerViewModelFactory(passwordDao)
            val viewModel: PasswordManagerViewModel = viewModel(factory = viewModelFactory)

            PasswordManagerScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("secure_notes") {
            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val noteDao = database.noteDao()
            migrateExistingNotes(noteDao)
            val viewModelFactory = NoteViewModelFactory(noteDao)
            val viewModel: NoteViewModel = viewModel(factory = viewModelFactory)

            SecureNotesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("document_storage") {
            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val documentDao = database.documentDao()
            val viewModelFactory = DocumentViewModelFactory(documentDao)
            val viewModel: DocumentViewModel = viewModel(factory = viewModelFactory)

            DocumentStorageScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}