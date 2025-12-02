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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.screens.*
import com.app.journeylink.ui.theme.JourneyLinkTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.navigation.NavType
import androidx.navigation.navArgument

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

        composable(
            route = "CompanionInfo/{name}/{rating}", // 1. Definimos la URL dinámica
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("rating") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            if (currentUser != null) {
                // 2. Extraemos los datos del "sobre" (backStackEntry)
                val argName = backStackEntry.arguments?.getString("name")
                val argRating = backStackEntry.arguments?.getInt("rating")

                // 3. Se los pasamos a la pantalla
                CompanionInfoScreen(
                    navController = navController,
                    userName = argName,
                    userRating = argRating
                )
            } else {
                // Tu lógica de seguridad intacta
                LaunchedEffect(Unit) {
                    navController.navigate("Login") {
                        // Aseguramos limpiar la pila correctamente
                        popUpTo("CompanionInfo/{name}/{rating}") { inclusive = true }
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