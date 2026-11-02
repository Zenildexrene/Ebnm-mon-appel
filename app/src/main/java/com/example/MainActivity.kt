package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.*
import com.example.ui.HomeScreen
import com.example.ui.ReaderScreen
import com.example.ui.dialogs.AboutDialog
import com.example.ui.dialogs.ChapterListModal
import com.example.ui.dialogs.SearchModal
import com.example.ui.dialogs.TextEditorModal
import com.example.ui.theme.MyApplicationTheme

enum class Screen {
    HOME,
    READER
}

enum class ActiveDialog {
    NONE,
    CHAPTERS,
    SEARCH,
    ABOUT,
    EDITOR
}

class MainActivity : ComponentActivity() {

    private lateinit var repository: ReaderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        repository = ReaderRepository(applicationContext)

        setContent {
            MyApplicationTheme {
                AppContent(repository = repository)
            }
        }
    }
}

@Composable
fun AppContent(repository: ReaderRepository) {
    var screen by remember { mutableStateOf(Screen.HOME) }
    var dialogState by remember { mutableStateOf(ActiveDialog.NONE) }

    var chapters by remember { mutableStateOf(repository.getChapters()) }
    var currentChapterIndex by remember { mutableStateOf(repository.getLastChapter().coerceIn(0, (chapters.size - 1).coerceAtLeast(0))) }
    var currentThemeIndex by remember { mutableStateOf(repository.getThemeIndex()) }
    var fontSize by remember { mutableStateOf(repository.getFontSize()) }
    var useSerif by remember { mutableStateOf(repository.getUseSerif()) }
    var bookmarks by remember { mutableStateOf(repository.getBookmarks()) }
    var scrollY by remember { mutableStateOf(repository.getLastScrollY()) }

    val currentPalette = readerPalettes[currentThemeIndex.coerceIn(0, readerPalettes.size - 1)]

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = currentPalette.bg
    ) {
        Crossfade(targetState = screen, label = "ScreenTransition") { currentScreen ->
            when (currentScreen) {
                Screen.HOME -> {
                    HomeScreen(
                        palette = currentPalette,
                        hasProgress = repository.hasProgress(),
                        currentThemeIndex = currentThemeIndex,
                        onStartReading = {
                            if (repository.hasProgress()) {
                                currentChapterIndex = repository.getLastChapter().coerceIn(0, (chapters.size - 1).coerceAtLeast(0))
                                scrollY = repository.getLastScrollY()
                            } else {
                                currentChapterIndex = 0
                                scrollY = 0
                            }
                            screen = Screen.READER
                        },
                        onOpenChapters = { dialogState = ActiveDialog.CHAPTERS },
                        onOpenSearch = { dialogState = ActiveDialog.SEARCH },
                        onOpenEditor = { dialogState = ActiveDialog.EDITOR },
                        onOpenAbout = { dialogState = ActiveDialog.ABOUT },
                        onChangeTheme = { newThemeIdx ->
                            currentThemeIndex = newThemeIdx
                            repository.setThemeIndex(newThemeIdx)
                        }
                    )
                }

                Screen.READER -> {
                    val isBookmarked = bookmarks.contains("ch_$currentChapterIndex")

                    ReaderScreen(
                        chapters = chapters,
                        chapterIndex = currentChapterIndex,
                        initialScrollY = scrollY,
                        fontSize = fontSize,
                        useSerif = useSerif,
                        isBookmarked = isBookmarked,
                        palette = currentPalette,
                        onBackToHome = {
                            repository.setLastChapter(currentChapterIndex)
                            screen = Screen.HOME
                        },
                        onSelectChapter = { index ->
                            currentChapterIndex = index
                            scrollY = 0
                            repository.setLastChapter(index)
                            repository.setLastScrollY(0)
                        },
                        onFontSizeChange = { newSize ->
                            fontSize = newSize
                            repository.setFontSize(newSize)
                        },
                        onFontFamilyToggle = {
                            useSerif = !useSerif
                            repository.setUseSerif(useSerif)
                        },
                        onCycleTheme = {
                            currentThemeIndex = (currentThemeIndex + 1) % readerPalettes.size
                            repository.setThemeIndex(currentThemeIndex)
                        },
                        onToggleBookmark = {
                            repository.toggleBookmark(currentChapterIndex)
                            bookmarks = repository.getBookmarks()
                        },
                        onOpenChapters = { dialogState = ActiveDialog.CHAPTERS },
                        onOpenSearch = { dialogState = ActiveDialog.SEARCH },
                        onSaveScrollY = { newScrollY ->
                            scrollY = newScrollY
                            repository.setLastScrollY(newScrollY)
                        }
                    )
                }
            }
        }

        // Active Dialogs / Bottom Sheets
        when (dialogState) {
            ActiveDialog.CHAPTERS -> {
                ChapterListModal(
                    chapters = chapters,
                    currentChapterIndex = currentChapterIndex,
                    bookmarks = bookmarks,
                    palette = currentPalette,
                    onSelectChapter = { index ->
                        currentChapterIndex = index
                        scrollY = 0
                        repository.setLastChapter(index)
                        repository.setLastScrollY(0)
                        if (screen == Screen.HOME) {
                            screen = Screen.READER
                        }
                    },
                    onToggleBookmark = { index ->
                        repository.toggleBookmark(index)
                        bookmarks = repository.getBookmarks()
                    },
                    onDismiss = { dialogState = ActiveDialog.NONE }
                )
            }

            ActiveDialog.SEARCH -> {
                SearchModal(
                    palette = currentPalette,
                    onSearch = { query -> repository.search(query) },
                    onSelectResult = { chIdx, _ ->
                        currentChapterIndex = chIdx
                        scrollY = 0
                        repository.setLastChapter(chIdx)
                        repository.setLastScrollY(0)
                        screen = Screen.READER
                    },
                    onDismiss = { dialogState = ActiveDialog.NONE }
                )
            }

            ActiveDialog.ABOUT -> {
                AboutDialog(
                    palette = currentPalette,
                    onDismiss = { dialogState = ActiveDialog.NONE }
                )
            }

            ActiveDialog.EDITOR -> {
                TextEditorModal(
                    palette = currentPalette,
                    initialText = repository.loadRawText(),
                    onSaveText = { newText ->
                        repository.saveRawText(newText)
                        chapters = repository.getChapters()
                        currentChapterIndex = 0
                        scrollY = 0
                    },
                    onResetDefault = {
                        repository.resetToDefaultText()
                        chapters = repository.getChapters()
                        currentChapterIndex = 0
                        scrollY = 0
                    },
                    onDismiss = { dialogState = ActiveDialog.NONE }
                )
            }

            ActiveDialog.NONE -> { /* No dialog active */ }
        }
    }
}
