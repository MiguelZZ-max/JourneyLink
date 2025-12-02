package com.app.journeylink.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MisViajesPreview() {
    JourneyLinkTheme {
        MisViajesScreen(
            navController = rememberNavController()
        )
    }
}

/**
 * Muestra el último viaje guardado en la colección:
 *  Viajes / <cualquier documento>
 * usando los campos:
 *  origen, destino, empresa, transporte,
 *  fechaPartida, fechaRegreso, precio, estado
 */
@Composable
fun MisViajesScreen(
    navController: NavController? = null,
    modifier: Modifier = Modifier
) {
    // --------- STATE DE FIRESTORE ---------
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var empresa by remember { mutableStateOf("") }
    var transporte by remember { mutableStateOf("") }
    var fechaPartida by remember { mutableStateOf("") }
    var fechaRegreso by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val isPreview = LocalInspectionMode.current

    // --------- CARGA DESDE FIRESTORE ---------
    LaunchedEffect(isPreview) {
        if (isPreview) {
            // Datos de ejemplo SOLO para el preview
            origen = "Mi ubicación actual"
            destino = "Paris"
            empresa = "AeroMexico"
            transporte = "avión"
            fechaPartida = "2025-12-22"
            fechaRegreso = "2025-12-26"
            precio = "30500"
            estado = "pendiente"
            loading = false
            return@LaunchedEffect
        }

        try {
            val db = Firebase.firestore

            // 🔹 Leer el ÚLTIMO documento de la colección raíz "Viajes"
            val snapshot = db.collection("Viajes")
                .get()
                .await()

            val doc = snapshot.documents.lastOrNull()

            if (doc != null) {
                origen       = doc.getString("origen") ?: ""
                destino      = doc.getString("destino") ?: ""
                empresa      = doc.getString("empresa") ?: ""
                transporte   = doc.getString("transporte") ?: ""
                fechaPartida = doc.getString("fechaPartida") ?: ""
                fechaRegreso = doc.getString("fechaRegreso") ?: ""
                estado       = doc.getString("estado") ?: ""

                // precio puede venir como String o como número
                precio = doc.getString("precio")
                    ?: doc.getLong("precio")?.toString()
                            ?: ""

                error = null
            } else {
                error = "No hay viajes guardados."
            }
        } catch (e: Exception) {
            error = e.message ?: "Error al cargar el viaje."
        } finally {
            loading = false
        }
    }

    // --------- UI ---------
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
    ) {
        // Título
        Text(
            text = stringResource(R.string.seg_titulo),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )

        // Contenido principal con scroll
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
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

            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
            }

            error?.let {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (!loading && error == null && destino.isNotBlank()) {

                // ----- CABECERA DEL VIAJE -----
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = destino,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        if (fechaPartida.isNotBlank()) {
                            Text(
                                text = fechaPartida,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.app_icon),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Info general del viaje
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Origen: $origen", fontSize = 14.sp)
                        Text("Destino: $destino", fontSize = 14.sp)
                        Text("Compañía: $empresa", fontSize = 14.sp)
                        Text("Transporte: $transporte", fontSize = 14.sp)
                        Text("Salida: $fechaPartida", fontSize = 14.sp)
                        Text("Regreso: $fechaRegreso", fontSize = 14.sp)
                        Text("Estado: $estado", fontSize = 14.sp)
                        if (precio.isNotBlank()) {
                            Text(
                                text = "Total: $$precio MXN",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // ----- TIMELINE SENCILLO -----
                Spacer(modifier = Modifier.height(20.dp))

                if (fechaPartida.isNotBlank()) {
                    DateHeader(text = fechaPartida)
                    TimelineEvent(
                        time = "8:00 am",
                        description = "$origen → $destino con $empresa"
                    )
                    TimelineEvent(
                        time = "9:40 am",
                        description = "Llegada a $destino"
                    )
                }

                if (fechaRegreso.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    DateHeader(text = fechaRegreso)
                    TimelineEvent(
                        time = "5:00 pm",
                        description = "$destino → $origen con $empresa"
                    )
                }
            }

            if (!loading && error == null && destino.isBlank()) {
                Text(
                    text = "No hay viajes para mostrar.",
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color.Black
                )
            }
        }

        BottomBar(
            onMapa = { navController?.navigate("Home") },
            onAdd = { navController?.navigate("Companions") },
            onPerfil = { navController?.navigate("Perfil") }
        )
    }
}

/* ---------- COMPONENTES DEL TIMELINE ---------- */

@Composable
private fun DateHeader(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(60.dp)) // espacio para la columna de horas
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun TimelineEvent(time: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = time,
            fontSize = 14.sp,
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.Right,
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .width(24.dp)
                .height(40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Red, shape = CircleShape)
            )
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = description,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = Color.Black
            )
        }
    }
}

/* ---------- BOTTOM BAR ---------- */

@Composable
private fun BottomBar(onMapa: () -> Unit, onAdd: () -> Unit, onPerfil: () -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 4.dp) {
        NavigationBarItem(
            selected = true, onClick = onMapa,
            icon = {
                Icon(
                    Icons.Filled.Place,
                    contentDescription = stringResource(R.string.barra_ubicacion)
                )
            },
            label = { Text(stringResource(R.string.barra_ubicacion)) }
        )
        NavigationBarItem(
            selected = false, onClick = onAdd,
            icon = {
                Icon(
                    Icons.Filled.AddCircle,
                    contentDescription = stringResource(R.string.barra_agregar)
                )
            },
            label = { Text(stringResource(R.string.barra_agregar)) }
        )
        NavigationBarItem(
            selected = false, onClick = onPerfil,
            icon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = stringResource(R.string.barra_perfil)
                )
            },
            label = { Text(stringResource(R.string.barra_perfil)) }
        )
    }
}
