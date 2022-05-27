package com.roger.petadoption.ui.main.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.roger.petadoption.databinding.FragmentHomeBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private val petListPagingAdapter: PetListPagingAdapter by lazy {
        PetListPagingAdapter { petEntity ->
            val intent = Intent(activity, PetDetailActivity::class.java).apply {
                putExtra(PetDetailActivity.ARG_PET_ID, petEntity.id)
            }
            startActivity(intent)
        }
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

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}