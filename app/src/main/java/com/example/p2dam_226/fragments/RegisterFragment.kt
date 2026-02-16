package com.example.p2dam_226.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.p2dam_226.R
import com.example.p2dam_226.databinding.FragmentRegisterBinding
import com.example.p2dam_226.viewmodels.NewUserViewModel
import com.example.p2dam_226.viewmodels.NewUserViewModelFactory
import com.example.p2dam_226.viewmodels.NewUserUiState
import com.google.android.material.snackbar.Snackbar
import androidx.core.widget.doAfterTextChanged
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import java.util.Calendar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewUserViewModel by viewModels { NewUserViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextWatchers()
        setupDatePicker()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isFormValid.collect { isValid ->
                        binding.buttonCreateAccount.isEnabled = isValid
                    }
                }
                launch {
                    viewModel.newUserUiState.collect { state ->
                        when(state) {
                            is NewUserUiState.Created -> {
                                Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack(R.id.loginFragment, false)
                            }
                            is NewUserUiState.Error -> {
                                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                            }
                            is NewUserUiState.Loading -> {
                                binding.buttonCreateAccount.isEnabled = false
                            }
                            is NewUserUiState.Idle -> {
                                // Do nothing
                            }
                        }
                    }
                }
            }
        }

        binding.buttonCreateAccount.setOnClickListener {
            viewModel.register()
        }
    }

    private fun setupTextWatchers() {
        binding.textInputEditUsername.doAfterTextChanged { e ->
            viewModel.onUsernameChanged(e?.toString().orEmpty())
        }
        binding.textInputEditPassword.doAfterTextChanged { e ->
            viewModel.onPasswordChanged(e?.toString().orEmpty())
            binding.textInputLayoutPassword.error = null
        }
        binding.textInputEditConfirmPassword.doAfterTextChanged { e ->
            viewModel.onConfirmPasswordChanged(e?.toString().orEmpty())
            binding.textInputLayoutConfirmPassword.error = null
        }
    }

    private fun setupDatePicker() {
        val edit = binding.textInputEditBirthdate
        edit.isFocusable = false
        edit.isClickable = true
        edit.setOnClickListener { showDatePicker() }
        binding.textInputLayoutBirthdate.setEndIconOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val month1 = month + 1
            val text = String.format("%02d/%02d/%04d", dayOfMonth, month1, year)
            binding.textInputEditBirthdate.setText(text)
            viewModel.onBirthdateChanged(text)
        }
        DatePickerDialog(requireContext(), listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
