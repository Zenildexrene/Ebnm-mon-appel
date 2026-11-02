package com.example.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ReaderPalette

@Composable
fun AboutDialog(
    palette: ReaderPalette,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = palette.bar,
        titleContentColor = palette.accent,
        textContentColor = palette.fg,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = palette.accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "À propos de Mon Appel",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Logo image / Medallion
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, palette.accent, CircleShape)
                        .padding(6.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kcc_logo),
                        contentDescription = "Logo KCC",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MON APPEL",
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = palette.accent,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "DIZOLELE-MPUNGU WAFIDUSWA Isaac\nPROPHÈTE DE L’ÉTERNEL",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = palette.fg,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = palette.bg.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Église Bon Nouveau Message (KCC)\nJuillet 2010",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = palette.accent,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "© Texte : Dizolele-Mpungu Wafiduswa Isaac. Tous droits réservés. Reproduction numérique réalisée avec autorisation.",
                            fontSize = 11.sp,
                            color = palette.fg.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = null,
                        tint = palette.accent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Application développée par Zenilde Lutu",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = palette.fg.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = "Version 1.0",
                    fontSize = 11.sp,
                    color = palette.fg.copy(alpha = 0.5f)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.accent,
                    contentColor = palette.bg
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Fermer", fontWeight = FontWeight.Bold)
            }
        }
    )
}
