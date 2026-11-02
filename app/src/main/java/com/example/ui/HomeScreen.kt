package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ReaderPalette
import com.example.data.readerPalettes

@Composable
fun HomeScreen(
    palette: ReaderPalette,
    hasProgress: Boolean,
    currentThemeIndex: Int,
    onStartReading: () -> Unit,
    onOpenChapters: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenEditor: () -> Unit,
    onOpenAbout: () -> Unit,
    onChangeTheme: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.bg)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // KCC Logo Medallion
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(4.dp, palette.accent, CircleShape)
                        .padding(10.dp)
                        .testTag("kcc_logo_medallion")
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kcc_logo),
                        contentDescription = "Logo KCC Mon Appel",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main Title
            Text(
                text = "MON APPEL",
                fontSize = 38.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp,
                color = palette.accent,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Author Info
            Text(
                text = "DIZOLELE-MPUNGU WAFIDUSWA Isaac\nPROPHÈTE DE L’ÉTERNEL",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                color = palette.fg,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Juillet 2010",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = palette.fg.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Primary Action Button (Commencer / Reprendre)
            Button(
                onClick = onStartReading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.accent,
                    contentColor = palette.bg
                ),
                shape = RoundedCornerShape(28.dp),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_reading_button")
            ) {
                Icon(
                    imageVector = if (hasProgress) Icons.Default.PlayArrow else Icons.Default.MenuBook,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (hasProgress) "Reprendre la lecture" else "Commencer la lecture",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Secondary Pill Buttons
            OutlinedButton(
                onClick = onOpenChapters,
                border = ButtonDefaults.outlinedToolboxBorder(palette.accent),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.accent),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("chapters_button")
            ) {
                Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Table des Chapitres", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onOpenSearch,
                    border = ButtonDefaults.outlinedToolboxBorder(palette.accent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.accent),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("search_button")
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Rechercher", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedButton(
                    onClick = onOpenAbout,
                    border = ButtonDefaults.outlinedToolboxBorder(palette.accent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.accent),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("about_button")
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("À propos", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onOpenEditor,
                border = ButtonDefaults.outlinedToolboxBorder(palette.fg.copy(alpha = 0.4f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.fg.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("editor_button")
            ) {
                Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Éditer / Modifier le texte", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Theme selector chips
            Text(
                text = "Thème de lecture",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = palette.fg.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                readerPalettes.forEachIndexed { idx, p ->
                    val isSelected = idx == currentThemeIndex
                    Surface(
                        color = if (isSelected) p.accent else p.bg,
                        contentColor = if (isSelected) p.bg else p.accent,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) p.accent else p.accent.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onChangeTheme(idx) }
                            .testTag("theme_chip_$idx")
                    ) {
                        Text(
                            text = p.name,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Computer,
                    contentDescription = null,
                    tint = palette.accent,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Application développée par Zenilde Lutu",
                    fontSize = 12.sp,
                    color = palette.fg.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ButtonDefaults.outlinedToolboxBorder(color: Color) =
    androidx.compose.foundation.BorderStroke(1.5.dp, color)
