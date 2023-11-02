package com.bernardooechsler.ecommerceapp.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bernardooechsler.ecommerceapp.R
import com.bernardooechsler.ecommerceapp.data.User
import com.bernardooechsler.ecommerceapp.databinding.FragmentRegisterBinding
import com.bernardooechsler.ecommerceapp.util.RegisterValidation
import com.bernardooechsler.ecommerceapp.util.Resource
import com.bernardooechsler.ecommerceapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user = User(
                    edFirstNameRegister.text.toString().trim(),
                    edLastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim(),
                )
                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonRegisterRegister.revertAnimation()
                        Toast.makeText(context, "Account registered", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Error -> {
                        binding.buttonRegisterRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.allFields is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, validation.allFields.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edEmailRegister.apply {
                            error = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edPasswordRegister.apply {
                            error = validation.password.message
                        }
                    }
                }

                if (validation.firsName is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edFirstNameRegister.apply {
                            error = validation.firsName.message
                        }
                    }
                }

                if (validation.lastName is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edLastNameRegister.apply {
                            error = validation.lastName.message
                        }
                    }
                }
            }
        }
    }
}