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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.Animal
import com.example.androiddevchallenge.data.mockAnimal
import com.example.androiddevchallenge.data.normalizedDistance
import com.example.androiddevchallenge.ui.theme.MyTheme
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun AnimalRow(animal: Animal, onClick: () -> Unit) {

    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            CoilImage(
                data = animal.primary_photo_cropped?.large ?: "",
                contentDescription = "Image for ${animal.name}",
                loading = {
                    Box(Modifier.matchParentSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.no_image),
                        contentDescription = "Image for ${animal.name}",
                        contentScale = ContentScale.Crop
                    )
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(140.dp)
                    .height(120.dp)
            )
            Column(
                modifier = Modifier.fillMaxHeight()
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Text(
                    text = animal.name,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = animal.description ?: "No description",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    maxLines = 3,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "${animal.gender} - ${animal.age}",
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    Text(
                        text = "${animal.normalizedDistance()} miles",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview("Light Theme")
@Composable
fun AnimalRowLightPreview() {
    MyTheme {
        AnimalRow(mockAnimal()) {}
    }
}

@Preview("Dark Theme")
@Composable
fun AnimalRowdDarkPreview() {
    MyTheme(darkTheme = true) {
        AnimalRow(mockAnimal()) {}
    }
}
