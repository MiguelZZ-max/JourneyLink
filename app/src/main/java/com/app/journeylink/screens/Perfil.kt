package com.app.journeylink.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.ui.theme.JourneyLinkTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PerfilPreview() {
    JourneyLinkTheme {
        PerfilScreen(navController = rememberNavController())
    }
}

@Composable
fun PerfilScreen(
    navController: NavController? = null
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Colores según el tema
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFF87CEEB)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header simple con título
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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Usuario 1",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Miembro desde 2024",
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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "¡Calificanos!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/games?device=phone"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dejar reseña en Play Store")
                }
            }
        }

        // Historial de viajes
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Historial de viajes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Opción: Ver mis viajes
                OutlinedButton(
                    onClick = {
                        navController?.navigate("Historial")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        "Ver mis viajes",
                        color = textColor
                    )
                }

                // Opción: Idioma
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Idioma",
                        fontSize = 16.sp,
                        color = textColor
                    )
                    Text(
                        "Español",
                        fontSize = 16.sp,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Configuración
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Configuración",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Notificaciones
                ListItem(
                    headlineContent = {
                        Text(
                            "Notificaciones",
                            color = textColor
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = true,
                            onCheckedChange = { }
                        )
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Cambiar tema
                ListItem(
                    headlineContent = {
                        Text(
                            if (isDarkTheme) "Cambiar a tema claro" else "Cambiar a tema oscuro",
                            color = textColor
                        )
                    },
                    leadingContent = {
                        Icon(
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

        // Botón de cerrar sesión
        Button(
            onClick = {
                navController?.navigate("login") {
                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Magenta
            )
        ) {
            Text("Cerrar sesión")
        }
    }
}


