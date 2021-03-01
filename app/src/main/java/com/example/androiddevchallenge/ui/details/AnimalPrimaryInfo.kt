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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.data.Animal
import com.example.androiddevchallenge.data.mockAnimal
import com.example.androiddevchallenge.data.nameAndDistance
import com.example.androiddevchallenge.ui.theme.MyTheme

@Composable
fun AnimalPrimaryInfo(animal: Animal) {

    Box(
        modifier = Modifier
            // .background(Color.White)
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
            //    .background(Color.White)
        ) {
            Text(
                style = MaterialTheme.typography.h5,
                text = animal.nameAndDistance(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                style = MaterialTheme.typography.body1,
                text = animal.description ?: "No description",
                fontSize = 16.sp,
                // color = colorResource(id = R.color.grayDark),
                modifier = Modifier.padding(end = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoPreview() {
    MyTheme() {
        AnimalPrimaryInfo(mockAnimal())
    }
}
