package com.example.snipeagame.ui.main.games.create_game

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentCreateGameBinding
import com.example.snipeagame.ui.main.games.create_game.CreateGameViewModel.ShowDialog
import com.example.snipeagame.utils.ButtonState
import com.example.snipeagame.utils.StringConstants
import com.example.snipeagame.utils.disable
import com.example.snipeagame.utils.enable
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class CreateGameFragment :
    BaseFragment<FragmentCreateGameBinding>(FragmentCreateGameBinding::inflate) {
    private val viewModel: CreateGameViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
        formatInitialText()
    }

    private fun setupListeners() {
        with(binding) {
            with(viewModel) {
                selectDateButton.setOnClickListener {
                    onSelectDateButtonClick()
                }
                selectTimeButton.setOnClickListener {
                    onSelectTimeButtonClick()
                }
                locationInputText.doAfterTextChanged {
                    onValidateLocation(locationInputText.text.toString())
                    isButtonActive()
                }
                playersInputText.doAfterTextChanged {
                    onValidateNumberOfPlayers(playersInputText.text.toString())
                    isButtonActive()
                }
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            navigation.observe(viewLifecycleOwner) { dialog ->
                when (dialog) {
                    is ShowDialog.DateDialog -> showDateDialog()
                    is ShowDialog.TimeDialog -> showTimeDialog()
                }
            }
            createButtonState.observe(viewLifecycleOwner) { buttonState ->
                with(binding) {
                    when (buttonState) {
                        is ButtonState.IsEnabled -> createGameButton.enable()
                        is ButtonState.NotEnabled -> createGameButton.disable()
                    }
                }
            }
        }
    }

    private fun showDateDialog() {
        val calendarDate = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            with(calendarDate) {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            updateDateLabel(calendarDate)
        }
        DatePickerDialog(
            requireContext(),
            datePicker,
            calendarDate.get(Calendar.YEAR),
            calendarDate.get(Calendar.MONTH),
            calendarDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimeDialog() {
        val calendarTime = Calendar.getInstance()
        val timePicker = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            with(calendarTime) {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                timeZone = TimeZone.getDefault()
            }
            updateTimeLabel(calendarTime)
        }
        TimePickerDialog(
            requireContext(),
            timePicker,
            calendarTime.get(Calendar.HOUR_OF_DAY),
            calendarTime.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun updateDateLabel(calendarDate: Calendar?) {
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        calendarDate?.let { calendar ->
            val formattedDate = date.format(calendar.time)
            binding.gameDateTextView.text = getString(R.string.game_date, formattedDate)
        }
        with(viewModel) {
            onValidateDate(formatDate())
            isButtonActive()
        }
    }

    private fun updateTimeLabel(calendarTime: Calendar?) {
        val time = SimpleDateFormat("HH:mm", Locale.getDefault())
        calendarTime?.let { calendar ->
            val formattedTime = time.format(calendar.time)
            binding.gameTimeTextView.text = getString(R.string.game_time, formattedTime)
        }
        with(viewModel) {
            onValidateTime(formatTime())
            onValidateDate(formatDate())
            isButtonActive()
        }
    }

    private fun formatInitialText() {
        with(binding) {
            gameDateTextView.text = getString(R.string.game_date, " ")
            gameTimeTextView.text = getString(R.string.game_time, " ")
        }
    }

    private fun formatDate(): String {
        return binding.gameDateTextView.text.toString()
            .replace(StringConstants.GAME_DATE, StringConstants.EMPTY_STRING)
    }

    private fun formatTime(): String {
        return binding.gameTimeTextView.text.toString()
            .replace(StringConstants.GAME_TIME, StringConstants.EMPTY_STRING)
    }
}