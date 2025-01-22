package ch.grab777.examprep.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import ch.grab777.examprep.Question
import com.beust.klaxon.Klaxon
import java.io.BufferedReader
import java.io.InputStreamReader

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private var questions = mutableStateListOf<Question>()
    private var jsonString: String = ""
    private val fileName = "questions.json"
    var currentQuestionIndex = mutableIntStateOf(0)
    var mistakes = mutableIntStateOf(0)
    var selectedAnswer = mutableStateOf("")
    val openAlertDialog = mutableStateOf(false)
    private val answeredQuestions = mutableSetOf<Int>()


    init {
        loadPersistedState()
        loadJson()
        parseJson()
        scrambleQuestionsAndAnswers()
    }

    fun loadPersistedState() {
        val context = getApplication<Application>().applicationContext
        val settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        currentQuestionIndex.intValue = settings.getInt("CURRENT_QUESTION_INDEX", 0)
        mistakes.intValue = settings.getInt("MISTAKES", 0)
        val questions = settings.getStringSet("ANSWERED_QUESTIONS", mutableSetOf<String>())
        if (questions?.isEmpty() == false) {
            answeredQuestions.addAll(questions.map { string -> Integer.parseInt(string) })
        }
    }

    private fun loadJson() {
        val context = getApplication<Application>().applicationContext
        val file = context.assets.open(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(file))
        jsonString = bufferedReader.readText()
    }

    private fun parseJson() {
        val parsed = Klaxon().parseArray<Question>(jsonString)?.filter { question ->
            var validQuestion = true
            for (answeredQuestion in answeredQuestions) {
                if (question.id == answeredQuestion) {
                    validQuestion = false
                }
            }
            validQuestion
        }
        Log.i("Questions loaded", parsed?.size.toString())
        questions.addAll(parsed!!)
    }

    private fun scrambleQuestionsAndAnswers() {
        questions.shuffle()
        for (question in questions) {
            question.a = question.a.shuffled()
        }
    }

    fun getCurrentQuestion(): String {
        return questions[currentQuestionIndex.intValue].q
    }

    fun getAnswerTexts(): List<String> {
        return questions[currentQuestionIndex.intValue].a.map { answer -> answer.text }
    }

    fun checkAnswer(navController: NavController) {
        if (selectedAnswer.value == "") {
            openAlertDialog.value = true
            return
        }
        val correctAnswer =
            questions[currentQuestionIndex.intValue].a
                .filter { answer -> answer.isCorrect }[0].text
        if (selectedAnswer.value == correctAnswer) {
            answeredQuestions.add(questions[currentQuestionIndex.intValue].id ?: 0)
            persist()
            navController.navigate("success")
            currentQuestionIndex.intValue += 1
            selectedAnswer.value = ""
        } else {
            mistakes.intValue += 1
            persist()
        }
    }

    private fun persist() {
        val context = getApplication<Application>().applicationContext
        val settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putInt("CURRENT_QUESTION_INDEX", currentQuestionIndex.intValue)
        editor.putInt("MISTAKES", mistakes.intValue)
        editor.putStringSet(
            "ANSWERED_QUESTIONS",
            answeredQuestions.map { question -> question.toString() }.toSet()
        )
        editor.apply()
    }
}