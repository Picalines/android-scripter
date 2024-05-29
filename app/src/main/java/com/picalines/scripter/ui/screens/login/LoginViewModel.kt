package com.picalines.scripter.ui.screens.login

import com.picalines.scripter.LOGIN_SCREEN
import com.picalines.scripter.REGISTER_SCREEN
import com.picalines.scripter.SCRIPT_LIST_SCREEN
import com.picalines.scripter.domain.service.AccountService
import com.picalines.scripter.ui.screens.ScripterAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountService: AccountService) :
    ScripterAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onLoginClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.login(email.value, password.value)
            openAndPopUp(SCRIPT_LIST_SCREEN, LOGIN_SCREEN)
        }
    }

    fun onRegisterClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(REGISTER_SCREEN, LOGIN_SCREEN)
    }
}