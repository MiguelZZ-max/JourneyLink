package com.app.journeylink.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.journeylink.R

@Composable
fun CompanionInfoScreen(navController: NavController) {
    val skyBlue = Color(0xFF87CEEB) // Celeste claro

    Scaffold(
        containerColor = skyBlue,
        bottomBar = {
            // Barrita inferior con íconos
            BottomAppBar(
                tonalElevation = 0.dp,
                containerColor = Color.White
            ) {
                Spacer(Modifier.weight(1f))
                BottomIcon(
                    icon = Icons.Outlined.Place,
                    contentDescription = "Ubicación",
                    onClick = {
                        navController.navigate("travelHome") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(Modifier.weight(1f))
                BottomIcon(
                    icon = Icons.Outlined.Person,
                    contentDescription = "Perfil",
                    onClick = {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(Modifier.weight(1f))
                BottomIcon(
                    icon = Icons.Outlined.PersonAdd,
                    contentDescription = "Agregar Acompañante",
                    onClick = {
                        navController.navigate("companions") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(Modifier.weight(1f))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(skyBlue)
        ) {
            // Header con la foto, nombre y rating
            HeaderSection(
                name = "Chespirito",
                rating = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            )

            // Panel de "Comentarios sobre el Acompañante"
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = stringResource(R.string.compinfo_comment),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(12.dp))

                    // Línea divisoria
                    HorizontalDivider(color = Color(0x11000000))

                    // Comentarios
                    CommentRow(
                        name = "Chespirito",
                        comment = stringResource(R.string.compinfo_commentej),
                        likes = "2.5k"
                    )

                    Spacer(Modifier.height(8.dp))

                    CommentRow(
                        name = "Chespirito",
                        comment = stringResource(R.string.compinfo_commentej),
                        likes = "2.5k"
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    name: String,
    rating: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto placeholder
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(14.dp))

        // Nombre
        Text(
            text = name,
            fontSize = 36.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Spacer(Modifier.height(12.dp))

        // Rating
        RatingStars(
            rating = rating,
            size = 28.dp,
            tint = Color.Black
        )
    }
}

@Composable
private fun CommentRow(
    name: String,
    comment: String,
    likes: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Foto placeholder
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFEFEF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = Color.Black
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            // Nombre
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Comentario
            Text(
                text = comment,
                fontSize = 13.sp,
                color = Color.Black.copy(alpha = 0.8f)
            )
        }

        // Coloca el ícono de corazón al lado derecho del texto
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(Modifier.width(6.dp))
            Text(likes, fontSize = 12.sp, color = Color.Black)
        }
    }
}

@Composable
private fun RatingStars(
    rating: Int,
    outOf: Int = 5,
    size: Dp = 20.dp,
    tint: Color = Color.Black
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(outOf) { index ->
            val icon = if (index < rating) Icons.Outlined.Star else Icons.Outlined.StarBorder
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(size)
            )
        }
    }
}

@Composable
private fun HorizontalDivider(color: Color) {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = color,
        thickness = 1.dp
    )
}

@Composable
private fun BottomIcon(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.Black,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CompanionInfoScreenPreview() {
    val nav = rememberNavController()
    CompanionInfoScreen(navController = nav)
}
