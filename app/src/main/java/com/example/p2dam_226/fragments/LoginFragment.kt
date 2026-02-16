package com.example.p2dam_226.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.p2dam_226.R
import com.example.p2dam_226.databinding.FragmentLoginBinding
import com.example.p2dam_226.viewmodels.AuthViewModel
import com.example.p2dam_226.viewmodels.AuthViewModelFactory
import com.example.p2dam_226.viewmodels.UserUiState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels { AuthViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextWatchers()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isFormValid.collect { isValid ->
                        binding.buttonLogin.isEnabled = isValid
                    }
                }
                launch {
                    viewModel.userUiState.collect { state ->
                        when(state) {
                            is UserUiState.Authenticated -> {
                                val options = androidx.navigation.NavOptions.Builder()
                                    .setPopUpTo(R.id.loginFragment, true)
                                    .build()
                                findNavController().navigate(R.id.tabFragment, null, options)
                            }
                            is UserUiState.Error -> {
                                binding.textInputLayoutUsername.error = state.message
                                binding.textInputLayoutPassword.error = state.message
                                // Show snackbar or similar if needed
                                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
                                viewModel.resetState()
                            }
                            is UserUiState.Loading -> {
                                binding.buttonLogin.isEnabled = false
                            }
                            is UserUiState.Idle -> {
                                // Do nothing
                            }
                        }
                    }
                }
            }
        }

        binding.buttonLogin.setOnClickListener {
            viewModel.login()
        }

        binding.buttonLoginGoogle.setOnClickListener {
            Snackbar.make(binding.root, getString(R.string.google_login_not_implemented), Snackbar.LENGTH_LONG).show()
        }

        binding.textCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupTextWatchers() {
        binding.textInputEditUsername.doAfterTextChanged { editable ->
            viewModel.onUsernameChanged(editable?.toString().orEmpty())
            binding.textInputLayoutUsername.error = null
        }
        binding.textInputEditPassword.doAfterTextChanged { editable ->
            viewModel.onPasswordChanged(editable?.toString().orEmpty())
            binding.textInputLayoutPassword.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
