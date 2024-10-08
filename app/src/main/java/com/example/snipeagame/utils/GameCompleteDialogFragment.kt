package com.example.snipeagame.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.example.snipeagame.R
import com.example.snipeagame.databinding.LayoutGameCompleteAlertBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Serializable

class GameCompleteDialogFragment : DialogFragment() {
    private lateinit var binding: LayoutGameCompleteAlertBinding
    private var playerCount: Int = 0
    private var isChecked: Boolean = false

    private val TAG = this::class.java.simpleName

    companion object {
        private const val CALLBACK_SUBMIT: String = StringConstants.CALLBACK_SUBMIT
        private const val PLAYERS: String = StringConstants.PLAYER_COUNT

        fun newInstance(
            onSubmitClick: (String, Boolean) -> Unit,
            playerCount: Int,
        ): GameCompleteDialogFragment {
            val fragment = GameCompleteDialogFragment()
            val bundle = Bundle().apply {
                putSerializable(CALLBACK_SUBMIT, onSubmitClick as Serializable)
                putInt(PLAYERS, playerCount)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = LayoutGameCompleteAlertBinding.inflate(layoutInflater)
        isCancelable = false
        setupListeners()
        setPlayerCount()
        return setupDialog()?.create() ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setPlayerCount() {
        playerCount = arguments?.getInt(PLAYERS) ?: 0
    }

    private fun setupDialog() =
        activity?.let { MaterialAlertDialogBuilder(it).setView(binding.root) }

    private fun setupListeners() {
        with(binding) {
            val onSubmit = getSubmitCallback()

            alertPositiveButton.disable()
            alertPositiveButton.setOnClickListener {
                onSubmit?.invoke(dialogInputText.text.toString(), isChecked)
                dialog?.dismiss()
            }
            alertNegativeButton.setOnClickListener {
                dialog?.dismiss()
            }
            checkbox.setOnClickListener {
                isChecked = isChecked == false
                Log.v(TAG, isChecked.toString())
            }
            dialogInputText.setOnEditorActionListener { _, actionId, _ ->
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkTakedowns(inputMethodManager)
                }
                true
            }
        }
    }

    private fun LayoutGameCompleteAlertBinding.checkTakedowns(
        inputMethodManager: InputMethodManager
    ) {
        if (dialogInputText.text.toString().toInt() > (playerCount * 2)) {
            dialogInputText.error = getString(R.string.takedown_error)
            alertPositiveButton.disable()
        } else {
            inputMethodManager.hideSoftInputFromWindow(dialogInputText.windowToken, 0)
            alertPositiveButton.enable()
        }
    }

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    private fun getSubmitCallback() =
        arguments?.getSerializable(CALLBACK_SUBMIT) as? (String, Boolean) -> Unit
}