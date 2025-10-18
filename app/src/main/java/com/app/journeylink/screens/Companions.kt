package com.app.journeylink.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CompanionsScreen(navController: NavController) {
    val skyBlue = Color(0xFF87CEEB)

    Scaffold(
        containerColor = skyBlue
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(skyBlue)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Tarjeta superior (Nombre / Rating) → clic navega a detalle
            FeaturedCompanionCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
                onClick = {
                    // Navegar a la siguiente pantalla al hacer clic en el botón
                    navController.navigate("CompanionInfo")
                }
            )

            Spacer(Modifier.height(16.dp))

            // Panel blanco redondeado con lista
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(28.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        "Encuentra Acompañantes por tu área",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Sección con lista de acompañantes (similar a la imagen)
                    Spacer(Modifier.height(16.dp))
                    CompanionList()
                }
            }
        }
    }
}

@Composable
private fun FeaturedCompanionCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(Modifier.padding(16.dp)) {
            // Encabezados
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Photo", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Nombre", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Rating", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))

            // Fila con el botón que será clickeable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Placeholder foto
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEFEFEF)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Outlined.Image, contentDescription = null, tint = Color.Black) }


                Button(
                    onClick = onClick,  // Navegará a CompanionInfoScreen
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = "Chespirito",  // El nombre que quieres mostrar en el botón
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                RatingStars(rating = 4)  // Placeholder de rating
            }

            Spacer(Modifier.height(120.dp))
        }
    }
}

@Composable
private fun RatingStars(
    rating: Int,
    size: Dp = 20.dp,
    tint: Color = Color.Black
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val icon = if (index < rating) Icons.Outlined.Star else Icons.Outlined.StarBorder
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(size))
        }
    }
}

@Composable
private fun CompanionList() {
    Column {
        CompanionItem(name = "Chespirito", rating = 4, distance = "A 5 km de ti")
        Spacer(Modifier.height(12.dp))
        CompanionItem(name = "Chespirito", rating = 3, distance = "A 5 km de ti")
        Spacer(Modifier.height(12.dp))
        CompanionItem(name = "Chespirito", rating = 5, distance = "A 5 km de ti")
    }
}

@Composable
private fun CompanionItem(name: String, rating: Int, distance: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Placeholder foto
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFEFEF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Image, contentDescription = null, tint = Color.Black)
        }

        // Nombre y distancia
        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(text = name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = distance, style = MaterialTheme.typography.bodySmall)
        }

        // Calificación
        RatingStars(rating = rating)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CompanionsScreenPreview() {
    val nav = rememberNavController()
    CompanionsScreen(navController = nav)
}
