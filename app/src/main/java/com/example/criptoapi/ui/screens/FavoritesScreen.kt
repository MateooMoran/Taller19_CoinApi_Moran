package com.example.criptoapi.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.criptoapi.ui.viewmodel.CryptoViewModel
import com.example.criptoapi.ui.viewmodel.CryptoUiState
import com.example.criptoapi.data.FavoritesRepository

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
