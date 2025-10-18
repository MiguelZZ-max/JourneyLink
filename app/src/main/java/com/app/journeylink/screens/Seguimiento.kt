package com.app.journeylink.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MisViajesPreview() {
    JourneyLinkTheme {
        MisViajesScreen(navController = rememberNavController())
    }
}
@Composable
fun MisViajesScreen(
    navController: NavController? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
    ) {
        // Título
        Text(
            text = "Mis viajes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Contenido principal - Sin scroll
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            // Imagen
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Imagen del viaje",
                modifier = Modifier
                    .size(145.dp, 120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fecha 1
            Text(
                text = "7 de Septiembre de 2025",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Eventos de la primera fecha
            EventoItem(text = "8:00 am - Ags - CD México - Aero México 2451")
            EventoItem(text = "9:40 am - Encontrarte con Juan Gonzales")
            EventoItem(text = "8:50 pm - Hospedaje Hotel Real Plaza")

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha 2
            Text(
                text = "20 de Septiembre de 2025",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Eventos de la segunda fecha
            EventoItem(text = "11:30 am - Desalojar Hotel Real Plaza")
            EventoItem(text = "5:00 pm - Paris - CD México - Aero México 8928")
            EventoItem(text = "5:50 pm - CD México - Ags - Aero México 8922")
        }
        BottomBar(
            onMapa = { navController?.navigate("mapa") },
            onAdd = { navController?.navigate("agregar") },
            onPerfil = { navController?.navigate("perfil") }
        )
    }

}

@Composable
fun EventoItem(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, MaterialTheme.shapes.small)
            .padding(8.dp)
    )
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
            selected = false, onClick = onAdd,
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Agregar") },
            label = { Text("Agregar") }
        )
        NavigationBarItem(
            selected = false, onClick = onPerfil,
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}

