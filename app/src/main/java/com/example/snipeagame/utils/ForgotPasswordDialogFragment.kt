package com.example.snipeagame.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.example.domain.utils.ValidationMessage
import com.example.snipeagame.R
import com.example.snipeagame.databinding.LayoutForgotPasswordAlertBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Serializable

class ForgotPasswordDialogFragment : DialogFragment() {
    private lateinit var binding: LayoutForgotPasswordAlertBinding

    companion object {
        private const val CALLBACK_SUBMIT: String = StringConstants.CALLBACK_SUBMIT

        fun newInstance(onSubmitClick: (String) -> Unit): ForgotPasswordDialogFragment {
            val fragment = ForgotPasswordDialogFragment()
            val bundle = Bundle().apply {
                putSerializable(CALLBACK_SUBMIT, onSubmitClick as Serializable)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = LayoutForgotPasswordAlertBinding.inflate(layoutInflater)
        isCancelable = false
        setupListeners()
        return setupDialog()?.create() ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupDialog() =
        activity?.let { MaterialAlertDialogBuilder(it).setView(binding.root) }

    private fun setupListeners() {
        val onSubmitClick = getSubmitCallback()
        with(binding) {
            alertPositiveButton.setOnClickListener {
                onSubmitClick?.invoke(dialogInputText.text.toString())
                dialog?.dismiss()
            }
            alertNegativeButton.setOnClickListener {
                dialog?.dismiss()
            }
            dialogInputText.setOnEditorActionListener { _, actionId, _ ->
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkEmail(inputMethodManager, dialogInputText.text.toString())
                }
                true
            }
        }
    }

    private fun LayoutForgotPasswordAlertBinding.checkEmail(
        inputMethodManager: InputMethodManager,
        inputText: String
    ) {
        val validateFields = ValidateFields()
        if (validateFields.validateEmail(inputText) != ValidationMessage.ERROR_NOT_FOUND) {
            dialogInputText.error = getString(R.string.invalid_email)
            alertPositiveButton.disable()
        } else {
            inputMethodManager.hideSoftInputFromWindow(dialogInputText.windowToken, 0)
            alertPositiveButton.enable()
        }
    }

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    private fun getSubmitCallback() =
        arguments?.getSerializable(CALLBACK_SUBMIT) as? (String) -> Unit
}