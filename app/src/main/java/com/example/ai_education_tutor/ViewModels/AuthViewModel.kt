package com.example.ai_education_tutor.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_education_tutor.authService.authService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val authservice = authService()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun performLogin(email: String, password: String, onLoginSuccess: () -> Unit, onLoginFailure: () -> Unit){
        authservice.login(
            email = email,
            password = password,
            onSuccess = {
                viewModelScope.launch {
                    _uiEvent.emit("welcome back")
                }
                onLoginSuccess()
            },
            onFailure = {errormessage ->
                viewModelScope.launch {
                    _uiEvent.emit(errormessage)
                }
                onLoginFailure()
            }
        )

    }

    fun performSignUp(email: String,password: String, onSignUpSuccess: () -> Unit, onFailure: () -> Unit){
        authservice.signup(
            email = email,
            password = password,
            onSuccess = {
                viewModelScope.launch {
                    _uiEvent.emit("sign up successful")
                }
                onSignUpSuccess()
            },
            onFailure = {errormessage ->
                viewModelScope.launch {
                    _uiEvent.emit(errormessage)
                }
                onFailure()
            }
        )

    }

    fun perfomfogotpass(email: String, onLinkSuccess: () -> Unit, onLinkFailure: () -> Unit){
        authservice.forgotPassword(
            email = email,
            onSuccess = {
                viewModelScope.launch {
                    _uiEvent.emit("password reset email sent")
                }
                onLinkSuccess()
            },
            onFailure = {
                viewModelScope.launch {
                    _uiEvent.emit("password reset failed to send link")
                }
                onLinkFailure()
            }
        )
    }
}