package com.example.snipeagame.utils

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.snipeagame.databinding.LayoutAlertBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Serializable

class AlertDialogFragment : DialogFragment() {
    private lateinit var binding: LayoutAlertBinding

    companion object {
        private const val TITLE: String = StringConstants.TITLE
        private const val DESCRIPTION: String = StringConstants.DESCRIPTION
        private const val CALLBACK_RETRY: String = StringConstants.CALLBACK_RETRY


        fun newInstance(
            title: String,
            description: String,
            onRetryClick: () -> Unit
        ): AlertDialogFragment {
            val fragment = AlertDialogFragment()
            val bundle = Bundle().apply {
                putString(TITLE, title)
                putString(DESCRIPTION, description)
                putSerializable(CALLBACK_RETRY, onRetryClick as Serializable)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = LayoutAlertBinding.inflate(layoutInflater)
        isCancelable = false
        setupViews()
        setupListeners()
        return setupDialog()?.create() ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupDialog() =
        activity?.let { MaterialAlertDialogBuilder(it).setView(binding.root) }

    private fun setupViews() {
        with(binding) {
            alertTitleTextView.text = arguments?.getString(TITLE)
            alertDescriptionTextView.text = arguments?.getString(DESCRIPTION)
        }
    }

    private fun setupListeners() {
        with(binding) {
            val onRetry = getRetryCallback()
            alertRetryButton.setOnClickListener {
                onRetry?.invoke()
                dialog?.dismiss()
            }
            alertCancelButton.setOnClickListener {
                Log.v("tag","clicked")
                dialog?.dismiss()
            }
        }
    }

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    private fun getRetryCallback() =
        arguments?.getSerializable(CALLBACK_RETRY) as? () -> Unit
}