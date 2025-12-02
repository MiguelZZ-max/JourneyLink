package com.app.journeylink.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PagoPreview() {
    JourneyLinkTheme {
        PagoScreen(
            navController = rememberNavController(),
            viajeId = "ID_VIAJE",
            pagoId = "ID_PAGO"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    navController: NavController? = null,
    viajeId: String = "",
    pagoId: String = "",
    onPagarClick: () -> Unit = {}
) {
    // Estados para los campos del formulario
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var selectedCard by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("$0.00") }

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val isPreview = LocalInspectionMode.current
    val scope = rememberCoroutineScope()

    // 🔹 Leer datos desde Firestore SOLO cuando no es preview
    LaunchedEffect(isPreview) {
        if (isPreview) {
            loading = false
            error = null
            return@LaunchedEffect
        }

        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: return@LaunchedEffect  // si no hay usuario, salimos

            val db = Firebase.firestore
            val snap = db.collection("Pagos")
                .document(uid)           // Pagos / {uid}
                .collection("Pagos")
                .document(uid)           //       / Pagos / {uid}
                .get()
                .await()

            if (snap.exists()) {
                val anio = snap.getString("anio") ?: ""
                val mes = snap.getString("mes") ?: ""
                val monto = snap.getLong("monto") ?: 0L
                val titular = snap.getString("titular") ?: ""
                val ultimos4 = snap.getString("ultimos4") ?: ""

                cardHolder = titular
                selectedMonth = mes
                selectedYear = if (anio.length == 2) "20$anio" else anio
                cardNumber = "**** **** **** $ultimos4"
                priceText = NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(monto)
                selectedCard = "Tarjeta Crédito"
            } else {
                // si no existe aún, el usuario podrá capturar los datos
                loading = false
            }
        } catch (e: Exception) {
            error = e.message ?: "Error al cargar datos."
        } finally {
            loading = false
        }
    }

    // Estado para desplegar el menú de tarjetas
    var cardMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
    ) {
        // Contenido principal
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            // Título
            Text(
                text = stringResource(R.string.pago_titulo),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Ruta del vuelo (por ahora fija)
            Text(
                text = "Ags - Paris",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            // Precio
            Text(
                text = priceText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
            }
            error?.let {
                Text("Error: $it", color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(12.dp))
            }

            // Selección de tarjeta
            Text(
                text = stringResource(R.string.pago_tarjeta),
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Dropdown de tipo de tarjeta
            ExposedDropdownMenuBox(
                expanded = cardMenuExpanded,
                onExpandedChange = { cardMenuExpanded = !cardMenuExpanded }
            ) {
                OutlinedTextField(
                    value = if (selectedCard.isEmpty())
                        stringResource(R.string.pago_tarjetasel)
                    else
                        selectedCard,
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .background(Color.White),
                )
                ExposedDropdownMenu(
                    expanded = cardMenuExpanded,
                    onDismissRequest = { cardMenuExpanded = false }
                ) {
                    listOf("Tarjeta Crédito", "Tarjeta Débito", "Nueva Tarjeta").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedCard = option
                                cardMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Número de tarjeta
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                readOnly = false,
                placeholder = { Text(stringResource(R.string.pago_tarjetanum)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del titular
            OutlinedTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it },
                placeholder = { Text(stringResource(R.string.pago_tarjetanom)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila para fecha de expiración y CVV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mes
                OutlinedTextField(
                    value = selectedMonth,
                    onValueChange = { selectedMonth = it },
                    readOnly = false,
                    placeholder = { Text(stringResource(R.string.pago_mes)) },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White),
                )

                // Año
                OutlinedTextField(
                    value = selectedYear,
                    onValueChange = { selectedYear = it },
                    readOnly = false,
                    placeholder = { Text(stringResource(R.string.pago_ano)) },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White),
                )

                // CVV
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it.filter { ch -> ch.isDigit() }.take(3) },
                    placeholder = { Text("CVV") },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de pago → guarda en /Pagos/{uid}/Pagos/{uid}
            Button(
                onClick = {
                    if (validarCampos(cardNumber, cardHolder, selectedMonth, selectedYear, cvv)) {
                        scope.launch {
                            try {
                                loading = true

                                val uid = FirebaseAuth.getInstance().currentUser?.uid
                                    ?: return@launch

                                // Precio fijo por ahora
                                val monto = 30125L
                                val fechaPago = SimpleDateFormat(
                                    "dd 'de' MMMM yyyy, HH:mm:ss",
                                    Locale("es", "MX")
                                ).format(Date())

                                val data = mapOf(
                                    "anio" to selectedYear.takeLast(2),
                                    "mes" to selectedMonth,
                                    "monto" to monto,
                                    "titular" to cardHolder,
                                    "ultimos4" to cardNumber.takeLast(4),
                                    "fechaPago" to fechaPago
                                )

                                val db = Firebase.firestore
                                db.collection("Pagos")
                                    .document(uid)          // Pagos / {uid}
                                    .collection("Pagos")
                                    .document(uid)          //       / Pagos / {uid}
                                    .set(data)
                                    .await()

                                // Actualizamos el texto del precio (por si acaso)
                                priceText = NumberFormat.getCurrencyInstance(
                                    Locale("es", "MX")
                                ).format(monto)

                                onPagarClick()
                                navController?.navigate("Seguimiento")
                            } catch (e: Exception) {
                                error = e.message ?: "Error al guardar el pago."
                            } finally {
                                loading = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.pago_boton),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Bottom Navigation
        BottomBar(
            onMapa = { navController?.navigate("Home") },
            onAdd = { navController?.navigate("Companions") },
            onPerfil = { navController?.navigate("Perfil") }
        )
    }
}

// Función de validación
private fun validarCampos(
    cardNumber: String,
    cardHolder: String,
    month: String,
    year: String,
    cvv: String
): Boolean {
    return cardNumber.isNotBlank() &&
            cardHolder.isNotBlank() &&
            month.isNotBlank() &&
            year.isNotBlank() &&
            cvv.isNotBlank() &&
            cvv.length == 3
}

@Composable
private fun BottomBar(onMapa: () -> Unit, onAdd: () -> Unit, onPerfil: () -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 4.dp) {
        NavigationBarItem(
            selected = true, onClick = onMapa,
            icon = { Icon(Icons.Filled.Place, contentDescription = stringResource(R.string.barra_ubicacion)) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false, onClick = onAdd,
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = stringResource(R.string.barra_agregar)) },
            label = { Text("Companions") }
        )
        NavigationBarItem(
            selected = false, onClick = onPerfil,
            icon = { Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.barra_perfil)) },
            label = { Text("Perfil") }
        )
    }
}
