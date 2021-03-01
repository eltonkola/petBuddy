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

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.androiddevchallenge.data.Animal
import com.example.androiddevchallenge.data.PetRepository
import com.example.androiddevchallenge.data.PetSource
import kotlinx.coroutines.launch

class MainViewModel(private val petRepository: PetRepository = PetRepository()) : ViewModel() {

    var currentAnimal by mutableStateOf<Animal?>(null)
        private set

    // will try with pagination, how things are done right
    val animalList = Pager(PagingConfig(pageSize = 20)) {
        PetSource(petRepository)
    }.flow

    // pagination was to slow, lets just load a list
    var topAnimalList by mutableStateOf<List<Animal>>(emptyList())
        private set

    var darkTheme by mutableStateOf(false)
        private set

    var zipCode by mutableStateOf("11203")

    var type by mutableStateOf("dog")

    init {
        Log.v("MainViewModel", " created")
        reloadTop100()
    }

    var favorites by mutableStateOf<List<Animal>>(emptyList())
        private set

    fun onAnimalClick(animal: Animal?) {
        currentAnimal = animal
    }

    fun addToFavorites(animal: Animal) {
        favorites = favorites.toMutableList().apply {
            add(animal)
        }.toList()
    }

    fun removeFromFavorites(animal: Animal) {
        favorites = favorites.toMutableList().apply {
            remove(animal)
        }.toList()
    }

    fun isFavorite(animal: Animal): Boolean {
        return favorites.firstOrNull { it.id == animal.id } != null
    }

    fun switchTheme() {
        darkTheme = !darkTheme
    }

    fun reloadTop100() {
        viewModelScope.launch {
            topAnimalList = petRepository.getAnimals(page = 1, limit = 100, location = zipCode, type = type)
                .filter { it.photos.firstOrNull() != null }
        }
    }
}

class ViewModelFactory(private val petRepository: PetRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(petRepository) as T
    }
}
