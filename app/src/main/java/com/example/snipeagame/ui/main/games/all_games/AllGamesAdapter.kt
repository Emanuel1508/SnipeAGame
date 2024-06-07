package com.example.snipeagame.ui.main.games.all_games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameParameters
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.databinding.ItemGameBinding
import com.example.snipeagame.utils.StringConstants
import com.example.snipeagame.utils.disable

class AllGamesAdapter(private val gameClickListener: GameClickListener) :
    RecyclerView.Adapter<AllGamesAdapter.ViewHolder>() {
    private val allGames = mutableListOf<GameParameters>()
    private val TAG = this::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = allGames[position]
        setViews(holder, game)
        setupListeners(holder, game)
    }

    override fun getItemCount() = allGames.size

    class ViewHolder(binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateTextView = binding.dateTextView
        val timeTextView = binding.timeTextView
        val locationTextView = binding.locationTextView
        val numberOfPlayersTextView = binding.playersTextView
        val button = binding.gameButton
    }

    @SuppressLint("SetTextI18n")
    private fun setViews(holder: ViewHolder, game: GameParameters) {
        holder.apply {
            game.apply {
                dateTextView.text = date
                timeTextView.text = time
                locationTextView.text = location
                numberOfPlayersTextView.text =
                    "$currentPlayers/$numberOfPlayers"
                if (currentPlayers == numberOfPlayers.toInt()) {
                    button.text = StringConstants.MAX_ROOM
                    button.disable()
                }
            }
        }
    }

    private fun setupListeners(holder: ViewHolder, game: GameParameters) {
        val position = allGames.indexOf(game)
        holder.button.setOnClickListener {
            gameClickListener.onGameClick(game)
            allGames.remove(game)
            notifyItemRemoved(position)
            notifyItemRangeChanged(NumberConstants.ZERO, allGames.size)
        }
    }

    fun updateGames() {
        notifyDataSetChanged()
    }

    fun setGames(games: Collection<GameParameters>) {
        allGames.clear()
        allGames.addAll(games.sorted())
        notifyItemChanged(NumberConstants.ZERO, allGames.size)
    }

    interface GameClickListener {
        fun onGameClick(game: GameParameters)
    }
}