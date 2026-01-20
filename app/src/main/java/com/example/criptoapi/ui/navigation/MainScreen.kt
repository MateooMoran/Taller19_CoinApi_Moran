package com.example.criptoapi.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.criptoapi.ui.screens.CryptoDetailScreen
import com.example.criptoapi.ui.screens.CryptoListScreen
import com.example.criptoapi.ui.screens.FavoritesScreen
import com.example.criptoapi.ui.screens.TrendingScreen
import com.example.criptoapi.ui.viewmodel.CryptoDetailViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = when (currentRoute) {
        "trending" -> 1
        "crypto_list" -> 0
        "favorites" -> 2
        else -> 0
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedIndex == 0,
                    onClick = {
                        navController.navigate("crypto_list") {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("crypto_list") { saveState = true }
                        }
                    },
                    icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "Mercado") },
                    label = { Text("Mercado") },
                    alwaysShowLabel = false
                )

                NavigationBarItem(
                    selected = selectedIndex == 1,
                    onClick = {
                        navController.navigate("trending") {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("crypto_list") { saveState = true }
                        }
                    },
                    icon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Trending") },
                    label = { Text("Trending") },
                    alwaysShowLabel = false
                )

                NavigationBarItem(
                    selected = selectedIndex == 2,
                    onClick = {
                        navController.navigate("favorites") {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("crypto_list") { saveState = true }
                        }
                    },
                    icon = { Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") },
                    alwaysShowLabel = false
                )
            }
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "crypto_list") {
                composable("crypto_list") {
                    CryptoListScreen(onCryptoClick = { id ->
                        navController.navigate("detail/$id")
                    })
                }

                composable("trending") {
                    TrendingScreen(onCryptoClick = { id ->
                        navController.navigate("detail/$id")
                    })
                }

                composable("favorites") {
                    FavoritesScreen(onCryptoClick = { id ->
                        navController.navigate("detail/$id")
                    })
                }

                composable("detail/{cryptoId}") { backStackEntry ->
                    val cryptoId = backStackEntry.arguments?.getString("cryptoId") ?: return@composable
                    val detailVm: CryptoDetailViewModel = viewModel()
                    CryptoDetailScreen(
                        cryptoId = cryptoId,
                        viewModel = detailVm,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}