package com.app.journeylink.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme
import com.google.firebase.firestore.FirebaseFirestore

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfirmScreenPreview() {
    JourneyLinkTheme {
        ConfirmScreen(navController = rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScreen(
    onConfirmarPagar: () -> Unit = {},
    viajeData: ViajeData = ViajeData(),
    navController: NavController // Datos por defecto
) {
    // Estado local con los datos del viaje
    var viajeState by remember { mutableStateOf(viajeData) }
    var relacionSeleccionada by remember { mutableStateOf(viajeState.opcionesRelacion[0]) }

    val isPreview = LocalInspectionMode.current
    val db = remember { FirebaseFirestore.getInstance() }

    // Leemos Firestore solo cuando no es preview
    LaunchedEffect(isPreview) {
        if (!isPreview) {
            db.collection("Viajes")
                .document("viaje")
                .get()
                .addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        val origen = doc.getString("origen") ?: viajeState.origen
                        val destino = doc.getString("destino") ?: viajeState.destino
                        val empresa = doc.getString("empresa") ?: viajeState.aerolinea
                        val precio = doc.getString("precio") ?: viajeState.total
                        val fechaPartida = doc.getString("fechaPartida")
                        val fechaRegreso = doc.getString("fechaRegreso")

                        val fechaDisplay =
                            if (!fechaPartida.isNullOrEmpty() && !fechaRegreso.isNullOrEmpty()) {
                                "$fechaPartida - $fechaRegreso"
                            } else {
                                viajeState.fecha
                            }

                        val nombreViaje = "Viaje a $destino"

                        viajeState = viajeState.copy(
                            origen = origen,
                            destino = destino,
                            aerolinea = empresa,
                            total = precio,
                            fecha = fechaDisplay,
                            nombreViaje = nombreViaje
                        )
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "Imagen del viaje",
            modifier = Modifier
                .size(139.dp, 148.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título del viaje
        OutlinedTextField(
            value = viajeState.nombreViaje,
            onValueChange = { },
            label = { Text(stringResource(R.string.conf_nomviaje)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 24.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Fecha
        OutlinedTextField(
            value = viajeState.fecha,
            onValueChange = { },
            label = { Text(stringResource(R.string.conf_fecha)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 24.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Origen y Destino
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viajeState.origen,
                onValueChange = { },
                label = { Text(stringResource(R.string.conf_origen)) },
                readOnly = true,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )

            OutlinedTextField(
                value = viajeState.destino,
                onValueChange = { },
                label = { Text(stringResource(R.string.conf_destino)) },
                readOnly = true,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Horas de salida y llegada
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viajeState.horaSalida,
                onValueChange = { },
                label = { Text(stringResource(R.string.conf_salida)) },
                readOnly = true,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )

            OutlinedTextField(
                value = viajeState.horaLlegada,
                onValueChange = { },
                label = { Text(stringResource(R.string.conf_llegada)) },
                readOnly = true,
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Aerolínea y Duración
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = viajeState.aerolinea,
                onValueChange = { },
                modifier = Modifier.background(Color.White),
                label = { Text(stringResource(R.string.conf_linea)) },
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )

            OutlinedTextField(
                value = viajeState.duracion,
                onValueChange = { },
                label = { Text(stringResource(R.string.conf_duracion)) },
                readOnly = true,
                modifier = Modifier.background(Color.White),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Acompañante
        Text(
            text = stringResource(R.string.conf_acomp),
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start),
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = viajeState.acompanante,
            onValueChange = { },
            label = { Text(stringResource(R.string.conf_nomacomp)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // FILA CON TOTAL A PAGAR Y BOTÓN
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Columna para el total a pagar
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.conf_total),
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold

                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = "$${viajeState.total} MXN",
                        onValueChange = { },
                        label = { Text("Total") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 15.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    )

                }

                Spacer(modifier = Modifier.width(16.dp))

                // Botón de confirmar/pagar
                Button(
                    onClick = {
                        onConfirmarPagar()
                        navController.navigate("Pago")

                    },
                    modifier = Modifier
                        .height(56.dp)
                        .width(150.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.conf_confirmar),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Data class para los datos del viaje
data class ViajeData(
    val nombreViaje: String = "Viaje a Paris",
    val fecha: String = "7-20 Septiembre 2025",
    val origen: String = "Aguascalientes",
    val destino: String = "Paris",
    val horaSalida: String = "8:00 am",
    val horaLlegada: String = "7:30 pm",
    val aerolinea: String = "AeroMexico",
    val duracion: String = "6h",
    val acompanante: String = "Jose Gonzales",
    val total: String = "30500",
    val opcionesRelacion: List<String> = listOf("Relación", "Familiar", "Amigo", "Colega")
)
