package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Chapter
import com.example.data.ReaderPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListModal(
    chapters: List<Chapter>,
    currentChapterIndex: Int,
    bookmarks: Set<String>,
    palette: ReaderPalette,
    onSelectChapter: (Int) -> Unit,
    onToggleBookmark: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredChapters = remember(searchQuery, chapters) {
        if (searchQuery.isBlank()) {
            chapters.mapIndexed { idx, ch -> idx to ch }
        } else {
            chapters.mapIndexed { idx, ch -> idx to ch }
                .filter { (_, ch) ->
                    ch.title.contains(searchQuery, ignoreCase = true) ||
                            ch.body.contains(searchQuery, ignoreCase = true)
                }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = palette.bar,
        contentColor = palette.fg
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Table des Chapitres (${chapters.size})",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.accent
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = palette.fg
                    )
                }
            }

            // Search filter bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Filtrer les chapitres...", color = palette.fg.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = palette.accent) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = palette.accent,
                    unfocusedBorderColor = palette.fg.copy(alpha = 0.3f),
                    focusedTextColor = palette.fg,
                    unfocusedTextColor = palette.fg
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(filteredChapters) { _, (originalIndex, chapter) ->
                    val isSelected = originalIndex == currentChapterIndex
                    val isBookmarked = bookmarks.contains("ch_$originalIndex")

                    Surface(
                        color = if (isSelected) palette.accent.copy(alpha = 0.15f) else palette.bg,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onSelectChapter(originalIndex)
                                onDismiss()
                            }
                            .testTag("chapter_item_$originalIndex")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 14.dp)
                        ) {
                            // Chapter badge
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) palette.accent else palette.bar
                                    )
                            ) {
                                Text(
                                    text = "${originalIndex + 1}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) palette.bg else palette.accent
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = chapter.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = if (isSelected) palette.accent else palette.fg,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = chapter.body.take(60) + if (chapter.body.length > 60) "..." else "",
                                    fontSize = 12.sp,
                                    color = palette.fg.copy(alpha = 0.6f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Actuel",
                                    tint = palette.accent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            IconButton(
                                onClick = { onToggleBookmark(originalIndex) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Favori",
                                    tint = if (isBookmarked) palette.accent else palette.fg.copy(alpha = 0.4f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
