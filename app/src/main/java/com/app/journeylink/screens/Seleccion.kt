package com.app.journeylink.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeleccionScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // --------- STATE ---------
    var roundTrip by remember { mutableStateOf(true) }

    var from by remember { mutableStateOf("") }
    var to by remember { mutableStateOf("") }

    // Fechas
    val tz = remember { ZoneId.systemDefault() }
    val fmt = remember { DateTimeFormatter.ofPattern("d MMM yyyy") }

    var departDate by remember {
        mutableStateOf(LocalDate.now(tz).plusDays(20)) // ej. 5 Ene 2025 aprox
    }
    var returnDate by remember {
        mutableStateOf(departDate.plusDays(4))
    }

    var showDepartPicker by remember { mutableStateOf(false) }
    var showReturnPicker by remember { mutableStateOf(false) }

    // Viajero / Clase
    val travelersOptions = listOf("1 Adulto", "2 Adultos", "3 Adultos", "Familia")
    val classOptions = listOf("Económica", "Negocios", "Primera")

    var travelers by remember { mutableStateOf(travelersOptions.first()) }
    var travelClass by remember { mutableStateOf("Negocios") }

    var travelersExpanded by remember { mutableStateOf(false) }
    var classExpanded by remember { mutableStateOf(false) }

    // Colores corregidos
    val fondo = Color(0x0AE0E0E0)  // Fondo gris claro para toda la pantalla
    val azulBoton = Color(0xFF4A90E2) // Azul brillante para los botones
    val azulTarjeta = Color(0x0AE0E0E0) // Azul más suave para las tarjetas

    // --------- UI ---------
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Place, contentDescription = null) },
                    label = { Text("Explorar") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    label = { Text("Nuevo") },
                    selected = false,
                    onClick = {}
                )
            }
        }
    ) { inner ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(fondo) // Fondo gris aplicado a la pantalla
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Título + selector ida/ida y vuelta
            Surface(
                color = azulTarjeta,
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        "¿A dónde vas?",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        SegmentedToggle(
                            text = "Ida y vuelta",
                            selected = roundTrip,
                            onClick = { roundTrip = true },
                            modifier = Modifier.weight(1f)
                        )
                        SegmentedToggle(
                            text = "Solo ida",
                            selected = !roundTrip,
                            onClick = { roundTrip = false },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Tarjeta del formulario
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(fondo)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    LabeledField(label = "Desde") {
                        OutlinedTextField(
                            value = from,
                            onValueChange = { from = it },
                            placeholder = { Text("Ciudad de origen") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    LabeledField(label = "Hacia") {
                        Box {
                            OutlinedTextField(
                                value = to,
                                onValueChange = { to = it },
                                placeholder = { Text("Destino") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            // Botón de intercambio
                            IconButton(
                                onClick = {
                                    val tmp = from
                                    from = to
                                    to = tmp
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 6.dp)
                            ) {
                                Icon(Icons.Outlined.SwapVert, contentDescription = "Intercambiar")
                            }
                        }
                    }

                    // Partida
                    LabeledField(label = "Partida") {
                        Box {
                            OutlinedTextField(
                                value = departDate.format(fmt),
                                onValueChange = {},
                                placeholder = { Text("Selecciona la fecha de partida") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDepartPicker = true }) {
                                        Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
                                    }
                                }
                            )
                            if (showDepartPicker) {
                                val state = rememberDatePickerState(
                                    initialSelectedDateMillis = departDate.atStartOfDay(tz).toInstant().toEpochMilli()
                                )
                                DatePickerDialog(
                                    onDismissRequest = { showDepartPicker = false },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            state.selectedDateMillis?.let {
                                                departDate = Instant.ofEpochMilli(it).atZone(tz).toLocalDate()
                                                if (!departDate.isBefore(returnDate)) {
                                                    returnDate = departDate.plusDays(1)
                                                }
                                            }
                                            showDepartPicker = false
                                        }) { Text("Aceptar") }
                                    },
                                    dismissButton = { TextButton(onClick = { showDepartPicker = false }) { Text("Cancelar") } }
                                ) {
                                    DatePicker(state = state)
                                }
                            }
                        }
                    }

                    // Regreso (deshabilitar si es Solo ida)
                    LabeledField(label = "Regreso") {
                        Box {
                            OutlinedTextField(
                                value = if (roundTrip) returnDate.format(fmt) else "—",
                                onValueChange = {},
                                placeholder = { Text("Selecciona la fecha de regreso") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { if (roundTrip) showReturnPicker = true }) {
                                        Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
                                    }
                                }
                            )
                            if (showReturnPicker) {
                                val state = rememberDatePickerState(
                                    initialSelectedDateMillis = returnDate.atStartOfDay(tz).toInstant().toEpochMilli()
                                )
                                DatePickerDialog(
                                    onDismissRequest = { showReturnPicker = false },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            state.selectedDateMillis?.let {
                                                returnDate = Instant.ofEpochMilli(it).atZone(tz).toLocalDate()
                                                if (returnDate.isBefore(departDate)) {
                                                    returnDate = departDate.plusDays(1)
                                                }
                                            }
                                            showReturnPicker = false
                                        }) { Text("Aceptar") }
                                    },
                                    dismissButton = { TextButton(onClick = { showReturnPicker = false }) { Text("Cancelar") } }
                                ) {
                                    DatePicker(state = state)
                                }
                            }
                        }
                    }

                    // Fila Viajero / Clase
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Viajero
                        ExposedDropdownMenuBox(
                            expanded = travelersExpanded,
                            onExpandedChange = { travelersExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                value = travelers,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Viajero") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = travelersExpanded) }
                            )
                            ExposedDropdownMenu(
                                expanded = travelersExpanded,
                                onDismissRequest = { travelersExpanded = false }
                            ) {
                                travelersOptions.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            travelers = it
                                            travelersExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Clase
                        ExposedDropdownMenuBox(
                            expanded = classExpanded,
                            onExpandedChange = { classExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                value = travelClass,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Clase") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) }
                            )
                            ExposedDropdownMenu(
                                expanded = classExpanded,
                                onDismissRequest = { classExpanded = false }
                            ) {
                                classOptions.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            travelClass = it
                                            classExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Botón Buscar
            Button(
                onClick = {
                    navController.navigate("Confirmacion")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = azulBoton)
            ) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Buscar", color = Color.White)
            }

        }
    }

    // --------- DATE PICKERS ---------
    if (showDepartPicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = departDate.atStartOfDay(tz).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDepartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        departDate = Instant.ofEpochMilli(it).atZone(tz).toLocalDate()
                        if (!departDate.isBefore(returnDate)) {
                            returnDate = departDate.plusDays(1)
                        }
                    }
                    showDepartPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton({ showDepartPicker = false }) { Text("Cancelar") } }
        ) {
            DatePicker(state = state)
        }
    }

    if (showReturnPicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = returnDate.atStartOfDay(tz).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showReturnPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        returnDate = Instant.ofEpochMilli(it).atZone(tz).toLocalDate()
                        if (returnDate.isBefore(departDate)) {
                            returnDate = departDate.plusDays(1)
                        }
                    }
                    showReturnPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton({ showReturnPicker = false }) { Text("Cancelar") } }
        ) {
            DatePicker(state = state)
        }
    }
}

// --------- HELPERS ---------

@Composable
private fun SegmentedToggle(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val azulBoton = Color(0xFF4A90E2)
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) azulBoton else colors.surface,
        tonalElevation = if (selected) 2.dp else 0.dp,
        border = if (selected) null else ButtonDefaults.outlinedButtonBorder,
        modifier = modifier.height(36.dp)
    ) {
        Box(Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
            Text(
                text,
                color = if (selected) colors.onPrimary else colors.onSurface
            )
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold)
        content()
    }
}

@Composable
private fun ReadonlyPicker(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val bg = MaterialTheme.colorScheme.surface
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Text(text)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun FlightSearchPreview() {
    MaterialTheme {
        SeleccionScreen(navController = rememberNavController())
    }
}
