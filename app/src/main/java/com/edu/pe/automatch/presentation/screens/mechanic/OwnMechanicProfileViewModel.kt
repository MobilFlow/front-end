package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pe.automatch.data.remote.dtos.ReputationSummaryDto
import com.edu.pe.automatch.data.remote.dtos.ReviewResponseDto
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.repository.MechanicRepository
import com.edu.pe.automatch.domain.repository.ReviewRepository
import com.edu.pe.automatch.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OwnMechanicProfileUiState {
    object Loading : OwnMechanicProfileUiState()
    data class Success(
        val mechanic: MechanicProfile?,
        val fullName: String,
        val reputation: ReputationSummaryDto?,
        val reviews: List<ReviewResponseDto>
    ) : OwnMechanicProfileUiState()
    data class Error(val message: String) : OwnMechanicProfileUiState()
}

class OwnMechanicProfileViewModel(
    private val userRepository: UserRepository,
    private val mechanicRepository: MechanicRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<OwnMechanicProfileUiState>(OwnMechanicProfileUiState.Loading)
    val uiState: StateFlow<OwnMechanicProfileUiState> = _uiState.asStateFlow()

    init {
        loadCurrentMechanicProfile()
    }

    fun loadCurrentMechanicProfile() {
        viewModelScope.launch {
            _uiState.value = OwnMechanicProfileUiState.Loading
            try {
                val currentUser = userRepository.getCurrentUser() ?: run {
                    _uiState.value = OwnMechanicProfileUiState.Error("No user logged in")
                    return@launch
                }

                val mechanic = mechanicRepository.getMechanicByUserId(currentUser.id)
                
                var reputation: ReputationSummaryDto? = null
                var reviews: List<ReviewResponseDto> = emptyList()

                if (mechanic != null) {
                    reputation = reviewRepository.getReputationSummary(mechanic.id)
                    reviews = reviewRepository.getMechanicReviews(mechanic.id)
                }

                _uiState.value = OwnMechanicProfileUiState.Success(
                    mechanic = mechanic,
                    fullName = currentUser.fullName,
                    reputation = reputation,
                    reviews = reviews
                )
            } catch (e: Exception) {
                _uiState.value = OwnMechanicProfileUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}
