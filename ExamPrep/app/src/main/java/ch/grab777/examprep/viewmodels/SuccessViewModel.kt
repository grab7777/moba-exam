package ch.grab777.examprep.viewmodels

import android.app.Application
import android.util.Log

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import ch.grab777.examprep.QuoteBacking
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SuccessViewModel(application: Application) : AndroidViewModel(application) {

    private var quotes = mutableStateListOf<Quote>()
    var currentQuote = mutableStateOf("loading ...")
    private val quotesUrl = "https://zenquotes.io/api/quotes"
    private val db = Room.databaseBuilder(
        getApplication<Application>().applicationContext,
        AppDatabase::class.java,
        "quotes"
    ).build()

    init {
        fetchFromDB()
        if (quotes.size < 400){
            fetchQuotes()
        }
    }

    private fun fetchQuotes() {
        val context = getApplication<Application>().applicationContext
        val requestQueue = Volley.newRequestQueue(context)
        val request = StringRequest(Request.Method.GET, quotesUrl,
            { response ->
                val parsed = Klaxon().parseArray<QuoteBacking>(response)

                parsed?.map { q -> Quote(quote = q.q, author = q.a) }?.let { quotes.addAll(it) }
                Log.i("response", response)
                updateQuote()
                persist()
            },
            {
                Log.e("Volley", "error loading data: $it")
            }
        )
        requestQueue.add(request)
    }

    fun updateQuote() {
        if (quotes.size == 0) {
            return
        }
        currentQuote.value = quotes[Random.nextInt(quotes.size)].quote
    }

    @Entity
    class Quote(
        @PrimaryKey(autoGenerate = true) val qid: Int = 0,
        @ColumnInfo(name = "quote") val quote: String,
        @ColumnInfo(name = "author") val author: String,
    )

    @Dao
    interface QuoteDao {
        @Query("SELECT * FROM Quote")
        fun getAll(): List<Quote>

        @Query("SELECT * FROM Quote WHERE author LIKE :author")
        fun getAllByAuthor(author: String): List<Quote>

        @Delete
        fun delete(quote: Quote)

        @Insert
        fun insertAll(quotes: List<Quote>)
    }

    @Database(
        entities = [Quote::class],
        version = 1, exportSchema = false
    )
    abstract class AppDatabase : RoomDatabase() {
        abstract fun quoteDao(): QuoteDao
    }

    private fun suspInsertAllQuotes() {
        db.quoteDao().insertAll(quotes.toList())
    }

    private fun suspLogAllQuotes() {
        val quotes = db.quoteDao().getAll()
        val quotesString = quotes.map { q -> "ID: ${q.qid} Quote: ${q.quote}" }
            .reduce { acc, string -> "$acc\n$string" }
        Log.i("quotes", quotesString)
    }

    // we have to call the DB asynchronously, to be not blocking
    // wrap execution in viewModelScope.launch(Dispatchers.IO) { here() }

    fun logAllQuotes() {
        viewModelScope.launch(Dispatchers.IO) {
            suspLogAllQuotes()
        }
    }

    private fun persist() {
        viewModelScope.launch(Dispatchers.IO) {
            db.clearAllTables()
            suspInsertAllQuotes()
        }
    }

    private fun suspFetchFromDB() {
        val quotesFromDB = db.quoteDao().getAll().toList()
        if (quotesFromDB.isNotEmpty()) {
            quotes.addAll(quotesFromDB)
        }
    }

    private fun fetchFromDB(){
        viewModelScope.launch(Dispatchers.IO){
            suspFetchFromDB()
        }
    }
}