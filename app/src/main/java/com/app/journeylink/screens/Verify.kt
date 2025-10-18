package com.app.journeylink.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerifyScreenPreview() {
    JourneyLinkTheme {
        Verify(navController = rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Verify(navController: NavController) {
    // Estados para los campos de texto
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estado para el dropdown de idioma
    var languageExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("Español") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB)) // Fondo azul claro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(){
                Text(
                    text = " ",
                    color = Color.White,
                    fontSize = 30.sp
                )
            }
            // Selector de idioma
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icono de ayuda
                IconButton(
                    onClick = { },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Help,
                        contentDescription = "Ayuda",
                        tint = Color.White
                    )
                }
                // Dropdown de idioma
                Box {
                    Button(
                        onClick = { languageExpanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Seleccionar idioma",
                                tint = Color.Blue
                            )
                            Text(
                                text = selectedLanguage,
                                color = Color.Blue,
                                fontSize = 14.sp
                            )

                        }
                    }

                    DropdownMenu(
                        expanded = languageExpanded,
                        onDismissRequest = { languageExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Español") },
                            onClick = {
                                selectedLanguage = "Español"
                                languageExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("English") },
                            onClick = {
                                selectedLanguage = "English"
                                languageExpanded = false
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = " ",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Título
            Text(
                text = "Verificación de identidad",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            // Descripcion
            Text(
                text = "Por su seguridad, verifque su identidad usando reconocimiento facial",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp),
                textAlign = TextAlign.Center
            )
            // Icono
            Image(
                painter = painterResource(id = R.drawable.face),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 32.dp)
            )

            // Identificacion
            Text(
                text = "Coloque su rostro en el marco",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Su rostro no se almacenara, solo sera usado para verificar su identidad",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp),
                textAlign = TextAlign.Center
            )


            // Botón Continuar
            Button(
                onClick = {
                    // Navegar a la pantalla principal
                    navController.navigate("Home")
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Iniciar escaneo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Texto Registrarme
            TextButton(
                onClick = {
                    // Navegar a la pantalla de registro
                    navController.navigate("Login")
                },
                modifier = Modifier.padding(bottom = 40.dp)
            ) {
                Text(
                    text = "Volver",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }
}
