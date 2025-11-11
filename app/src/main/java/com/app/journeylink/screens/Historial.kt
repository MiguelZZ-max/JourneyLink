package com.app.journeylink.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme

// Data class para representar un viaje
data class Viaje(
    val id: Int,
    val destino: String,
    val fecha: String,
    val tipo: String // "actual" o "anterior"
)


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistorialPreview() {
    JourneyLinkTheme {
        HistorialScreen(navController = rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    navController: NavController? = null
) {
    var searchText by remember { mutableStateOf("") }
    var filtroExpanded by remember { mutableStateOf(false) }
    var filtroSeleccionado by remember { mutableStateOf("Todos los viajes") }

    // Lista de viajes de ejemplo
    val viajes = listOf(
        Viaje(1, "Paris, Francia", "7-20 Sept 2025", "actual"),
        Viaje(2, "Londres, Reino Unido", "15-25 Oct 2025", "actual"),
        Viaje(3, "Tokio, Japón", "1-10 Nov 2025", "actual"),
        Viaje(4, "New York, USA", "10-15 Ago 2024", "anterior"),
        Viaje(5, "Roma, Italia", "5-12 Jul 2024", "anterior"),
        Viaje(6, "Barcelona, España", "20-27 Jun 2024", "anterior")
    )

    // Filtrar viajes según selección
    val viajesFiltrados = when (filtroSeleccionado) {
        "Viajes actuales" -> viajes.filter { it.tipo == "actual" }
        "Viajes anteriores" -> viajes.filter { it.tipo == "anterior" }
        else -> viajes // "Todos los viajes"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
            .padding(16.dp)
    ) {
        // Header con título "A"
        Text(
            text = "A",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Información del usuario
        Text(
            text = "Usuario 1",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Sección de Viajes Actuales
        Text(
            text = stringResource(R.string.hist_filtrar),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Barra de búsqueda
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.hist_buscar),
                    tint = Color.Gray,
                    modifier = Modifier.padding(end = 12.dp)
                )

                // Campo de búsqueda
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (searchText.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.hist_buscar),
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        // Filtro con DropdownMenu
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.hist_filtrar),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Dropdown para selección de filtro
                ExposedDropdownMenuBox(
                    expanded = filtroExpanded,
                    onExpandedChange = { filtroExpanded = !filtroExpanded }
                ) {
                    OutlinedTextField(
                        value = filtroSeleccionado,
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .background(Color.White),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = stringResource(R.string.hist_filtrardesc)
                            )
                        },
                    )

                    ExposedDropdownMenu(
                        expanded = filtroExpanded,
                        onDismissRequest = { filtroExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { stringResource(R.string.hist_viajes) },
                            onClick = {
                                filtroSeleccionado = "Todos los viajes"
                                filtroExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { stringResource(R.string.hist_viajesact)},
                            onClick = {
                                filtroSeleccionado = "Viajes actuales"
                                filtroExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { stringResource(R.string.hist_viajesant) },
                            onClick = {
                                filtroSeleccionado = "Viajes anteriores"
                                filtroExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Contenedor de viajes
        Text(
            text = "Viajes (${viajesFiltrados.size})",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Lista de viajes
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viajesFiltrados) { viaje ->
                ViajeItem(
                    viaje = viaje,
                    onSeguirViaje = {
                        navController!!.navigate("Seguimiento")
                    }
                )
            }
        }
    }
}

@Composable
fun ViajeItem(
    viaje: Viaje,
    onSeguirViaje: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Información del viaje
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = viaje.destino,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = viaje.fecha,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = if (viaje.tipo == "actual") "Viaje Actual" else "Viaje Anterior",
                    fontSize = 12.sp,
                    color = if (viaje.tipo == "actual") Color(0xFF4CAF50) else Color(0xFF757575),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Botón "Seguir Viaje"
            Button(
                onClick = {
                    onSeguirViaje ()



                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.hist_seg),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SimpleViajeItem(
    viaje: Viaje,
    onSeguirViaje: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = viaje.destino,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = viaje.fecha,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Button(
                onClick = onSeguirViaje,
                modifier = Modifier.height(36.dp)
            ) {
                Text("Seguir")
            }
        }
    }
}