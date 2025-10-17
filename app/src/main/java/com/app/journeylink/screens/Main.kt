package com.app.journeylink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.screens.Login
import com.app.journeylink.screens.MainScreen
import com.app.journeylink.screens.Splash
import com.app.journeylink.ui.theme.JourneyLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JourneyLinkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Configuración del sistema de navegación
                    val navController = rememberNavController()
                    //Agregar nuevas pantallas a esta lista
                    NavHost(
                        navController = navController,
                        startDestination = "Main"
                    ) {
                        composable("Main") {
                            MainScreen(navController = navController)
                        }
                        composable("Splash") {
                            Splash(navController = navController)
                        }
                        composable("Login") {
                            Login(navController = navController)
                        }
                    }
                }
            }
        }
    }
}