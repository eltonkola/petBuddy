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
package com.example.androiddevchallenge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.MainViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme

@Composable
fun SettingsTabScreen(viewModel: MainViewModel = MainViewModel()) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        },
        content = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
            ) {

                // change theme
                Row(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Use the dark theme",
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = viewModel.darkTheme,
                        onCheckedChange = {
                            viewModel.switchTheme()
                        },
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(MaterialTheme.colors.secondary)
                        .alpha(0.5f)
                )
                // zip code

                Row(modifier = Modifier.padding(16.dp)) {

                    TextField(
                        value = viewModel.zipCode,
                        onValueChange = {
                            viewModel.zipCode = it
                            viewModel.reloadTop100()
                        },
                        label = { Text("What is your zip code?") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(MaterialTheme.colors.secondary)
                        .alpha(0.5f)
                )

                // kind of pet
                Text(
                    text = "What are you looking for?",
                    modifier = Modifier.padding(16.dp)
                )

                val radioOptions = listOf("dog", "cat", "rabbit", "small-fully", "horse", "bird", "scales-fins-other", "barnyard")

                Column {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (text == viewModel.type),
                                    onClick = {
                                        viewModel.type = text
                                        viewModel.reloadTop100()
                                    }
                                )
                                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
                        ) {
                            RadioButton(
                                selected = (text == viewModel.type),
                                onClick = {
                                    viewModel.type = text
                                    viewModel.reloadTop100()
                                }
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.body1.merge(),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        },

    )
}

@Preview("Light Theme")
@Composable
fun SettingsTabScreenLightPreview() {
    MyTheme() {
        SettingsTabScreen(MainViewModel())
    }
}

@Preview("Dark Theme")
@Composable
fun SettingsTabScreenDarkPreview() {
    MyTheme(darkTheme = true) {
        SettingsTabScreen(MainViewModel())
    }
}
