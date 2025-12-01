package com.app.journeylink

import PagoScreen
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.screens.*
import com.app.journeylink.ui.theme.JourneyLinkTheme
import com.app.journeylink.viewmodels.AuthViewModel
import com.app.journeylink.viewmodels.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JourneyLinkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth: FirebaseAuth = Firebase.auth

    // Estado para verificar si hay un usuario autenticado
    val currentUser by produceState(initialValue = auth.currentUser) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            value = firebaseAuth.currentUser
        }
        auth.addAuthStateListener(listener)
    }

    // Determinar la pantalla inicial basada en la autenticación
    val startDestination = if (currentUser != null) {
        "Home"  // Si ya está autenticado, ir directamente a Home
    } else {
        "Splash" // Si no está autenticado, empezar con Splash
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantallas públicas (accesibles sin login)
        composable("Splash") {
            Splash(navController = navController)
        }
        composable("Login") {
            Login(navController = navController)
        }
        composable("Register") {
            Register(navController = navController)
        }
        composable("Verify") {
            Verify(navController = navController)
        }

        // Pantallas privadas (requieren autenticación)
        composable("Home") {
            // Verificar autenticación antes de mostrar Home
            if (currentUser != null) {
                HomeScreen(navController = navController)
            } else {
                // Redirigir al login si no está autenticado
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Home") { inclusive = true }
                    }
                }
            }
        }

        composable("Seleccion") {
            if (currentUser != null) {
                SeleccionScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Seleccion") { inclusive = true }
                    }
                }
            }
        }

        composable("Companions") {
            if (currentUser != null) {
                CompanionsScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Companions") { inclusive = true }
                    }
                }
            }
        }

        composable("CompanionInfo") {
            if (currentUser != null) {
                CompanionInfoScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("CompanionInfo") { inclusive = true }
                    }
                }
            }
        }

        composable("Confirmacion") {
            if (currentUser != null) {
                ConfirmScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Confirmacion") { inclusive = true }
                    }
                }
            }
        }

        composable("Pago") {
            if (currentUser != null) {
                PagoScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Pago") { inclusive = true }
                    }
                }
            }
        }

        composable("Seguimiento") {
            if (currentUser != null) {
                MisViajesScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Seguimiento") { inclusive = true }
                    }
                }
            }
        }

        composable("Historial") {
            if (currentUser != null) {
                HistorialScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Historial") { inclusive = true }
                    }
                }
            }
        }

        composable("Perfil") {
            if (currentUser != null) {
                PerfilScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Perfil") { inclusive = true }
                    }
                }
            }
        }

        // Pantalla Main (si aún la necesitas)
        composable("Main") {
            if (currentUser != null) {
                MainScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        popUpTo("Main") { inclusive = true }
                    }
                }
            }
        }
    }
}