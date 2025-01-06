package ch.grab777.examprep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.grab777.examprep.ui.Button
import ch.grab777.examprep.ui.CustomAlertDialog
import ch.grab777.examprep.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val model: QuestionViewModel by viewModels()
                    Column(Modifier.padding(innerPadding)) {
                        Box(
                            modifier = Modifier
                                .background(Color.Gray)
                                .height(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Title(text = "Frage ${model.currentQuestionIndex.intValue + 1}")
                        }
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Question(text = model.getCurrentQuestion())
                            RadioButtonSingleSelection(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .weight(9f),
                                options = model.getAnswerTexts(),
                                model = model
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Mistakes(mistakes = model.mistakes.intValue)
                                Button(onClick = { model.checkAnswer() }, text = "Nächste Frage")
                            }
                        }
                    }
                    when {
                        model.openAlertDialog.value -> {
                            CustomAlertDialog(
                                onDismissRequest = { model.openAlertDialog.value = false },
                                dialogText = "Bitte wähle eine Antwort aus.",
                                onConfirmation = {
                                    model.openAlertDialog.value = false
                                },
                                icon = Icons.Default.Info,
                                dialogTitle = "Keine Antwort gewählt"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Mistakes(mistakes: Int = 0) {
    Text(text = "Fehler bisher: $mistakes")
}

@Composable
fun Title(text: String = "Title") {
    Text(
        text = text,
        Modifier
            .background(Color.Gray)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        color = Color.White
    )
}

@Composable
fun Question(text: String = "Question") {
    Text(text = text)
}
