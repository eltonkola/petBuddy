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
package com.example.androiddevchallenge.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PetSource(val repo: PetRepository) : PagingSource<Int, Animal>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Animal> {
        return try {
            val page = params.key ?: 1
            val photoResponse = repo.getAnimals(page)
            LoadResult.Page(
                data = photoResponse,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Animal>): Int? {
        return null
    }
}
