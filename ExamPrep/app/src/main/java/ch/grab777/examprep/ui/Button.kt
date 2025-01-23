package ch.grab777.examprep.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String = "Button",
    onClick: () -> Unit,
    enabled: Boolean = true,
    active: Boolean = false,
) {
    val color = if (active) Color(0xffff0000) else Color(0xffbbbbbb)
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(10)
    ) {
        Text(text = text)
    }
}