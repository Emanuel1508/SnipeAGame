package com.example.snipeagame.ui.introduction.roles

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.databinding.ItemRoleBinding
import com.example.snipeagame.ui.models.Roles

class RolesAdapter(private var onRoleItemClicked: ((Int) -> Unit)) :
    RecyclerView.Adapter<RolesAdapter.ViewHolder>() {
    private val roles = mutableListOf<Roles>()

    fun setRoles(roles: Collection<Roles>) {
        this.roles.clear()
        this.roles.addAll(roles)
        notifyItemChanged(NumberConstants.ZERO, roles.size)
    }

    fun verifyRole(position: Int) {
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRoleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = roles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val role = roles[position]
        setViews(holder, role)
        setListener(holder, position)
    }

    class ViewHolder(binding: ItemRoleBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.roleImageView
        val titleTextView: TextView = binding.roleTitleTextView
        val descriptionTextView: TextView = binding.roleDescriptionTextView
        val constraintLayout: ConstraintLayout = binding.roleConstraintLayout
    }

    private fun setViews(holder: ViewHolder, role: Roles) {
        holder.apply {
            role.apply {
                imageView.setImageResource(imageResource)
                titleTextView.text = title
                descriptionTextView.text = description
                constraintLayout.setBackgroundResource(colorResource)
            }
        }
    }

    private fun setListener(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onRoleItemClicked.invoke(position)
        }
    }
}