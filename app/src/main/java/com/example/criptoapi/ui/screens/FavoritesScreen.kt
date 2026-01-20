package com.example.criptoapi.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.font.FontWeight
import com.example.criptoapi.ui.viewmodel.CryptoViewModel
import com.example.criptoapi.ui.viewmodel.CryptoUiState
import com.example.criptoapi.data.FavoritesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: CryptoViewModel = viewModel(),
    onCryptoClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        FavoritesRepository.load(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Favoritos", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (val state = uiState) {
                is CryptoUiState.Loading -> {
                    LoadingContent()
                }
                is CryptoUiState.Success -> {
                    val favs = FavoritesRepository.favorites.value
                    val filtered = state.cryptos.filter { favs.contains(it.id) }
                    if (filtered.isEmpty()) {
                        EmptyContent()
                    } else {
                        CryptoList(cryptos = filtered, onCryptoClick = onCryptoClick)
                    }
                }
                is CryptoUiState.Error -> {
                    ErrorContent(message = state.message, onRetry = { viewModel.refresh() })
                }
            }
        }
    }
}
