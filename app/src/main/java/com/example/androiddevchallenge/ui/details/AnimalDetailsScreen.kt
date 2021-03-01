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

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.androiddevchallenge.MainViewModel
import com.example.androiddevchallenge.data.Animal
import com.example.androiddevchallenge.data.toContactList
import com.example.androiddevchallenge.data.toDetailList
import com.example.androiddevchallenge.data.toReadableAddress
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun AnimalDetailsScreen(viewModel: MainViewModel = MainViewModel(), onBackClick: () -> Unit) {

    viewModel.currentAnimal?.let { animal ->

        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    title = { Text(animal.name) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onAnimalClick(null)
                                onBackClick()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to all pets",
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingFab(animal, viewModel)
            },
            content = {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {

                    items(5) {
                        when (it) {
                            0 -> GalleryView(animal)
                            1 -> AnimalPrimaryInfo(animal)
                            2 -> AnimalDetailListView("Details", animal.toDetailList())
                            3 -> AnimalDetailListView("Contact", animal.toContactList())
                            4 -> AnimalFooterInfo(animal)
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun FloatingFab(animal: Animal, viewModel: MainViewModel) {

    var favorite by remember { mutableStateOf(viewModel.isFavorite(animal)) }

    if (favorite) {
        OutlinedButton(
            onClick = {
                viewModel.removeFromFavorites(animal)
                favorite = false
            },
            shape = RoundedCornerShape(50),
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove favorite icon",
                    tint = Color.Red

                )
                Text(
                    text = "Remove",
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    } else {
        OutlinedButton(
            onClick = {
                viewModel.addToFavorites(animal)
                favorite = true
            },
            shape = RoundedCornerShape(50),
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Add favorite icon",
                    tint = MaterialTheme.colors.secondary
                )
                Text(
                    text = "Add",
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp),
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

fun adopt(animal: Animal, context: Context) {
    val intent = animal.contact.phone?.let {
        it.phoneIntent()
    } ?: run {
        animal.contact.email?.let {
            it.emailIntent(animal.name)
        } ?: run {
            animal.contact.address.toReadableAddress().mapIntent()
        }
    }
    context.startActivityWithPermissionCheck(intent)
}

fun Context.startActivityWithPermissionCheck(intent: Intent) {
    if (intent.action != Intent.ACTION_CALL) {
        this.startActivity(intent)
        return
    }
    val fa = this as FragmentActivity
    GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
        try {
            val result = fa.askPermission(Manifest.permission.CALL_PHONE)
            if (result.isAccepted) {
                fa.startActivity(intent)
            } else {
                Toast.makeText(fa, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        } catch (e: PermissionException) {
            if (e.hasDenied()) {
                Toast.makeText(fa, "Please accept our permissions", Toast.LENGTH_SHORT).show()
                e.askAgain()
            }
            if (e.hasForeverDenied()) {
                Toast.makeText(fa, ":( a pet would make you a better person!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun String.phoneIntent(): Intent {
    return Intent(Intent.ACTION_CALL, Uri.parse("tel:$this"))
}

fun String.emailIntent(animalName: String): Intent {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, this)
    intent.putExtra(Intent.EXTRA_SUBJECT, "I want to adopt $animalName")
    return intent
}

fun String.mapIntent(): Intent {
    val uri = Uri.parse("geo:0,0?q=$this")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    return intent
}

@Preview("Light Theme")
@Composable
fun AnimalDetailsScreenLightPreview() {
    MyTheme {
        AnimalDetailsScreen(MainViewModel()) {}
    }
}

@Preview("Dark Theme")
@Composable
fun AnimalDetailsScreenDarkPreview() {
    MyTheme(darkTheme = true) {
        AnimalDetailsScreen(MainViewModel()) {}
    }
}
