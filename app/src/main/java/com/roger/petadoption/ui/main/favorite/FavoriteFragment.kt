package com.roger.petadoption.ui.main.favorite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.roger.domain.entity.pet.PetEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.FragmentFavoriteBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.SimpleDialogFragment
import com.roger.petadoption.ui.main.MainViewModel
import com.roger.petadoption.ui.main.home.HomeFragment
import com.roger.petadoption.ui.main.home.PetDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private val viewModel: FavoriteViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
    private val detailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.getBooleanExtra(HomeFragment.ARG_IS_SET_FAVORITE, true) == false) {
                    activityViewModel.setIsNeedRefresh(true)
                    activityViewModel.getFavoritePetList()
                }
            }
        }
    private val favoritePetAdapter: FavoritePetAdapter by lazy {
        FavoritePetAdapter(
            ::rvItemClickEvent,
            ::rvItemRemoveClickEvent
        )
    }

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel? = viewModel

    override fun bindFragmentListener(context: Context) {
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            with(rvFavoriteList) {
                this.itemAnimator = null
                adapter = favoritePetAdapter
            }

            activityViewModel.favoritePetInfoList.observe(viewLifecycleOwner) {
                if (it?.toList().isNullOrEmpty()){
                    rvFavoriteList.visibility = View.GONE
                    viewEmpty.visibility = View.VISIBLE
                }else{
                    rvFavoriteList.visibility = View.VISIBLE
                    viewEmpty.visibility = View.GONE
                }
                favoritePetAdapter.submitList(it?.toList())
            }
        }
    }

    private fun rvItemClickEvent(petEntity: PetEntity) {
        val intent = Intent(activity, PetDetailActivity::class.java).apply {
            putExtra(PetDetailActivity.ARG_PET_ID, petEntity.id)
        }
        detailLauncher.launch(intent)
    }

    private fun rvItemRemoveClickEvent(petEntity: PetEntity) {
        showDialog(
            SimpleDialogFragment.newInstance(
                getString(R.string.favorite_remove_favorite_dialog_title),
                getString(R.string.favorite_remove_favorite_dialog_content),
                btnConfirm = getString(R.string.confirm),
                btnCancel = getString(R.string.cancel),
                clickEvent = object : SimpleDialogFragment.ClickEvent {
                    override fun onConfirmClick() {
                        activityViewModel.removeFavorite(petEntity.id ?: 0)
                    }

                    override fun onCancelClick() {
                    }
                }
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}