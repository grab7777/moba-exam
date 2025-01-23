package ch.grab777.examprep.viewmodels

import android.app.Application
import android.util.Log

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import ch.grab777.examprep.Cinema
import ch.grab777.examprep.Nodes
import com.beust.klaxon.Klaxon
import java.io.BufferedReader
import java.io.InputStreamReader

class CinemaViewModel(application: Application) : AndroidViewModel(application) {

    private var jsonString: String = ""
    private val fileName = "cinemas.json"
    var selectedAnswer = mutableStateOf("")


    val cinemas = mutableStateListOf<Cinema>()


    init {
        loadJson()
        parseJson()
    }

    private fun loadJson() {
        val context = getApplication<Application>().applicationContext
        val file = context.assets.open(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(file))
        jsonString = bufferedReader.readText()
    }

    private fun parseJson() {
        val parsed = Klaxon().parse<Nodes>(jsonString)
        Log.i("Cinemas loaded", parsed?.nodes?.size.toString())
        cinemas.addAll(parsed!!.nodes)
    }
}