package com.edu.pe.automatch.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pe.automatch.domain.repository.UserRepository
import com.edu.pe.automatch.presentation.navigation.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val state = MutableStateFlow(UiState())

    var debugUser = MutableStateFlow<String?>(null)

    fun signIn(
        email: String,
        password: String
    ) {

        viewModelScope.launch {
            try {
                val user = userRepository.signIn(
                    email = email,
                    password = password
                )

                debugUser.value = """
                    id=${user.id}
                    email=${user.email}
                    name=${user.fullName}
                    role=${user.role}
                """.trimIndent()

                state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        isMechanic = user.role == "ROLE_MECHANIC"
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()

                debugUser.value = e.message

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