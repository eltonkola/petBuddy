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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.androiddevchallenge.MainViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme

@Composable
fun AnimalListScreen(viewModel: MainViewModel, onDetailsClick: () -> Unit) {

    val pets = viewModel.animalList.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text("petBuddy") }) },
        content = {

            Column() {

//                pets.apply {
//                    when {
//                        loadState.refresh is LoadState.Loading -> {
//                            Text(text = "Loading..")
//                        }
//                        loadState.append is LoadState.Loading -> {
//                            Text(text = "Loading..")
//                        }
//                    }
//                }

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

                    items(pets) { pet ->
                        pet?.let {
                            AnimalRow(it) {
                                viewModel.onAnimalClick(it)
                                onDetailsClick()
                            }
                            Spacer(Modifier.size(8.dp))
                        }
                    }
                }
            }
        },

    )
}

@Preview("Light Theme")
@Composable
fun AnimalListScreenLightPreview() {
    MyTheme() {
        AnimalListScreen(MainViewModel()) {}
    }
}

@Preview("Dark Theme")
@Composable
fun AnimalListScreenDarkPreview() {
    MyTheme(darkTheme = true) {
        AnimalListScreen(MainViewModel()) {}
    }
}
