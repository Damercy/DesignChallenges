package dev.dayaonweb.designchallenges.wavyuploadbutton

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import dev.dayaonweb.designchallenges.wavyuploadbutton.ui.theme.WavyUploadButtonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WavyUploadButtonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        WavyButton()
                    }
                }
            }
        }
    }
}

private const val TAG = "MainActivity"

@OptIn(ExperimentalUnitApi::class)
@Composable
fun WavyButton() {
    var scale by rememberSaveable { mutableStateOf(1.0f) }
    val animatedScale by animateFloatAsState(targetValue = scale)

    Text(
        text = "Upload",
        color = Color.White,
        modifier = Modifier
            .scale(animatedScale)
            .clip(shape = RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { _ ->
                        Log.d(TAG, "CLICKED\tscale=$scale")
                        scale = 0.95f
                        tryAwaitRelease()
                        scale = 1.0f
                    }
                )
            }
            .background(Color.Blue)
            .padding(vertical = 8.dp, horizontal = 24.dp),

        )

}


@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
        textAlign = TextAlign.Center
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WavyUploadButtonTheme {
        WavyButton()
    }
}