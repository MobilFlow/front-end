package com.edu.pe.automatch.presentation.navigation

data class UiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val isMechanic: Boolean = false
)