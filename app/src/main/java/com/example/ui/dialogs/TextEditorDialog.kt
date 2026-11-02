package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ReaderPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditorModal(
    palette: ReaderPalette,
    initialText: String,
    onSaveText: (String) -> Unit,
    onResetDefault: () -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = palette.bar,
        contentColor = palette.fg
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        tint = palette.accent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Éditer le texte (mon_appel.txt)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.accent
                    )
                }
                TextButton(onClick = {
                    onResetDefault()
                    onDismiss()
                }) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Réinitialiser",
                        tint = palette.fg.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Réinitialiser",
                        color = palette.fg.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                text = "Un titre commençant par '# ' crée un nouveau chapitre. Les lignes vides séparent les paragraphes.",
                fontSize = 12.sp,
                color = palette.fg.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    color = palette.fg
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = palette.accent,
                    unfocusedBorderColor = palette.fg.copy(alpha = 0.3f),
                    focusedContainerColor = palette.bg,
                    unfocusedContainerColor = palette.bg
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.fg)
                ) {
                    Text("Annuler")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        onSaveText(text)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = palette.accent,
                        contentColor = palette.bg
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Enregistrer", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
