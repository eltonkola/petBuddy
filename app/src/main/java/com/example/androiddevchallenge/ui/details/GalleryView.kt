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
package com.example.androiddevchallenge.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.data.Animal
import com.example.androiddevchallenge.data.mockAnimal
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.jetcaster.util.Pager
import com.example.jetcaster.util.PagerState
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun GalleryView(animal: Animal) {

    val pagerState = remember { PagerState() }

    pagerState.maxPage = (animal.photos.size - 1).coerceAtLeast(0)
    Box() {

        Pager(
            state = pagerState,

            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp),
        ) {

            CoilImage(
                data = animal.photos.firstOrNull()?.large ?: "",
                contentDescription = "Image for ${animal.name}",
                loading = {
                    Box(Modifier.matchParentSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                },
                error = {
                    Image(
                        painter = painterResource(com.example.androiddevchallenge.R.drawable.no_image),
                        contentDescription = "Image for ${animal.name}"
                    )
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
        if (animal.photos.size > 1) {

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .align(alignment = Alignment.BottomStart)
            ) {
                (animal.photos.indices).forEach {
                    if (it == pagerState.currentPage) {
                        Card(
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .padding(4.dp),
                            shape = RoundedCornerShape(6.dp),
                            elevation = 2.dp,
                            backgroundColor = MaterialTheme.colors.primary,
                        ) {}
                    } else {
                        Card(
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .padding(4.dp),
                            shape = RoundedCornerShape(6.dp),
                            elevation = 2.dp,
                            backgroundColor = Color.White
                        ) {}
                    }
                }
            }
        }

        if (animal.status == "adoptable") {
            val context = LocalContext.current
            OutlinedButton(
                onClick = {
                    adopt(animal, context)
                },
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .offset((-16).dp, (-16).dp),
                shape = RoundedCornerShape(50),
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = "Adopt icon",
                        tint = MaterialTheme.colors.secondary
                    )
                    Text(
                        text = "Adopt me",
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryViewPreview() {
    MyTheme() {
        GalleryView(mockAnimal())
    }
}
