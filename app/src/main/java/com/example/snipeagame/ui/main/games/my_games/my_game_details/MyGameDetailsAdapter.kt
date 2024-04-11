package com.example.snipeagame.ui.main.games.my_games.my_game_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.UserGameDataParameters
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.R
import com.example.snipeagame.databinding.ItemPlayerBinding
import com.example.snipeagame.utils.StringConstants

class MyGameDetailsAdapter : RecyclerView.Adapter<MyGameDetailsAdapter.ViewHolder>() {
    private var myGamesDetails = mutableListOf<UserGameDataParameters>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = myGamesDetails.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = myGamesDetails[position]
        setViews(holder, player)
    }

    fun setPlayers(players: List<UserGameDataParameters>) {
        myGamesDetails.clear()
        myGamesDetails.addAll(players)
        notifyItemChanged(NumberConstants.ZERO, myGamesDetails.size)
    }

    private fun setViews(holder: ViewHolder, player: UserGameDataParameters) {
        holder.apply {
            player.apply {
                fullNameTextView.text = player.name
                emailTextView.text = player.email
                factionImageView.setImageResource(getImageSrc(player.faction))
                firstRoleImageView.setImageResource(getImageSrc(player.roles[0]))
                secondRoleImageView.setImageResource(getImageSrc(player.roles[1]))
            }
        }
    }

    private fun getImageSrc(playerFaction: String): Int {
        return when (playerFaction) {
            StringConstants.AIRBORNE -> R.drawable.helicopter
            StringConstants.IMMORTALS -> R.drawable.first_aid
            StringConstants.MARKSMAN -> R.drawable.dmr
            StringConstants.ASSAULT_RIFLEMAN -> R.drawable.assault_rifle
            StringConstants.MACHINE_GUNNER -> R.drawable.machine_gun
            StringConstants.BREACHER -> R.drawable.shotgun
            StringConstants.MEDIC -> R.drawable.medic
            StringConstants.GRENADIER -> R.drawable.grenade
            else -> R.drawable.profile_image
        }
    }

    class ViewHolder(binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        val fullNameTextView = binding.playerNameTextView
        val emailTextView = binding.playerEmailTextView
        val factionImageView = binding.playerFactionImageView
        val firstRoleImageView = binding.playerFirstRoleImageView
        val secondRoleImageView = binding.playerSecondRoleImageView
    }
}