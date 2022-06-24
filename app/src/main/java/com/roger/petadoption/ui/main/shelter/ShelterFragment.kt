package com.roger.petadoption.ui.main.shelter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.roger.petadoption.databinding.FragmentShelterBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.shelter.detail.ShelterDetailActivity
import com.roger.petadoption.ui.main.shelter.filter.ShelterFilterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShelterFragment : BaseFragment<FragmentShelterBinding>() {

    private val viewModel: ShelterViewModel by viewModels()
    private val shelterListPagingAdapter: ShelterListPagingAdapter by lazy {
        ShelterListPagingAdapter {
            val intent = Intent(activity, ShelterDetailActivity::class.java).apply {
                putExtra(ShelterDetailActivity.ARG_SHELTER_ID, it.id)
            }
            startActivity(intent)
        }
    }
    private val filterLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.setCity(result.data?.getParcelableExtra(ShelterFilterActivity.ARG_CITY))
                viewModel.getFilterPagingList()
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
    ): FragmentShelterBinding = FragmentShelterBinding.inflate(inflater, container, false)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            tvFilter.setOnClickListener {
                val intent = ShelterFilterActivity.createIntent(
                    activity,
                    viewModel.city.value,
                )
                filterLauncher.launch(intent)
            }

            with(rvShelterList) {
                adapter = shelterListPagingAdapter
            }

            shelterListPagingAdapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && shelterListPagingAdapter.itemCount < 1) {
                    rvShelterList.visibility = View.GONE
                    viewEmpty.visibility = View.VISIBLE
                } else {
                    rvShelterList.visibility = View.VISIBLE
                    viewEmpty.visibility = View.GONE
                }
            }

            viewModel.shelterListPagingData.observe(viewLifecycleOwner) {
                shelterListPagingAdapter.submitData(lifecycle, it)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShelterFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}