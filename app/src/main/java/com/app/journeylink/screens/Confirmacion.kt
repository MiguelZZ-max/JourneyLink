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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme

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
    var relacionSeleccionada by remember { mutableStateOf(viajeData.opcionesRelacion[0]) }

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
            value = viajeData.nombreViaje,
            onValueChange = { },
            label = { Text("Nombre del viaje") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth() .background(Color.White),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 24.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Fecha
        OutlinedTextField(
            value = viajeData.fecha,
            onValueChange = { },
            label = { Text("Fecha") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth() .background(Color.White),
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
                value = viajeData.origen,
                onValueChange = { },
                label = { Text("Origen") },
                readOnly = true,
                modifier = Modifier.weight(1f) .background(Color.White),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )

            OutlinedTextField(
                value = viajeData.destino,
                onValueChange = { },
                label = { Text("Destino") },
                readOnly = true,
                modifier = Modifier.weight(1f) .background(Color.White),
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
                value = viajeData.horaSalida,
                onValueChange = { },
                label = { Text("Salida") },
                readOnly = true,
                modifier = Modifier.weight(1f) .background(Color.White),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )

            OutlinedTextField(
                value = viajeData.horaLlegada,
                onValueChange = { },
                label = { Text("Llegada") },
                readOnly = true,
                modifier = Modifier.weight(1f) .background(Color.White),
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
                value = viajeData.aerolinea,
                onValueChange = { },
                modifier = Modifier.background(Color.White),
                label = { Text("Aerolínea") },
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )

            OutlinedTextField(
                value = viajeData.duracion,
                onValueChange = { },
                label = { Text("Duración") },
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
            text = "Acompañante",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start),
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = viajeData.acompanante,
            onValueChange = { },
            label = { Text("Nombre del acompañante") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth() .background(Color.White),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Spinner de relación
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { }
        ) {
            OutlinedTextField(
                value = relacionSeleccionada,
                onValueChange = { },
                label = { Text("Relación") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .background(Color.White),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )

            ExposedDropdownMenu(
                expanded = false,
                onDismissRequest = { }
            ) {
                viajeData.opcionesRelacion.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            relacionSeleccionada = opcion
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                        text = "Total a Pagar:",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold

                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = "$${viajeData.total} MXN",
                        onValueChange = { },
                        label = { Text("Total") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth() .background(Color.White),
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
                        onConfirmarPagar ()
                        navController.navigate("Pago")

                              },
                    modifier = Modifier
                        .height(56.dp)
                        .width(150.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Confirmar/Pagar",
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