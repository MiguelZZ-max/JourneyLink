package com.app.journeylink.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Modelo de datos que coincide con la coleccion usuario de Firestore
data class CompanionData(
    val id: String = "",
    val name: String = "",
    val rating: Int = 0,
    val email: String = "",
    val distance: String = "1.5 km" // Valor por defecto no presente en BD
)

@Composable
fun CompanionsScreen(navController: NavController) {
    val skyBlue = Color(0xFF87CEEB)

    // Estado para la lista de datos y carga
    var listData by remember { mutableStateOf<List<CompanionData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Filtra el primer usuario con rating mayor a 4 para destacar
    val featuredUser = listData.firstOrNull { it.rating >= 4 }

    // Carga datos de la coleccion usuario al iniciar la pantalla
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("usuario")
            .get()
            .addOnSuccessListener { result ->
                val items = result.documents.mapNotNull { doc ->
                    doc.toObject(CompanionData::class.java)?.copy(id = doc.id)
                }
                listData = items
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al obtener usuarios", e)
                isLoading = false
            }
    }

    Scaffold(
        containerColor = skyBlue,
        bottomBar = {
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
            // Tarjeta superior con datos dinamicos
            FeaturedCompanionCard(
                companion = featuredUser,
                modifier = Modifier.fillMaxWidth().heightIn(min = 220.dp),
                onClick = {
                    // Pasamos los datos reales o valores seguros si es nulo
                    val name = featuredUser?.name ?: "Desconocido"
                    val rating = featuredUser?.rating ?: 0
                    navController.navigate("CompanionInfo/$name/$rating")
                }
            )

            Spacer(Modifier.height(16.dp))

            // Panel de lista dinamica
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

                    if (isLoading) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = skyBlue)
                        }
                    } else {
                        CompanionList(
                            companions = listData,
                            onItemClick = { item ->
                                // Navegamos pasando los datos de ESTE item específico
                                navController.navigate("CompanionInfo/${item.name}/${item.rating}")
                            }
                        )
                    }
                }
            }
        }
    }
}

/* -------------------- UI interna -------------------- */

@Composable
private fun FeaturedCompanionCard(
    companion: CompanionData?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.comp_foto), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.comp_nombre), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Rating", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    // Muestra el nombre real o un texto de espera si es nulo
                    Text(
                        text = companion?.name ?: "Buscando destacado...",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Muestra rating real o 0 si es nulo
                RatingStars(rating = companion?.rating ?: 0)
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
private fun CompanionList(
    companions: List<CompanionData>,
    onItemClick: (CompanionData) -> Unit // Nuevo parámetro para manejar el click
) {
    LazyColumn {
        items(companions) { item ->
            // Envolvemos el item en un Surface o Box para hacerlo clickeable
            Surface(
                color = Color.Transparent, // Transparente para no afectar el diseño
                modifier = Modifier.clickable { onItemClick(item) } // Detecta el click
            ) {
                CompanionItem(
                    name = item.name,
                    rating = item.rating,
                    distance = item.distance
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CompanionItem(name: String, rating: Int, distance: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFEFEF)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Outlined.Image, contentDescription = null, tint = Color.Black) }

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

/* ---------------- Barra inferior ---------------- */

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
            selected = true,
            onClick = onAdd,
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = stringResource(R.string.barra_agregar)) },
            label = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CompanionsScreenPreview() {
    val nav = rememberNavController()
    CompanionsScreen(navController = nav)
}