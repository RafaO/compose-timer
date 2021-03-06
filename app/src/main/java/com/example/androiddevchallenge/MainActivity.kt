/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf(TextFieldValue("30")) }
    var time by remember { mutableStateOf(text.text.toInt() * 1000) }
    var running by remember { mutableStateOf(false) }
    var controlsEnabled by remember { mutableStateOf(true) }
    val height by animateDpAsState(
        if (running) 0.dp else 300.dp,
        animationSpec = tween(
            durationMillis = if (running) time else 300,
            easing = LinearEasing
        )
    )
    Surface(color = MaterialTheme.colors.background) {
        Row(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(
                    modifier = Modifier.width(100.dp),
                    value = text,
                    onValueChange = { newValue ->
                        text = newValue
                        text.text
                            .takeIf { it.isNotEmpty() }
                            ?.let { time = it.toInt() * 1000 }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true
                )
                Row {
                    Button(
                        enabled = controlsEnabled,
                        modifier = Modifier.padding(18.dp),
                        onClick = {
                            running = true
                            controlsEnabled = false
                            scope.launch {
                                while (time > 0) {
                                    delay(1000)
                                    time -= 1000
                                }
                                controlsEnabled = true
                            }
                        }
                    ) {
                        Text(stringResource(R.string.start))
                    }
                    Button(
                        enabled = controlsEnabled,
                        modifier = Modifier.padding(18.dp),
                        onClick = {
                            running = false
                            time = text.text.toInt() * 1000
                        }
                    ) { Text(stringResource(R.string.reset)) }
                }
            }
            Column(
                Modifier
                    .height(height)
                    .width(100.dp)
                    .background(MaterialTheme.colors.secondary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(LocalContext.current.getString(R.string.time, time / 1000))
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
