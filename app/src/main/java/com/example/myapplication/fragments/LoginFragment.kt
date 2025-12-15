package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import androidx.core.widget.doAfterTextChanged

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextWatchers()

        viewModel.isFormValid.observe(viewLifecycleOwner) { isValid ->
            binding.buttonLogin.isEnabled = isValid
        }

        binding.buttonLogin.setOnClickListener {
            val success = viewModel.login()
            if (success) {
                val options = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
                findNavController().navigate(R.id.tabFragment, null, options)
            } else {
                binding.textInputLayoutUsername.error = getString(R.string.error_invalid_credentials)
                binding.textInputLayoutPassword.error = getString(R.string.error_invalid_credentials)
            }
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
