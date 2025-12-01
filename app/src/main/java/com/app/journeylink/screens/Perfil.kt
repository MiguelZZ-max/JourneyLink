package com.app.journeylink.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PerfilPreview() {
    JourneyLinkTheme {
        PerfilScreen(navController = rememberNavController())
    }
}

@Composable
fun PerfilScreen(navController: NavController) {
    var isDarkTheme by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Estados para los datos del usuario
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userRating by remember { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = Firebase.auth
    val firestore = Firebase.firestore
    val currentUser = auth.currentUser

    // Cargar datos del usuario cuando se inicia la pantalla
    LaunchedEffect(key1 = currentUser?.uid) {
        if (currentUser != null) {
            loadUserData(currentUser.uid, firestore) { name, email, rating ->
                userName = name
                userEmail = email
                userRating = rating
                isLoading = false
            }
        } else {
            isLoading = false
            errorMessage = "Usuario no autenticado"
        }
    }

    // Colores según el tema
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFF87CEEB)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            JLBottomBarPerfil(
                onMapa = { navController.navigate("Home") },
                onAdd = { navController.navigate("Companions") },
                onPerfil = { /* ya estás en Perfil */ }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            // Titulo pantalla
            Text(
                text = stringResource(R.string.perfil_titulo),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Mostrar error si existe
            errorMessage?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = message,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Información del usuario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                if (isLoading) {
                    // Mostrar loading mientras se cargan los datos
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Blue)
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = userName.ifEmpty { "Usuario" },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = userEmail.ifEmpty { "No email" },
                            fontSize = 14.sp,
                            color = textColor.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Rating: ${"%.1f".format(userRating)}",
                            fontSize = 14.sp,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Sección de calificación
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.perfil_calf),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Button(
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/games?device=phone")
                            )
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(stringResource(R.string.perfil_resena)) }
                }
            }

            // Historial de viajes
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.perfil_historial),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedButton(
                        onClick = { navController.navigate("Historial") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) { Text(stringResource(R.string.perfil_ver), color = textColor) }
                }
            }

            // Configuración
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.perfil_config),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Notificaciones - Row personalizado
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.perfil_notf),
                            color = textColor,
                            fontSize = 16.sp
                        )
                        Switch(checked = true, onCheckedChange = { })
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Tema oscuro/claro - Row personalizado
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            androidx.compose.material3.Icon(
                                if (isDarkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                                contentDescription = "Tema",
                                tint = textColor,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            Text(
                                text = if (isDarkTheme) "Tema Oscuro" else "Tema Claro",
                                color = textColor,
                                fontSize = 16.sp
                            )
                        }
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { isDarkTheme = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cerrar sesión
            Button(
                onClick = {
                    // Cerrar sesión en Firebase
                    Firebase.auth.signOut()
                    // Navegar al login y limpiar el back stack
                    navController.navigate("Login") {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
            ) { Text(stringResource(R.string.perfil_cerrar)) }
        }
    }
}

// Función para cargar datos del usuario desde Firestore
private suspend fun loadUserData(
    userId: String,
    firestore: com.google.firebase.firestore.FirebaseFirestore,
    onDataLoaded: (name: String, email: String, rating: Double) -> Unit
) {
    try {
        val document = firestore.collection("usuario")
            .document(userId)
            .get()
            .await()

        if (document.exists()) {
            val name = document.getString("name") ?: "Usuario"
            val email = document.getString("email") ?: ""
            val rating = document.getDouble("rating") ?: 0.0
            onDataLoaded(name, email, rating)
        } else {
            // Si no existe el documento, usar datos básicos del auth
            val authUser = Firebase.auth.currentUser
            val name = authUser?.displayName ?: "Usuario"
            val email = authUser?.email ?: ""
            onDataLoaded(name, email, 0.0)
        }
    } catch (e: Exception) {
        // En caso de error, usar datos básicos del auth
        val authUser = Firebase.auth.currentUser
        val name = authUser?.displayName ?: "Usuario"
        val email = authUser?.email ?: ""
        onDataLoaded(name, email, 0.0)
    }
}

/* --------- Barra inferior --------- */
@Composable
private fun JLBottomBarPerfil(
    onMapa: () -> Unit,
    onAdd: () -> Unit,
    onPerfil: () -> Unit
) {
    NavigationBar(containerColor = Color.White, tonalElevation = 4.dp) {
        NavigationBarItem(
            selected = false,
            onClick = onMapa,
            icon = { androidx.compose.material3.Icon(Icons.Filled.Place, contentDescription = stringResource(R.string.barra_ubicacion)) },
            label = {}
        )
        NavigationBarItem(
            selected = true,
            onClick = onPerfil,
            icon = { androidx.compose.material3.Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.barra_perfil)) },
            label = {}
        )
        NavigationBarItem(
            selected = false,
            onClick = onAdd,
            icon = { androidx.compose.material3.Icon(Icons.Filled.AddCircle, contentDescription = stringResource(R.string.barra_agregar)) },
            label = {}
        )
    }
}