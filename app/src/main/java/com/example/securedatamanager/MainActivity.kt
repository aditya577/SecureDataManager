package com.example.securedatamanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.securedatamanager.data.database.AppDatabase
import com.example.securedatamanager.ui.notes.NoteViewModel
import com.example.securedatamanager.ui.notes.NoteViewModelFactory
import com.example.securedatamanager.ui.notes.SecureNotesScreen
import com.example.securedatamanager.ui.password.PasswordManagerScreen
import com.example.securedatamanager.ui.password.PasswordManagerViewModel
import com.example.securedatamanager.ui.password.PasswordManagerViewModelFactory
import com.example.securedatamanager.ui.theme.SecureDataManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecureDataManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

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

            PasswordManagerScreen(viewModel = viewModel)
        }
        composable("secure_notes") {
            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val noteDao = database.noteDao()
            val viewModelFactory = NoteViewModelFactory(noteDao)
            val viewModel: NoteViewModel = viewModel(factory = viewModelFactory)

            SecureNotesScreen(viewModel = viewModel)
        }
        composable("document_storage") { DocumentStorageScreen() }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate("password_manager") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Password Manager")
        }

        Button(
            onClick = { navController.navigate("secure_notes") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Secure Notes")
        }

        Button(
            onClick = { navController.navigate("document_storage") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Document Storage")
        }
    }
}

//@Composable
//fun SecureNotesScreen() {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Text(
//            text = "Secure Notes",
//            modifier = Modifier.fillMaxSize(),
//            style = MaterialTheme.typography.titleLarge
//        )
//    }
//}

@Composable
fun DocumentStorageScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = "Document Storage",
            modifier = Modifier.fillMaxSize(),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
