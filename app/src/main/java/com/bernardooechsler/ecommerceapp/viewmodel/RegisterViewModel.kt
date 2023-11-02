package com.bernardooechsler.ecommerceapp.viewmodel

import androidx.lifecycle.ViewModel
import com.bernardooechsler.ecommerceapp.data.User
import com.bernardooechsler.ecommerceapp.util.Constants.USER_COLLECTION
import com.bernardooechsler.ecommerceapp.util.RegisterFieldsState
import com.bernardooechsler.ecommerceapp.util.RegisterValidation
import com.bernardooechsler.ecommerceapp.util.Resource
import com.bernardooechsler.ecommerceapp.util.validateAllFields
import com.bernardooechsler.ecommerceapp.util.validateEmail
import com.bernardooechsler.ecommerceapp.util.validateFirstName
import com.bernardooechsler.ecommerceapp.util.validateLastName
import com.bernardooechsler.ecommerceapp.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(
        user: User,
        password: String
    ) {
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid, user)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateAllFields(user.firstName, user.lastName, user.email, password),
                validateEmail(user.email),
                validatePassword(password),
                validateFirstName(user.firstName),
                validateLastName(user.lastName)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val allFields = validateAllFields(user.firstName, user.lastName, user.email, password)
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val firstName = validateFirstName(user.firstName)
        val lastName = validateLastName(user.lastName)

        return (emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
                && allFields is RegisterValidation.Success && firstName is RegisterValidation.Success && lastName is RegisterValidation.Success)
    }
}