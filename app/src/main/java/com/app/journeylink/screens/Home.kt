package com.app.journeylink.screens

import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.app.journeylink.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    val fondo = Color(0xFFE9EDF1)
    val azulTarjeta = Color(0xFF9FD6FF)

    Scaffold(
        containerColor = fondo,
        bottomBar = {
            BottomBar(
                onMapa = { /* ya estás en Home */ },
                onAdd  = { navController.navigate("Companions") },
                onPerfil = { navController.navigate("Perfil") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MapPlaceholder()

            Card(
                colors = CardDefaults.cardColors(containerColor = azulTarjeta),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    TypeSelector()
                    SearchBox()
                    DestinationList(
                        listOf(
                            "Velaria Mall",
                            "Isla San Marcos",
                            "Central Camionera de AGS",
                            "Altaria Mall"
                        )
                    )
                }
            }

            // Botón "Siguiente"
            Button(
                onClick = { navController.navigate("Seleccion") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(contentColor = azulTarjeta)
            ) {
                Text(text = "Siguiente", color = Color.White)
            }
        }
    }
}



/* ---------- Secciones UI ---------- */

@Composable
private fun MapPlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.mapa),
        contentDescription = "Mapa",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}


@Composable
private fun TypeSelector() {
    var selected by remember { mutableStateOf(0) }
    val icons = listOf(
        Icons.Filled.DirectionsCar,
        Icons.Filled.LocalShipping,
        Icons.Filled.Flight
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icons.forEachIndexed { i, icon ->
            val active = selected == i
            Box(
                modifier = Modifier
                    .weight(1f)                 // ← MISMO ANCHO para los 3
                    .height(56.dp)              // misma altura
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (active) Color.White else Color.Transparent)
                    .clickable { selected = i },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF1F2A33)
                )
            }
        }
    }
}


@Composable
private fun SearchBox() {
    var query by remember { mutableStateOf("") }
    TextField(
        value = query,
        onValueChange = { query = it },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        placeholder = {
            Text("¿A dónde y por dónde?", maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,   // tamaño/altura de línea correctos
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)   // ← en vez de .height(48.dp)
    )
}


@Composable
private fun DestinationList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { DestinationRow(it) }
    }
}

@Composable
private fun DestinationRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { /* TODO: navegar al detalle */ }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(26.dp).clip(CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Place, contentDescription = null, tint = Color(0xFF1F2A33)) }
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xFF1F2A33)
        )
    }
}

@Composable
private fun BottomBar(onMapa: () -> Unit, onAdd: () -> Unit, onPerfil: () -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 4.dp) {
        NavigationBarItem(
            selected = true, onClick = onMapa,
            icon = { Icon(Icons.Filled.Place, contentDescription = "Mapa") },
            label = { Text("Mapa") }
        )
        NavigationBarItem(
            selected = false, onClick = onPerfil,
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
        NavigationBarItem(
            selected = false, onClick = onAdd,
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Agregar") },
            label = { Text("Agregar") }
        )

    }
}
