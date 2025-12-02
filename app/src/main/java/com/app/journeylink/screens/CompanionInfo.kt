package com.app.journeylink.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// 1. MODELO ACTUALIZADO: Agregamos likedBy
data class CommentData(
    val id: String = "",
    val autor: String = "",
    val contenido: String = "",
    val likes: String = "0",
    val destinatario: String = "",
    val likedBy: List<String> = emptyList() // Nueva lista de usuarios que dieron like
)

@Composable
fun CompanionInfoScreen(
    navController: NavController,
    userName: String?,
    userRating: Int?
) {
    val skyBlue = Color(0xFF87CEEB)
    val context = LocalContext.current
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    val finalName = (userName ?: "Usuario").trim()
    val finalRating = userRating ?: 0

    var commentsList by remember { mutableStateOf<List<CommentData>>(emptyList()) }
    var isLoadingComments by remember { mutableStateOf(true) }
    var newCommentText by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(finalName, refreshTrigger) {
        val db = Firebase.firestore
        db.collection("comentarios")
            .whereEqualTo("destinatario", finalName)
            .get()
            .addOnSuccessListener { result ->
                val items = result.documents.mapNotNull { doc ->
                    doc.toObject(CommentData::class.java)?.copy(id = doc.id)
                }
                commentsList = items
                isLoadingComments = false
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error cargando comentarios", e)
                isLoadingComments = false
            }
    }

    fun postComment() {
        if (newCommentText.isBlank()) return
        if (currentUser == null) {
            Toast.makeText(context, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            return
        }
        isPosting = true
        val db = Firebase.firestore
        val authorName = currentUser.email?.substringBefore("@") ?: "Anónimo"

        val newComment = hashMapOf(
            "autor" to authorName,
            "contenido" to newCommentText.trim(),
            "destinatario" to finalName,
            "likes" to "0",
            "likedBy" to listOf<String>() // Inicializamos la lista vacía
        )

        db.collection("comentarios")
            .add(newComment)
            .addOnSuccessListener {
                isPosting = false
                newCommentText = ""
                refreshTrigger++
                Toast.makeText(context, "Comentario agregado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                isPosting = false
                Toast.makeText(context, "Error al comentar", Toast.LENGTH_SHORT).show()
            }
    }

    // --- 2. LÓGICA DE LIKE PROFESIONAL ---
    fun toggleLike(comment: CommentData) {
        if (currentUser == null) {
            Toast.makeText(context, "Inicia sesión para dar like", Toast.LENGTH_SHORT).show()
            return
        }

        val db = Firebase.firestore
        val myId = currentUser.uid // Usamos el ID único del usuario

        // Verificamos si YA le di like revisando la lista
        val isLikedByMe = comment.likedBy.contains(myId)

        val currentLikesInt = comment.likes.trim().toIntOrNull() ?: 0

        if (isLikedByMe) {
            // --- UNLIKE (Quitar Like) ---
            val newCount = (currentLikesInt - 1).coerceAtLeast(0) // Evitar negativos

            db.collection("comentarios").document(comment.id)
                .update(
                    mapOf(
                        "likes" to newCount.toString(),
                        "likedBy" to FieldValue.arrayRemove(myId) // Saca mi ID de la lista
                    )
                )
                .addOnSuccessListener { refreshTrigger++ }
        } else {
            // --- LIKE (Dar Like) ---
            val newCount = currentLikesInt + 1

            db.collection("comentarios").document(comment.id)
                .update(
                    mapOf(
                        "likes" to newCount.toString(),
                        "likedBy" to FieldValue.arrayUnion(myId) // Mete mi ID a la lista
                    )
                )
                .addOnSuccessListener { refreshTrigger++ }
        }
    }

    Scaffold(
        containerColor = skyBlue,
        bottomBar = {
            JLBottomBarCompanions(
                onMapa = {
                    navController.navigate("Home") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                onPerfil = {
                    navController.navigate("Perfil") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                onAdd = {
                    navController.navigate("companions") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(skyBlue)
        ) {
            HeaderSection(
                name = finalName,
                rating = finalRating,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = stringResource(R.string.compinfo_comment),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newCommentText,
                            onValueChange = { newCommentText = it },
                            placeholder = { Text("Escribe un comentario...") },
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(
                            onClick = { postComment() },
                            enabled = !isPosting && newCommentText.isNotBlank(),
                            modifier = Modifier
                                .background(Color.Blue, RoundedCornerShape(50))
                                .size(48.dp)
                        ) {
                            if (isPosting) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White)
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0x11000000))

                    if (isLoadingComments) {
                        Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = skyBlue)
                        }
                    } else if (commentsList.isEmpty()) {
                        Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            Text("Sé el primero en comentar.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn {
                            items(commentsList) { comment ->
                                // 3. Calculamos el estado visual basado en la lista real
                                val myId = currentUser?.uid ?: ""
                                val isLikedByMe = comment.likedBy.contains(myId)

                                CommentRow(
                                    commentData = comment,
                                    isLiked = isLikedByMe,
                                    onLikeClick = { toggleLike(comment) }
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Componentes UI ---

@Composable
private fun HeaderSection(name: String, rating: Int, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(84.dp).clip(RoundedCornerShape(10.dp)).background(Color(0x33FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Image, null, tint = Color.White, modifier = Modifier.size(48.dp))
        }
        Spacer(Modifier.height(14.dp))
        Text(name, fontSize = 36.sp, fontWeight = FontWeight.Medium, color = Color.White)
        Spacer(Modifier.height(12.dp))
        RatingStars(rating = rating, size = 28.dp, tint = Color.Black)
    }
}

@Composable
private fun CommentRow(
    commentData: CommentData,
    isLiked: Boolean,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFEFEFEF)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Outlined.Image, null, tint = Color.Black) }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(commentData.autor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(commentData.contenido, fontSize = 13.sp, color = Color.Black.copy(alpha = 0.8f))
        }

        // --- ZONA DEL CORAZÓN ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onLikeClick() }
        ) {
            val icon = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
            val tint = if (isLiked) Color.Red else Color.Black

            Icon(
                imageVector = icon,
                contentDescription = "Like",
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(text = commentData.likes, fontSize = 12.sp, color = Color.Black)
        }
    }
}

@Composable
private fun RatingStars(rating: Int, outOf: Int = 5, size: Dp = 20.dp, tint: Color = Color.Black) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(outOf) { index ->
            val icon = if (index < rating) Icons.Outlined.Star else Icons.Outlined.StarBorder
            Icon(icon, null, tint = tint, modifier = Modifier.size(size))
        }
    }
}

@Composable
private fun HorizontalDivider(color: Color) {
    androidx.compose.material3.HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = color, thickness = 1.dp)
}

@Composable
private fun JLBottomBarCompanions(onMapa: () -> Unit, onAdd: () -> Unit, onPerfil: () -> Unit) {
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
private fun CompanionInfoScreenPreview() {
    val nav = rememberNavController()
    CompanionInfoScreen(navController = nav, userName = "Preview User", userRating = 4)
}