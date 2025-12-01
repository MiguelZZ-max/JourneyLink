package com.app.journeylink.screens

import android.content.pm.PackageManager
import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.ui.theme.JourneyLinkTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    JourneyLinkTheme {
        HomeScreen(navController = rememberNavController())
    }
}
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
                            stringResource(R.string.home_central),
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
                Text(text = stringResource(R.string.home_next), color = Color.White)
            }
        }
    }
}



/* ---------- Secciones UI ---------- */


@SuppressLint("MissingPermission")
@Composable
private fun MapPlaceholder() {
    val context = LocalContext.current

    // Cliente de ubicación de Google
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Ubicación por defecto (por si no hay permisos o no hay ubicación)
    val defaultLatLng = LatLng(21.8818, -102.2916) // Aguascalientes

    var myLocation by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 13f)
    }

    val hasFineLocationPermission =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    val hasCoarseLocationPermission =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    // Lanzador para pedir permisos en tiempo de ejecución
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    myLocation = latLng
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(latLng, 15f)
                }
            }
        }
    }

    // Pedir permisos / obtener ubicación
    LaunchedEffect(Unit) {
        if (!hasFineLocationPermission && !hasCoarseLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    myLocation = latLng
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(latLng, 15f)
                }
            }
        }
    }

    val mapProperties = MapProperties(
        isMyLocationEnabled = hasFineLocationPermission || hasCoarseLocationPermission
    )

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp)),
        cameraPositionState = cameraPositionState,
        properties = mapProperties
    ) {
        myLocation?.let { latLng ->
            Marker(
                state = rememberMarkerState(position = latLng),
                title = "Tu ubicación"
            )
        }
    }
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
            Text(stringResource(R.string.home_donde), maxLines = 1, overflow = TextOverflow.Ellipsis)
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
            label = { stringResource(R.string.barra_ubicacion)}
        )
        NavigationBarItem(
            selected = false, onClick = onPerfil,
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { stringResource(R.string.barra_perfil)}
        )
        NavigationBarItem(
            selected = false, onClick = onAdd,
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Agregar") },
            label = { stringResource(R.string.barra_agregar)}
        )

    }
}
