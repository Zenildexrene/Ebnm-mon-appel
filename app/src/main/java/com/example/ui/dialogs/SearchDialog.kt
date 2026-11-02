package com.example.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ReaderPalette
import com.example.data.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchModal(
    palette: ReaderPalette,
    onSearch: (String) -> List<SearchResult>,
    onSelectResult: (chapterIndex: Int, matchIndex: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val results = remember(query) { onSearch(query) }

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
            Text(
                text = "Recherche dans l'ouvrage",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = palette.accent,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Tapez un mot ou un passage...", color = palette.fg.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = palette.accent) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Effacer", tint = palette.fg)
                        }
                    }
                },
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
                    .padding(bottom = 12.dp)
            )

            if (query.isNotEmpty()) {
                Text(
                    text = "${results.size} résultat(s) trouvé(s)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = palette.accent,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(results) { res ->
                    Surface(
                        color = palette.bg,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onSelectResult(res.chapterIndex, res.matchIndex)
                                onDismiss()
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = res.chapterTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = palette.accent
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = res.snippet,
                                fontSize = 13.sp,
                                color = palette.fg.copy(alpha = 0.85f),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
