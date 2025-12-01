package com.app.journeylink.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R
import com.app.journeylink.ui.theme.JourneyLinkTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Preview(showBackground = true, showSystemUi = true, name = "Register Portrait")
@Composable
private fun RegisterPreview() {
    JourneyLinkTheme { Register(rememberNavController()) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavController) {
    // --- Estados persistentes ---
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    var langExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedLanguage by rememberSaveable { mutableStateOf(LanguageUtils.getCurrentLanguageDisplayName()) }

    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE
    val auth = Firebase.auth
    val firestore = Firebase.firestore
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Función para registrar usuario
    fun registerUser() {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, completa todos los campos"
            return
        }

        if (password.length < 6) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        isLoading = true
        errorMessage = null

        coroutineScope.launch {
            try {
                // 1. Crear usuario en Firebase Authentication
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                // 2. Crear documento en Firestore con solo los campos requeridos
                val user = authResult.user
                if (user != null) {
                    val userData = hashMapOf(
                        "email" to email,
                        "name" to username,
                        "rating" to 0.0
                    )

                    firestore.collection("usuario")
                        .document(user.uid) // Usar el UID de Auth como ID del documento
                        .set(userData)
                        .await()

                    // 3. Navegar a la siguiente pantalla después del registro exitoso
                    navController.navigate("Perfil") {
                        popUpTo("Register") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                errorMessage = when {
                    e.message?.contains("email address is already") == true ->
                        "Este correo electrónico ya está registrado"
                    e.message?.contains("badly formatted") == true ->
                        "Formato de correo electrónico inválido"
                    else -> e.message ?: "Error al registrar usuario"
                }
            } finally {
                isLoading = false
            }
        }
    }

    // --- Componentes reutilizables ---
    @Composable
    fun LanguageSwitcher() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { /* TODO: ayuda */ }, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = stringResource(R.string.login_help),
                    tint = Color.White
                )
            }
            Box {
                Button(
                    onClick = { langExpanded = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(R.string.login_lang),
                            tint = Color.Blue
                        )
                        Text(text = selectedLanguage, color = Color.Blue, fontSize = 14.sp)
                    }
                }
                DropdownMenu(expanded = langExpanded, onDismissRequest = { langExpanded = false }) {
                    DropdownMenuItem(text = { Text("Español") }, onClick = {
                        selectedLanguage = "Español"
                        langExpanded = false
                        LanguageUtils.setAppLanguage("es")
                    })
                    DropdownMenuItem(text = { Text("English") }, onClick = {
                        selectedLanguage = "English"
                        langExpanded = false
                        LanguageUtils.setAppLanguage("en")
                    })
                }
            }
        }
    }

    @Composable
    fun FormBlock(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mostrar mensaje de error
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }

            // Usuario
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = null // Limpiar error al escribir
                },
                label = { Text(stringResource(R.string.reg_user)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null // Limpiar error al escribir
                },
                label = { Text(stringResource(R.string.reg_mail)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null // Limpiar error al escribir
                },
                label = { Text(stringResource(R.string.reg_pass)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible)
                                stringResource(R.string.pass_hide)
                            else
                                stringResource(R.string.pass_show),
                            tint = Color.Blue
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Botón registrar
            Button(
                onClick = { registerUser() },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.reg_register),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Volver
            TextButton(
                onClick = {
                    if (!isLoading) {
                        navController.navigate("Login")
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
                enabled = !isLoading
            ) {
                Text(
                    text = stringResource(R.string.btn_volver),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // --- Layout raíz responsivo ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
            .padding(16.dp)
    ) {
        // Idioma arriba-derecha
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) { LanguageSwitcher() }
        }

        if (isLandscape) {
            // ===== HORIZONTAL =====
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Izquierda: logo + título
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plane),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.reg_titulo),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Derecha: formulario acotado
                FormBlock(
                    modifier = Modifier
                        .weight(1f)
                        .widthIn(max = 420.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        } else {
            // ===== VERTICAL =====
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(28.dp))
                Image(
                    painter = painterResource(id = R.drawable.plane),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 24.dp)
                )
                Text(
                    text = stringResource(R.string.reg_titulo),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                FormBlock(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}