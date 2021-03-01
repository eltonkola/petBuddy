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
package com.example.androiddevchallenge.ui

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.MainViewModel
import com.example.androiddevchallenge.ui.browse.AnimalTopListScreen
import com.example.androiddevchallenge.ui.details.AnimalDetailsScreen
import com.example.androiddevchallenge.ui.theme.MyTheme

sealed class AppScreens(val route: String) {
    object PetList : AppScreens("pets")
    object Favorites : AppScreens("favorites")
    object Settings : AppScreens("settings")
    object PetDetails : AppScreens("petDetails")
}

sealed class AppTabs(val icon: ImageVector, val title: String, val screen: AppScreens) {
    object Browse : AppTabs(Icons.Default.Pets, "Pets", AppScreens.PetList)
    object Favorites : AppTabs(Icons.Default.Favorite, "Favorites", AppScreens.Favorites)
    object Settings : AppTabs(Icons.Default.Settings, "Settings", AppScreens.Settings)
}

@Composable
fun MainAppScreen(viewModel: MainViewModel = MainViewModel()) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
    val tabs = listOf(AppTabs.Browse, AppTabs.Favorites, AppTabs.Settings)

    Scaffold(
        bottomBar = {
            if (currentRoute in tabs.map { it.screen.route }) {

                BottomNavigation {
                    tabs.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.screen.route,
                            alwaysShowLabel = true,
                            onClick = {
                                if (currentRoute != screen.screen.route) {
                                    navController.navigate(screen.screen.route,)
                                }
                            }
                        )
                    }
                }
            }
        },
    ) {
        NavHost(navController, startDestination = AppScreens.PetList.route) {
            composable(AppScreens.PetList.route) {
                AnimalTopListScreen(viewModel) {
                    navController.navigate(AppScreens.PetDetails.route)
                }
            }
            composable(AppScreens.Favorites.route) {
                FavoritesTabScreen(viewModel) {
                    navController.navigate(AppScreens.PetDetails.route)
                }
            }
            composable(AppScreens.Settings.route) {
                SettingsTabScreen(viewModel)
            }
            composable(AppScreens.PetDetails.route) {
                AnimalDetailsScreen(viewModel) {
                    navController.navigateUp()
                }
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun MainAppScreenLightPreview() {
    MyTheme() {
        MainAppScreen()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun MainAppScreenDarkPreview() {
    MyTheme(darkTheme = true) {
        MainAppScreen()
    }
}
