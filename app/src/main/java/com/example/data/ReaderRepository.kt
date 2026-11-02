package com.example.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ReaderRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("mon_appel_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CUSTOM_TEXT = "custom_mon_appel_text"
        private const val KEY_THEME = "theme_index"
        private const val KEY_SIZE = "font_size"
        private const val KEY_CHAPTER = "last_chapter"
        private const val KEY_SCROLL = "last_scroll_y"
        private const val KEY_SERIF = "use_serif"
        private const val KEY_BOOKMARKS = "bookmarks_set"
    }

    fun loadRawText(): String {
        val saved = prefs.getString(KEY_CUSTOM_TEXT, null)
        if (!saved.isNullOrBlank()) {
            return saved
        }
        return try {
            context.assets.open("mon_appel.txt").bufferedReader(Charsets.UTF_8).use { it.readText() }
        } catch (e: Exception) {
            "# Mon Appel\nBienvenue dans l'application Mon Appel."
        }
    }

    fun saveRawText(text: String) {
        prefs.edit { putString(KEY_CUSTOM_TEXT, text) }
    }

    fun resetToDefaultText() {
        prefs.edit { remove(KEY_CUSTOM_TEXT) }
    }

    fun getChapters(): List<Chapter> {
        val text = loadRawText()
        val list = mutableListOf<Chapter>()
        var title = "Introduction"
        val sb = StringBuilder()
        var idCounter = 0

        fun flush() {
            val body = sb.toString().trim()
            if (body.isNotEmpty()) {
                list.add(Chapter(idCounter++, title, formatBody(body)))
            }
            sb.clear()
        }

        for (line in text.lines()) {
            if (line.trim().startsWith("# ")) {
                flush()
                title = line.trim().substring(2).trim()
            } else {
                sb.append(line).append('\n')
            }
        }
        flush()

        if (list.isEmpty()) {
            list.add(Chapter(0, "Contenu manquant", "Veuillez ajouter le texte dans mon_appel.txt."))
        }
        return list
    }

    private fun formatBody(raw: String): String {
        return raw.split(Regex("\\n\\s*\\n"))
            .map { it.replace(Regex("\\s*\\n\\s*"), " ").trim() }
            .filter { it.isNotEmpty() }
            .joinToString("\n\n")
    }

    fun search(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        val chapters = getChapters()
        val results = mutableListOf<SearchResult>()
        val lowercaseQuery = query.lowercase().trim()

        chapters.forEachIndexed { chIdx, ch ->
            val bodyLower = ch.body.lowercase()
            var startIndex = 0
            while (true) {
                val found = bodyLower.indexOf(lowercaseQuery, startIndex)
                if (found == -1) break

                val snippetStart = (found - 40).coerceAtLeast(0)
                val snippetEnd = (found + query.length + 60).coerceAtMost(ch.body.length)
                var snippet = ch.body.substring(snippetStart, snippetEnd).replace("\n", " ")
                if (snippetStart > 0) snippet = "...$snippet"
                if (snippetEnd < ch.body.length) snippet = "$snippet..."

                results.add(SearchResult(chIdx, ch.title, snippet, found))
                startIndex = found + lowercaseQuery.length + 50 // step forward to find next occurrences
            }
        }
        return results
    }

    // Preferences
    fun getThemeIndex(): Int = prefs.getInt(KEY_THEME, 0).coerceIn(0, readerPalettes.size - 1)
    fun setThemeIndex(index: Int) = prefs.edit { putInt(KEY_THEME, index) }

    fun getFontSize(): Float = prefs.getFloat(KEY_SIZE, 19f).coerceIn(12f, 34f)
    fun setFontSize(size: Float) = prefs.edit { putFloat(KEY_SIZE, size) }

    fun getLastChapter(): Int = prefs.getInt(KEY_CHAPTER, 0)
    fun setLastChapter(chapter: Int) = prefs.edit { putInt(KEY_CHAPTER, chapter) }

    fun getLastScrollY(): Int = prefs.getInt(KEY_SCROLL, 0)
    fun setLastScrollY(scrollY: Int) = prefs.edit { putInt(KEY_SCROLL, scrollY) }

    fun getUseSerif(): Boolean = prefs.getBoolean(KEY_SERIF, true)
    fun setUseSerif(serif: Boolean) = prefs.edit { putBoolean(KEY_SERIF, serif) }

    fun hasProgress(): Boolean = prefs.contains(KEY_CHAPTER)

    fun getBookmarks(): Set<String> = prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
    fun toggleBookmark(chapterIndex: Int): Boolean {
        val current = getBookmarks().toMutableSet()
        val key = "ch_$chapterIndex"
        val isAdded = if (current.contains(key)) {
            current.remove(key)
            false
        } else {
            current.add(key)
            true
        }
        prefs.edit { putStringSet(KEY_BOOKMARKS, current) }
        return isAdded
    }
}
