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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.data.Animal
import com.example.androiddevchallenge.data.mockAnimal
import com.example.androiddevchallenge.data.publishedDate
import com.example.androiddevchallenge.ui.theme.MyTheme

@Composable
fun AnimalFooterInfo(animal: Animal) {

    Box(
        modifier = Modifier
            // .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, bottom = 46.dp)
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier

            //    .background(Color.White)
        ) {
            Text(
                style = MaterialTheme.typography.body1,
                text = "Published on: ${animal.publishedDate()}",
                fontSize = 16.sp,
                // color = colorResource(id = R.color.grayDark),
                modifier = Modifier.padding(end = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimalFooterInfoPreview() {
    MyTheme() {
        AnimalFooterInfo(mockAnimal())
    }
}
