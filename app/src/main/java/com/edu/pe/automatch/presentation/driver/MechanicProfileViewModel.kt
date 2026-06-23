package com.edu.pe.automatch.presentation.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pe.automatch.data.remote.dtos.ReputationSummaryDto
import com.edu.pe.automatch.data.remote.dtos.ReviewResponseDto
import com.edu.pe.automatch.domain.model.MechanicLocation
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.model.ServiceItem
import com.edu.pe.automatch.domain.repository.MechanicRepository
import com.edu.pe.automatch.domain.repository.ReviewRepository
import com.edu.pe.automatch.domain.repository.ServiceCatalogRepository
import com.edu.pe.automatch.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MechanicProfileUiState {
    object Loading : MechanicProfileUiState()
    data class Success(
        val mechanic: MechanicProfile,
        val fullName: String,
        val profilePicture: String? = null,
        val reputation: ReputationSummaryDto?,
        val reviews: List<ReviewResponseDto>,
        val services: List<ServiceItem> = emptyList(),
        val location: MechanicLocation? = null
    ) : MechanicProfileUiState()
    data class Error(val message: String) : MechanicProfileUiState()
    object Empty : MechanicProfileUiState()
}

class MechanicProfileViewModel(
    private val userRepository: UserRepository,
    private val mechanicRepository: MechanicRepository,
    private val reviewRepository: ReviewRepository,
    private val serviceCatalogRepository: ServiceCatalogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MechanicProfileUiState>(MechanicProfileUiState.Loading)
    val uiState: StateFlow<MechanicProfileUiState> = _uiState.asStateFlow()

    fun loadMechanicProfile(mechanicId: String) {
        viewModelScope.launch {
            _uiState.value = MechanicProfileUiState.Loading
            try {
                val mId = mechanicId.toLongOrNull() ?: run {
                    _uiState.value = MechanicProfileUiState.Error("Invalid Mechanic ID")
                    return@launch
                }

                val allMechanics = mechanicRepository.getAllMechanics()
                val mechanic = allMechanics.find { it.id == mId }

                if (mechanic == null) {
                    _uiState.value = MechanicProfileUiState.Empty
                    return@launch
                }

                val user = userRepository.getUserById(mechanic.userId)
                val reputation = reviewRepository.getReputationSummary(mId)
                val reviews = reviewRepository.getMechanicReviews(mId)
                val location = mechanicRepository.getMechanicLocation(mId)
                val services = serviceCatalogRepository.getServicesByMechanic(mId)
                    .filter { it.status?.uppercase() != "INACTIVE" }

                _uiState.value = MechanicProfileUiState.Success(
                    mechanic = mechanic,
                    fullName = user?.fullName ?: "Unknown Mechanic",
                    profilePicture = user?.profilePicture,
                    reputation = reputation,
                    reviews = reviews,
                    services = services,
                    location = location
                )
            } catch (e: Exception) {
                _uiState.value = MechanicProfileUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}
