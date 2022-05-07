package dev.dayaonweb.designchallenges.wavyuploadbutton

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import dev.dayaonweb.designchallenges.wavyuploadbutton.ui.theme.WavyUploadButtonTheme
import kotlinx.coroutines.newSingleThreadContext
import java.util.*
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

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
    var startTextAnimation by rememberSaveable { mutableStateOf(false) }
    var isHideAnimation by rememberSaveable { mutableStateOf(false) }

    val animatedScale by animateFloatAsState(targetValue = scale, finishedListener = { value ->
        if (value == 1.0f) {
            startTextAnimation = true
            isHideAnimation = true
        }
    })

    Box(
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
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 8.dp, horizontal = 24.dp),
    ) {
        AnimatingText(startAnimation = startTextAnimation, isHideAnimation = isHideAnimation) {
            Log.d(TAG, "WavyButton: DONE")
            startTextAnimation = false
        }
    }

}

@OptIn(ExperimentalAnimationApi::class, ExperimentalUnitApi::class)
@Composable
fun AnimatingText(
    text: String = "Upload",
    startAnimation: Boolean = false,
    isHideAnimation: Boolean = false,
    interval: Long = 1000,
    onAnimationComplete: () -> Unit
) {
    var animatedText by rememberSaveable { mutableStateOf(text) }
    AnimatedContent(targetState = animatedText,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = fadeIn(),
                initialContentExit = shrinkOut(
                    shrinkTowards = Alignment.Center,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy
                    )
                ),
                sizeTransform = null
            )
        }) { animText ->
        Text(
            text = animText,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }

    if (startAnimation) {
        if (isHideAnimation) {
            if (animatedText.trim().isEmpty()) {
                onAnimationComplete()
                animatedText = "Upload" // TODO: Remove later
                return
            }
            Timer("AnimatingText", false).schedule(timerTask {
                val lastIndex = animatedText.trim().lastIndex
                animatedText = animatedText.subSequence(0, lastIndex).toString().plus(" ")
            }, interval)
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WavyUploadButtonTheme {
        WavyButton()
    }
}