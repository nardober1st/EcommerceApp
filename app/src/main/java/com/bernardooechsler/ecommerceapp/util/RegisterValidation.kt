package com.bernardooechsler.ecommerceapp.util

sealed class RegisterValidation() {
    object Success : RegisterValidation()
    data class Failed(val message: String) : RegisterValidation()
}

data class RegisterFieldsState(
    val allFields: RegisterValidation,
    val email: RegisterValidation,
    val password: RegisterValidation,
    val firsName: RegisterValidation,
    val lastName: RegisterValidation
)

data class LoginFieldsState(
    val email: RegisterValidation,
    val password: RegisterValidation
)

