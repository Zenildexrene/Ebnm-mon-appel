package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Chapter
import com.example.data.ChapterItem
import com.example.data.ReaderPalette
import com.example.data.parseChapterItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    chapters: List<Chapter>,
    chapterIndex: Int,
    initialScrollY: Int,
    fontSize: Float,
    useSerif: Boolean,
    isBookmarked: Boolean,
    palette: ReaderPalette,
    onBackToHome: () -> Unit,
    onSelectChapter: (Int) -> Unit,
    onFontSizeChange: (Float) -> Unit,
    onFontFamilyToggle: () -> Unit,
    onCycleTheme: () -> Unit,
    onToggleBookmark: () -> Unit,
    onOpenChapters: () -> Unit,
    onOpenSearch: () -> Unit,
    onSaveScrollY: (Int) -> Unit
) {
    val currentChapter = chapters.getOrElse(chapterIndex) { chapters.first() }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val items = remember(currentChapter.body) { parseChapterItems(currentChapter.body) }

    // Restore scroll position when entering chapter
    LaunchedEffect(chapterIndex) {
        if (initialScrollY > 0) {
            scrollState.scrollTo(initialScrollY)
        } else {
            scrollState.scrollTo(0)
        }
    }

    // Save scroll position when scroll changes
    LaunchedEffect(scrollState.value) {
        onSaveScrollY(scrollState.value)
    }

    // Intercept back button to return to Home
    BackHandler {
        onSaveScrollY(scrollState.value)
        onBackToHome()
    }

    Scaffold(
        containerColor = palette.bg,
        topBar = {
            Column {
                Surface(
                    color = palette.bar,
                    contentColor = palette.accent,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 4.dp)
                    ) {
                        IconButton(
                            onClick = {
                                onSaveScrollY(scrollState.value)
                                onBackToHome()
                            },
                            modifier = Modifier.testTag("reader_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Retour à l'accueil",
                                tint = palette.accent
                            )
                        }

                        Text(
                            text = currentChapter.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.accent,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                        )

                        // Font size A-
                        TextButton(
                            onClick = { onFontSizeChange((fontSize - 1f).coerceAtLeast(13f)) },
                            contentPadding = PaddingValues(4.dp),
                            modifier = Modifier.testTag("font_dec_button")
                        ) {
                            Text(
                                text = "A−",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.accent
                            )
                        }

                        // Font size A+
                        TextButton(
                            onClick = { onFontSizeChange((fontSize + 1f).coerceAtMost(32f)) },
                            contentPadding = PaddingValues(4.dp),
                            modifier = Modifier.testTag("font_inc_button")
                        ) {
                            Text(
                                text = "A+",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.accent
                            )
                        }

                        // Theme Toggle button (◐)
                        IconButton(
                            onClick = onCycleTheme,
                            modifier = Modifier.testTag("theme_toggle_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Contrast,
                                contentDescription = "Changer de thème",
                                tint = palette.accent,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Search
                        IconButton(
                            onClick = onOpenSearch,
                            modifier = Modifier.testTag("reader_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Rechercher",
                                tint = palette.accent,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Bookmark
                        IconButton(
                            onClick = onToggleBookmark,
                            modifier = Modifier.testTag("reader_bookmark_button")
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Favori",
                                tint = palette.accent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Progress Bar
                val progress = (chapterIndex + 1).toFloat() / chapters.size.toFloat()
                LinearProgressIndicator(
                    progress = { progress },
                    color = palette.accent,
                    trackColor = palette.bar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                )
            }
        },
        bottomBar = {
            Surface(
                color = palette.bar,
                contentColor = palette.accent,
                shadowElevation = 8.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    // Previous Chapter
                    TextButton(
                        onClick = {
                            if (chapterIndex > 0) {
                                onSelectChapter(chapterIndex - 1)
                            }
                        },
                        enabled = chapterIndex > 0,
                        modifier = Modifier.testTag("prev_chapter_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                            contentDescription = null,
                            tint = if (chapterIndex > 0) palette.accent else palette.accent.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Précédent",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (chapterIndex > 0) palette.accent else palette.accent.copy(alpha = 0.3f)
                        )
                    }

                    // Chapter List Picker
                    OutlinedButton(
                        onClick = onOpenChapters,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, palette.accent),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.accent),
                        modifier = Modifier.testTag("chapters_modal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${chapterIndex + 1}/${chapters.size} Chapitres",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Next Chapter
                    TextButton(
                        onClick = {
                            if (chapterIndex < chapters.size - 1) {
                                onSelectChapter(chapterIndex + 1)
                            }
                        },
                        enabled = chapterIndex < chapters.size - 1,
                        modifier = Modifier.testTag("next_chapter_button")
                    ) {
                        Text(
                            text = "Suivant",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (chapterIndex < chapters.size - 1) palette.accent else palette.accent.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = null,
                            tint = if (chapterIndex < chapters.size - 1) palette.accent else palette.accent.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 22.dp, vertical = 20.dp)
        ) {
            // Chapter Title Header in Reader Body
            Text(
                text = currentChapter.title,
                fontSize = (fontSize + 6f).sp,
                fontFamily = if (useSerif) FontFamily.Serif else FontFamily.Default,
                fontWeight = FontWeight.Bold,
                color = palette.accent,
                lineHeight = (fontSize + 12f).sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Render Chapter Items (Text Paragraphs & Embedded Photos)
            items.forEach { item ->
                when (item) {
                    is ChapterItem.TextBlock -> {
                        Text(
                            text = item.content,
                            fontSize = fontSize.sp,
                            fontFamily = if (useSerif) FontFamily.Serif else FontFamily.Default,
                            color = palette.fg,
                            lineHeight = (fontSize * 1.55f).sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                    }

                    is ChapterItem.ImageBlock -> {
                        val resId = remember(item.resName) {
                            context.resources.getIdentifier(item.resName, "drawable", context.packageName)
                        }
                        if (resId != 0) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = palette.bar),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = resId),
                                        contentDescription = item.caption,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                    if (item.caption.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = item.caption,
                                            fontSize = (fontSize - 3f).coerceAtLeast(12f).sp,
                                            fontStyle = FontStyle.Italic,
                                            fontWeight = FontWeight.Medium,
                                            color = palette.accent,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
