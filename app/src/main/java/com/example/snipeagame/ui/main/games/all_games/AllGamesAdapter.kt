package com.example.snipeagame.ui.main.games.all_games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameParameters
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.databinding.ItemGameBinding
import com.example.snipeagame.utils.StringConstants

class AllGamesAdapter() : RecyclerView.Adapter<AllGamesAdapter.ViewHolder>() {
    private val allGames = mutableListOf<GameParameters>()
    private val TAG = this::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = allGames[position]
        setViews(holder, game)
    }

    fun setGames(games: Collection<GameParameters>) {
        allGames.clear()
        allGames.addAll(games)
        notifyItemChanged(NumberConstants.ZERO, allGames.size)
    }

    override fun getItemCount() = allGames.size

    class ViewHolder(binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateTextView = binding.gameDateTextView
        val timeTextView = binding.gameTimeTextView
        val locationTextView = binding.gameLocationTextView
        val numberOfPlayersTextView = binding.gameNumberOfPlayersTextView
        val button = binding.joinGameButton
    }

    @SuppressLint("SetTextI18n")
    private fun setViews(holder: ViewHolder, game: GameParameters) {
        holder.apply {
            game.apply {
                dateTextView.text = StringConstants.GAME_DATE + date
                timeTextView.text = StringConstants.GAME_TIME + time
                locationTextView.text = StringConstants.GAME_LOCATION + location
                numberOfPlayersTextView.text = StringConstants.MAXIMUM_PLAYERS + numberOfPlayers
            }
        }
    }
}