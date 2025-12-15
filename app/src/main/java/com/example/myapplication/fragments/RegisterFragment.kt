package com.example.myapplication.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.example.myapplication.viewmodels.RegisterViewModel
import androidx.core.widget.doAfterTextChanged
import java.util.Calendar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

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

        viewModel.isFormValid.observe(viewLifecycleOwner) { isValid ->
            binding.buttonCreateAccount.isEnabled = isValid
        }

        binding.buttonCreateAccount.setOnClickListener {
            if (viewModel.passwordsMatch()) {
                binding.textInputLayoutPassword.error = null
                binding.textInputLayoutConfirmPassword.error = null
                findNavController().popBackStack(R.id.loginFragment, false)
            } else {
                val error = getString(R.string.error_passwords_mismatch)
                binding.textInputLayoutPassword.error = error
                binding.textInputLayoutConfirmPassword.error = error
            }
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
