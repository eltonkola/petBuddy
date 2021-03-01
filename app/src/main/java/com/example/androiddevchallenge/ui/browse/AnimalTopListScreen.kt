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
package com.example.androiddevchallenge.ui.browse

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.MainViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme

@Composable
fun AnimalTopListScreen(viewModel: MainViewModel, onDetailsClick: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PetBuddy") },
//                actions = {
//
//
//
//                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        },
        content = {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),

            ) {

                items(viewModel.topAnimalList) { pet ->
                    pet?.let {
                        AnimalRow(it) {
                            viewModel.onAnimalClick(it)
                            onDetailsClick()
                        }
                        Spacer(Modifier.size(8.dp))
                    }
                }
            }
        },

    )
}

@Preview("Light Theme")
@Composable
fun AnimalTopListScreenLightPreview() {
    MyTheme() {
        AnimalTopListScreen(MainViewModel()) {}
    }
}

@Preview("Dark Theme")
@Composable
fun AnimalTopListScreenDarkPreview() {
    MyTheme(darkTheme = true) {
        AnimalTopListScreen(MainViewModel()) {}
    }
}
