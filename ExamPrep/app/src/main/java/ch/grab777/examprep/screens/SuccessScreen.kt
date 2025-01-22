package ch.grab777.examprep.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ch.grab777.examprep.viewmodels.SuccessViewModel
import ch.grab777.examprep.ui.Button

@Composable
fun SuccessScreen(
    innerPadding: PaddingValues, model: SuccessViewModel, navController: NavController
) {
    Column(
        Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "😄 Success!!", fontSize = 40.sp, modifier = Modifier.padding(10.dp))
        Text(
            text = "Here's a motivational quote for you:",
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = model.currentQuote.value,
            fontSize = 24.sp,
            modifier = Modifier.padding(10.dp),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                navController.popBackStack()
                model.updateQuote()
                // model.logAllQuotes()
            }, text = "Next Question"
        )
    }
}