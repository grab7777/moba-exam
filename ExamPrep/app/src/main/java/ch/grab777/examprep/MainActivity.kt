package ch.grab777.examprep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.grab777.examprep.screens.ContactsScreen
import ch.grab777.examprep.ui.theme.MyApplicationTheme
import ch.grab777.examprep.screens.CinemasScreen
import ch.grab777.examprep.screens.SuccessScreen
import ch.grab777.examprep.viewmodels.CinemaViewModel
import ch.grab777.examprep.viewmodels.SuccessViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val cinemaViewModel: CinemaViewModel by viewModels()
                    val successViewModel: SuccessViewModel by viewModels()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            CinemasScreen(
                                innerPadding,
                                cinemaViewModel,
                                navController = navController
                            )
                        }
                        composable("success") {
                            SuccessScreen(
                                innerPadding,
                                model = successViewModel,
                                navController = navController
                            )
                        }
                        composable("contacts") {
                            ContactsScreen(
                                innerPadding,
                                contentResolver = contentResolver,
                            )
                        }
                    }
                }
            }
        }
    }
}

