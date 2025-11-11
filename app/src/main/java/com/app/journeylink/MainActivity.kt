package com.app.journeylink

import PagoScreen
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.screens.*   // importa todas tus pantallas
import com.app.journeylink.ui.theme.JourneyLinkTheme

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
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "Main"
                    ) {
                        composable("Main")          { MainScreen(navController = navController) }
                        composable("Splash")        { Splash(navController = navController) }
                        composable("Login")         { Login(navController = navController) }
                        composable("Home")          { HomeScreen(navController = navController) }
                        composable("Seleccion")     { SeleccionScreen(navController = navController) }
                        composable("Companions")    { CompanionsScreen(navController = navController) }
                        composable("CompanionInfo") { CompanionInfoScreen(navController = navController) }
                        composable("Confirmacion")  { ConfirmScreen(navController = navController) }
                        composable("Pago")          { PagoScreen(navController = navController) }
                        composable("Register")      { Register(navController = navController) }
                        composable("Seguimiento")   { MisViajesScreen(navController = navController) }
                        composable("Verify")        { Verify(navController = navController) }
                        composable("Historial")     { HistorialScreen(navController = navController) }
                        composable("Perfil")        { PerfilScreen(navController = navController) }
                    }
                }
            }
        }
    }
}
