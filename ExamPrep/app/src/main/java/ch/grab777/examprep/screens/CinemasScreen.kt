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
            Button(text = "Alle", active = true, onClick = { Log.i("clicked", "alle") })
            Spacer(modifier = Modifier.weight(0.1f))
            Button(text = "Zürich", onClick = { Log.i("clicked", "Zürich") })
            Spacer(modifier = Modifier.weight(0.1f))
            Button(text = "Bern", onClick = { Log.i("clicked", "Bern") })
            Spacer(modifier = Modifier.weight(0.1f))
            Button(text = "Basel", onClick = { Log.i("clicked", "Basel") })
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
            Text(text = cinema.city ?: "no address", fontStyle = FontStyle.Italic)

        }
        Modifier.clickable { }
        Column(
            Modifier
                .fillMaxWidth()
                .weight(0.1f).clickable { Log.i("Logo", "clicked") }
        ) {
            Image(
                painter = painterResource(R.drawable.icon),
                contentDescription = "Go to website",
                Modifier.size(30.dp)
            )
        }

    }
}
