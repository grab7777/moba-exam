package ch.grab777.examprep.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ch.grab777.examprep.Cinema
import ch.grab777.examprep.R
import ch.grab777.examprep.viewmodels.CinemaViewModel
import ch.grab777.examprep.ui.Button

@Composable
fun CinemasScreen(
    innerPadding: PaddingValues,
    model: CinemaViewModel,
    navController: NavController
) {
    Column(
        Modifier
            .padding(innerPadding)
            .padding(5.dp)
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                text = "Alle",
                active = model.filterValue.value == "",
                onClick = { model.filterValue.value = ""; model.filter() })
            Spacer(modifier = Modifier.weight(0.1f))
            Button(
                text = "Zürich",
                active = model.filterValue.value == "Zürich",
                onClick = { model.filterValue.value = "Zürich"; model.filter() })
            Spacer(modifier = Modifier.weight(0.1f))
            Button(
                text = "Bern",
                active = model.filterValue.value == "Bern",
                onClick = { model.filterValue.value = "Bern"; model.filter() })
            Spacer(modifier = Modifier.weight(0.1f))
            Button(
                text = "Basel",
                active = model.filterValue.value == "Basel",
                onClick = { model.filterValue.value = "Basel"; model.filter() })
            Spacer(modifier = Modifier.weight(1f))

        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(model.cinemas) { index, cinema ->
                CinemaInfo(cinema)
            }
        }
    }


}


@Composable
fun CinemaInfo(cinema: Cinema) {
    var url = cinema.website
    val address = "${cinema.street}, ${cinema.postcode} ${cinema.city}"
    Row(
        Modifier
            .padding()
            .fillMaxWidth()

            .padding(vertical = 10.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(0.9f)
        ) {
            Text(text = cinema.name ?: "no name", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = address ?: "no address", fontStyle = FontStyle.Italic)

        }
        Modifier.clickable { }
        Column(
            Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .clickable { Log.i("Open website", "clicked") }
            /*
                            .clickable { openUrl(cinema.website)}
            */
        ) {
            Image(
                painter = painterResource(R.drawable.icon),
                contentDescription = "Go to website",
                Modifier.size(30.dp)
            )
        }

    }
}


@Composable
fun openUrl(url: String?) {
    var betterUrl = url
    if (!betterUrl.isNullOrEmpty() && !betterUrl.startsWith("http://") && !betterUrl.startsWith("https://")) {
        betterUrl = "http://$url"
    }
    if (!betterUrl.isNullOrEmpty()) {
        val uriHandler = LocalUriHandler.current
        uriHandler.openUri(betterUrl)
    }
}