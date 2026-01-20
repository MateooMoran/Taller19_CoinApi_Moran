package com.example.criptoapi.data

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object FavoritesRepository {
    private val _favorites = mutableStateOf(setOf<String>())
    val favorites: State<Set<String>> get() = _favorites

    private const val PREFS_NAME = "favorites_prefs"
    private const val KEY_FAV_IDS = "fav_ids"

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY_FAV_IDS, emptySet()) ?: emptySet()
        _favorites.value = set
    }

    fun toggle(context: Context, id: String) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(id)) current.remove(id) else current.add(id)
        _favorites.value = current

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(KEY_FAV_IDS, current).apply()
    }

    fun isFavorite(id: String): Boolean = _favorites.value.contains(id)
}
