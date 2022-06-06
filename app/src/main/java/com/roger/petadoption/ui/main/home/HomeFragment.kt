package com.roger.petadoption.ui.main.home

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
import androidx.paging.LoadState
import com.roger.domain.entity.pet.PetEntity
import com.roger.petadoption.databinding.FragmentHomeBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.home.filter.FilterActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.roger.petadoption.ui.main.MainViewModel

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
    private val filterLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.setType(result.data?.getParcelableExtra(FilterActivity.ARG_TYPE))
                viewModel.setGender(result.data?.getParcelableExtra(FilterActivity.ARG_GENDER))
                viewModel.setBodyType(result.data?.getParcelableExtra(FilterActivity.ARG_BODY_TYPE))
                viewModel.setColor(result.data?.getParcelableExtra(FilterActivity.ARG_COLOR))
                viewModel.getFilterPagingList()
            }
        }
    private val detailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.getBooleanExtra(ARG_IS_SET_FAVORITE, true) == true) {
                    activityViewModel.setIsNeedRefresh(true)
                    viewModel.getFavoritePetList()
                }
            }
        }
    private val petListPagingAdapter: PetListPagingAdapter by lazy {
        PetListPagingAdapter(
            ::rvItemClickEvent,
            ::rvItemFavoriteClickEvent,
            ::rvItemCloseClickEvent
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
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            cvFilter.setOnClickListener {
                val intent = FilterActivity.createIntent(
                    activity,
                    viewModel.type.value,
                    viewModel.gender.value,
                    viewModel.bodyType.value,
                    viewModel.color.value,
                )
                filterLauncher.launch(intent)
            }

            with(rvPetList) {
                adapter = petListPagingAdapter
            }

            viewModel.petListPagingData.observe(viewLifecycleOwner) {
                petListPagingAdapter.submitData(lifecycle, it)
            }

            petListPagingAdapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && petListPagingAdapter.itemCount < 1) {
                    rvPetList.visibility = View.GONE
                    viewEmpty.visibility = View.VISIBLE
                } else {
                    rvPetList.visibility = View.VISIBLE
                    viewEmpty.visibility = View.GONE
                }
            }
        }
    }

    private fun rvItemClickEvent(petEntity: PetEntity) {
        val intent = Intent(activity, PetDetailActivity::class.java).apply {
            putExtra(PetDetailActivity.ARG_PET_ID, petEntity.id)
        }
        detailLauncher.launch(intent)
    }

    private fun rvItemFavoriteClickEvent(petEntity: PetEntity) {
        viewModel.insertFavoritePet(petEntity.id ?: 0)
        activityViewModel.setIsNeedRefresh(true)
        petListPagingAdapter.refresh()
    }

    private fun rvItemCloseClickEvent(position: Int) {
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = position + 1
        binding?.rvPetList?.layoutManager?.startSmoothScroll(smoothScroller)
    }

    companion object {
        const val ARG_IS_SET_FAVORITE = "is_set_favorite"

        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}