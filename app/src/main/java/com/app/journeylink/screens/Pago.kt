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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.ui.theme.JourneyLinkTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PagoPreview() {
    JourneyLinkTheme {
        PagoScreen(navController = rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    navController: NavController? = null,
    onPagarClick: () -> Unit = {}
) {
    // Estados para los campos del formulario
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var selectedCard by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }

    // Opciones para los spinners
    val cardOptions = listOf("Tarjeta Crédito", "Tarjeta Débito", "Nueva Tarjeta")
    val months = listOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    val years = listOf("2024", "2025", "2026", "2027", "2028", "2029", "2030")

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
                text = "Pago de Viaje",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Ruta del vuelo
            Text(
                text = "Ags - Paris",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            // Precio
            Text(
                text = "$30,125",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Selección de tarjeta
            Text(
                text = "Selecciona tu tarjeta",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Spinner de tipo de tarjeta
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { }
            ) {
                OutlinedTextField(
                    value = selectedCard.ifEmpty { "Seleccionar tarjeta" },
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor().background(Color.White),
                )

                ExposedDropdownMenu(
                    expanded = false,
                    onDismissRequest = { }
                ) {
                    cardOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedCard = option
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
                placeholder = { Text("No. Tarjeta") },
                modifier = Modifier.fillMaxWidth().background(Color.White),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del titular
            OutlinedTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it },
                placeholder = { Text("Nombre Titular") },
                modifier = Modifier.fillMaxWidth().background(Color.White),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila para fecha de expiración y CVV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Spinner de mes
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedMonth.ifEmpty { "Mes" },
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .background(Color.White),
                    )

                    ExposedDropdownMenu(
                        expanded = false,
                        onDismissRequest = { }
                    ) {
                        months.forEach { month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = month
                                }
                            )
                        }
                    }
                }

                // Spinner de año
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedYear.ifEmpty { "Año" },
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor().background(Color.White),
                    )

                    ExposedDropdownMenu(
                        expanded = false,
                        onDismissRequest = { }
                    ) {
                        years.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year) },
                                onClick = {
                                    selectedYear = year
                                }
                            )
                        }
                    }
                }

                // CVV
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    placeholder = { Text("CVV") },
                    modifier = Modifier.weight(1f).background(Color.White),
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de pago
            Button(
                onClick = {
                    // Navegar a la pantalla Seguimiento.kt
                    navController!!.navigate("Seguimiento")

                    // Validar campos antes de proceder
                    if (validarCampos(cardNumber, cardHolder, selectedMonth, selectedYear, cvv)) {
                        onPagarClick()
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
                    text = "PAGAR",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Bottom Navigation
        BottomBar(
            onMapa = { navController?.navigate("mapa") },
            onAdd = { navController?.navigate("agregar") },
            onPerfil = { navController?.navigate("perfil") }
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
