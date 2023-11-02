package com.bernardooechsler.ecommerceapp.util

import android.util.Patterns

fun validateAllFields(
    firstName: String,
    lastName: String,
    email: String,
    password: String
): RegisterValidation {
    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
        return RegisterValidation.Failed("Please, fill out all the fields!")
    }

    return RegisterValidation.Success
}

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("Email cannot be empty!")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format!")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Password cannot be empty!")

    if (password.length < 6)
        return RegisterValidation.Failed("Password should contain at least 6 characters!")

    return RegisterValidation.Success
}

fun validateFirstName(firstName: String): RegisterValidation {
    if (firstName.isEmpty()) {
        return RegisterValidation.Failed("First name cannot be empty!")
    }

    return RegisterValidation.Success
}

fun validateLastName(lastName: String): RegisterValidation {
    if (lastName.isEmpty()) {
        return RegisterValidation.Failed("Last name cannot be empty!")
    }

    return RegisterValidation.Success
}