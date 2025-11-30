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

    // Colores según el tema
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFF87CEEB)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            // Barra de navegación igual que en Home, con mismas rutas
            JLBottomBarPerfil(
                onMapa = { navController.navigate("Home") },          // ir a Home
                onAdd = { navController.navigate("Companions") },     // ir a Companions
                onPerfil = { /* ya estás en Perfil */ }               // actual
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
            // Header simple con “A”
            Text(
                text = "A",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Información del usuario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Usuario 1",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.perfil_age),
                        fontSize = 14.sp,
                        color = textColor.copy(alpha = 0.7f)
                    )
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // (espacio preparado por si agregas más acciones)
                    }
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

                    // Notificaciones
                    ListItem(
                        headlineContent = {
                            Text(stringResource(R.string.perfil_notf), color = textColor)
                        },
                        trailingContent = {
                            Switch(checked = true, onCheckedChange = { })
                        }
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Cambiar tema
                    ListItem(
                        headlineContent = {
                            Text(
                                if (isDarkTheme)
                                    stringResource(R.string.perfil_temaclaro)
                                else
                                    stringResource(R.string.perfil_temaoscuro),
                                color = textColor
                            )
                        },
                        leadingContent = {
                            androidx.compose.material3.Icon(
                                imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = "Cambiar tema",
                                tint = textColor
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { isDarkTheme = it }
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cerrar sesión
            Button(
                onClick = {
                    navController.navigate("Login") { popUpTo(0) }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
            ) { Text(stringResource(R.string.perfil_cerrar)) }
        }
    }
}

/* --------- Barra inferior (idéntica lógica a Home) --------- */
/* Nota: uso otro nombre para no chocar con la BottomBar privada de Home.kt */
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
            label = {} // sin texto, como en tu Home
        )
        NavigationBarItem(
            selected = true, // estamos en Perfil
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
