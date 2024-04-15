package com.example.snipeagame.ui.main.games.my_games

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameParameters
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.databinding.ItemGameBinding
import com.example.snipeagame.utils.StringConstants
import com.example.snipeagame.utils.convertDate
import com.example.snipeagame.utils.disable
import com.example.snipeagame.utils.enable
import com.example.snipeagame.utils.hide
import com.example.snipeagame.utils.show
import java.util.Calendar

class MyGamesAdapter(private val myGameClickListener: MyGameClickListener) :
    RecyclerView.Adapter<MyGamesAdapter.ViewHolder>() {
    private val myGames = mutableListOf<GameParameters>()
    private val TAG = this::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = myGames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = myGames[position]
        setViews(holder, game)
        setupListener(holder, game)
    }

    @SuppressLint("SetTextI18n")
    fun setViews(holder: ViewHolder, game: GameParameters) {
        holder.apply {
            game.apply {
                dateTextView.text = StringConstants.GAME_DATE + date
                timeTextView.text = StringConstants.GAME_TIME + time
                locationTextView.text = StringConstants.GAME_LOCATION + location
                numberOfPlayersTextView.text =
                    "${StringConstants.PLAYERS}$currentPlayers/$numberOfPlayers"

                if (verifyDate(game)) {
                    button.text = StringConstants.FINISH_GAME
                    button.show()
                    button.enable()
                } else {
                    button.hide()
                    button.disable()
                }
            }
        }
    }

    fun setMyGames(games: List<GameParameters>) {
        myGames.clear()
        myGames.addAll(games)
        myGames.sortBy { it.date }
        notifyItemChanged(NumberConstants.ZERO, myGames.size)
    }

    fun finishGame(game: GameParameters) {
        val position = myGames.indexOf(game)
        myGames.remove(game)
        notifyItemRemoved(position)
    }

    private fun setupListener(holder: ViewHolder, game: GameParameters) {
        holder.itemView.setOnClickListener {
            myGameClickListener.onMyGameClick(game)
        }
        holder.button.setOnClickListener {
            myGameClickListener.onFinishGameClick(game)
        }
    }

    private fun verifyDate(game: GameParameters): Boolean {
        val formattedDate = convertDate(game)
        return formattedDate.before(Calendar.getInstance().time)
    }

    class ViewHolder(binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
        val dateTextView = binding.gameDateTextView
        val timeTextView = binding.gameTimeTextView
        val locationTextView = binding.gameLocationTextView
        val numberOfPlayersTextView = binding.gameNumberOfPlayersTextView
        val button = binding.gameButton
    }

    interface MyGameClickListener {
        fun onMyGameClick(game: GameParameters)
        fun onFinishGameClick(game: GameParameters)
    }
}