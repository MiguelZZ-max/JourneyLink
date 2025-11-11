package com.app.journeylink.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R

@Composable
fun CompanionsScreen(navController: NavController) {
    val skyBlue = Color(0xFF87CEEB)

    Scaffold(
        containerColor = skyBlue,
        bottomBar = {
            // Barra igual que en Home; aquí marcamos seleccionado el botón "Agregar"
            JLBottomBarCompanions(
                onMapa = { navController.navigate("Home") },
                onAdd = { /* ya estás en Companions */ },
                onPerfil = { navController.navigate("Perfil") }
            )
        }
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
                onClick = { navController.navigate("CompanionInfo") }
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
                        stringResource(R.string.comp_find),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(16.dp))
                    CompanionList()
                }
            }
        }
    }
}

/* -------------------- UI interna -------------------- */

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
                Text(stringResource(R.string.comp_foto), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.comp_nombre), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Rating", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))

            // Fila con el botón clickeable
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
                    onClick = onClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = "Chespirito",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                RatingStars(rating = 4)
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
        CompanionItem(name = "Chespirito", rating = 4, distance = stringResource(R.string.comp_distancia))
        Spacer(Modifier.height(12.dp))
        CompanionItem(name = "Chespirito", rating = 3, distance = stringResource(R.string.comp_distancia))
        Spacer(Modifier.height(12.dp))
        CompanionItem(name = "Chespirito", rating = 5, distance = stringResource(R.string.comp_distancia))
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
        ) { Icon(Icons.Outlined.Image, contentDescription = null, tint = Color.Black) }

        // Nombre y distancia
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(text = name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = distance, style = MaterialTheme.typography.bodySmall)
        }

        RatingStars(rating = rating)
    }
}

/* ---------------- Barra inferior (idéntica en rutas a Home) ---------------- */

@Composable
private fun JLBottomBarCompanions(
    onMapa: () -> Unit,
    onAdd: () -> Unit,
    onPerfil: () -> Unit
) {
    NavigationBar(containerColor = Color.White, tonalElevation = 4.dp) {
        NavigationBarItem(
            selected = false,
            onClick = onMapa,
            icon = { Icon(Icons.Filled.Place, contentDescription = stringResource(R.string.barra_ubicacion)) },
            label = {}
        )
        NavigationBarItem(
            selected = false,
            onClick = onPerfil,
            icon = { Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.barra_perfil)) },
            label = {}
        )
        NavigationBarItem(
            selected = true, // ← estás en “Agregar/Companions”
            onClick = onAdd,
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = stringResource(R.string.barra_agregar)) },
            label = {}
        )
    }
}

/* Preview */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CompanionsScreenPreview() {
    val nav = rememberNavController()
    CompanionsScreen(navController = nav)
}
