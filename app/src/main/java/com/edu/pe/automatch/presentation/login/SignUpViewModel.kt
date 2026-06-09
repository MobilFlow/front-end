package com.edu.pe.automatch.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.edu.pe.automatch.domain.repository.UserRepository
import com.edu.pe.automatch.presentation.navigation.UiState

class SignUpViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val state = MutableStateFlow(UiState())

    fun signUp(
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String,
        role: String
    ) {
        viewModelScope.launch {

            state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                userRepository.signUp(
                    email = email,
                    password = password,
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    role = role
                )

                // LOGIN AUTOMÁTICO
                val user = userRepository.signIn(
                    email = email,
                    password = password
                )

                state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        isMechanic = user.role == "ROLE_MECHANIC"
                    )
                }

            } catch (e: Exception) {

                state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }

            }
        }
    }
}