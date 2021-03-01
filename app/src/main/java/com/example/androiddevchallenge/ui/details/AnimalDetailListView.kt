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

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.data.AnimalDetailsRow
import com.example.androiddevchallenge.data.mockAnimal
import com.example.androiddevchallenge.data.toDetailList
import com.example.androiddevchallenge.ui.theme.MyTheme

@Composable
fun AnimalDetailListView(title: String, details: List<AnimalDetailsRow>) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.secondary)
                    .padding(16.dp)

            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            details.forEachIndexed { index, item ->
                UserDetailsRowView(item) { intent ->
                    context.startActivityWithPermissionCheck(intent)
                }
                if (index < details.size - 1) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(MaterialTheme.colors.secondary)
                            .alpha(0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun UserDetailsRowView(details: AnimalDetailsRow, handleIntent: (Intent) -> Unit) {

    val rowModifier = details.openIntent?.let {
        Modifier.clickable {
            handleIntent(it)
        }
    } ?: run {
        Modifier
    }

    Row(
        modifier = rowModifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = details.icon,
            contentDescription = "detail icon",
            tint = MaterialTheme.colors.secondary,
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
        )

        Text(
            text = details.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp),
            textAlign = TextAlign.Left,
        )

        details.openIntent?.let {
            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "more",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailsPreview() {
    MyTheme(darkTheme = true) {
        AnimalDetailListView("Details", mockAnimal().toDetailList())
    }
}
