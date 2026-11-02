package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

data class Chapter(
    val id: Int,
    val title: String,
    val body: String
)

sealed interface ChapterItem {
    data class TextBlock(val content: String) : ChapterItem
    data class ImageBlock(val resName: String, val caption: String) : ChapterItem
}

fun parseChapterItems(body: String): List<ChapterItem> {
    val paragraphs = body.split("\n\n")
    return paragraphs.mapNotNull { p ->
        val trimmed = p.trim()
        if (trimmed.startsWith("[IMG:") && trimmed.endsWith("]")) {
            val inner = trimmed.substring(5, trimmed.length - 1)
            val parts = inner.split("|", limit = 2)
            val resName = parts.getOrNull(0)?.trim() ?: ""
            val caption = parts.getOrNull(1)?.trim() ?: ""
            if (resName.isNotEmpty()) {
                ChapterItem.ImageBlock(resName, caption)
            } else null
        } else if (trimmed.isNotEmpty()) {
            ChapterItem.TextBlock(trimmed)
        } else null
    }
}

data class ReaderPalette(
    val id: Int,
    val name: String,
    val bg: Color,
    val fg: Color,
    val bar: Color,
    val accent: Color
)

data class SearchResult(
    val chapterIndex: Int,
    val chapterTitle: String,
    val snippet: String,
    val matchIndex: Int
)

val readerPalettes = listOf(
    ReaderPalette(0, "Nuit", NightBg, NightFg, NightBar, NightAccent),
    ReaderPalette(1, "Jour", DayBg, DayFg, DayBar, DayAccent),
    ReaderPalette(2, "Sépia", SepiaBg, SepiaFg, SepiaBar, SepiaAccent),
    ReaderPalette(3, "Sombre", DarkBg, DarkFg, DarkBar, DarkAccent)
)
