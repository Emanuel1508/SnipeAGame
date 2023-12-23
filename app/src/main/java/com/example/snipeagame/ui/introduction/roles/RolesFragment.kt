package com.example.snipeagame.ui.introduction.roles

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentRolesBinding
import com.example.snipeagame.ui.main.MainActivity
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.ButtonState
import com.example.snipeagame.utils.disable
import com.example.snipeagame.utils.enable
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RolesFragment : BaseFragment<FragmentRolesBinding>(FragmentRolesBinding::inflate) {
    private val viewModel: RolesViewModel by viewModels()
    private lateinit var adapter: RolesAdapter
    private lateinit var chosenFaction: String

    private val TAG = this::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRoleViews()
        setupAdapter()
        setupObservers()
        setupListeners()
        getFaction()
    }

    private fun setupAdapter() {
        adapter = RolesAdapter { position ->
            viewModel.onRoleItemClick(position)
            verifyRoles(position)
        }
        val recyclerView: RecyclerView = binding.roleRecyclerView
        recyclerView.adapter = adapter
        viewModel.roles.observe(viewLifecycleOwner) { roles ->
            adapter.setRoles(roles)
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            buttonState.observe(viewLifecycleOwner) { buttonState ->
                with(binding) {
                    when (buttonState) {
                        is ButtonState.IsEnabled -> roleFinishButton.enable()
                        is ButtonState.NotEnabled -> roleFinishButton.disable()
                    }
                }
            }
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showAlertDialog(error.message)
            }
            loadingLiveData.observe(viewLifecycleOwner) {
                updateRefreshAnimation(it)
            }
        }
    }

    private fun setupListeners() {
        binding.roleFinishButton.setOnClickListener {
            viewModel.onFinishButtonClick(chosenFaction)
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun updateRefreshAnimation(value: Result.Loading) {
        when (value.shouldShowLoading) {
            true -> showLoadingAnimation()
            false -> hideLoadingAnimation()
        }
    }

    private fun showLoadingAnimation() = binding.rolesSwipeRefresh.showRefresh()
    private fun hideLoadingAnimation() = binding.rolesSwipeRefresh.hideRefresh()

    private fun setupRoleViews() {
        setupSniperRole()
        setupAssaultRiflemanRole()
        setupMachineGunnerRole()
        setupBreacherRole()
        setupMedicRole()
        setupGrenadierRole()
    }

    private fun setupSniperRole() {
        viewModel.setupRole(
            R.drawable.dmr,
            getString(R.string.role_designated_marksman),
            getString(R.string.designated_marksman_description),
            R.color.white
        )
    }

    private fun setupAssaultRiflemanRole() {
        viewModel.setupRole(
            R.drawable.assault_rifle,
            getString(R.string.role_assault_rifleman),
            getString(R.string.assault_rifleman_description),
            R.color.white
        )
    }

    private fun setupMachineGunnerRole() {
        viewModel.setupRole(
            R.drawable.machine_gun,
            getString(R.string.role_machine_gunner),
            getString(R.string.machine_gunner_description),
            R.color.white
        )
    }

    private fun setupBreacherRole() {
        viewModel.setupRole(
            R.drawable.shotgun,
            getString(R.string.role_breacher),
            getString(R.string.breacher_description),
            R.color.white
        )
    }

    private fun setupMedicRole() {
        viewModel.setupRole(
            R.drawable.medic,
            getString(R.string.role_medic),
            getString(R.string.medic_description),
            R.color.white
        )
    }

    private fun setupGrenadierRole() {
        viewModel.setupRole(
            R.drawable.grenade,
            getString(R.string.role_grenadier),
            getString(R.string.grenadier_description),
            R.color.white
        )
    }

    private fun verifyRoles(position: Int) {
        adapter.verifyRole(position)
    }

    private fun getFaction() {
        val args: RolesFragmentArgs by navArgs()
        chosenFaction = args.factionName
    }

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {
                    viewModel.onFinishButtonClick(chosenFaction)
                })
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}
