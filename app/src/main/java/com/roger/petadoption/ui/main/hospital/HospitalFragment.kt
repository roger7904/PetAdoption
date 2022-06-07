package com.roger.petadoption.ui.main.hospital

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.roger.petadoption.databinding.FragmentHospitalBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalFragment : BaseFragment<FragmentHospitalBinding>() {

    private val viewModel: HospitalViewModel by viewModels()
    private val regionAdapter: FilterRegionAdapter by lazy {
        FilterRegionAdapter {
            viewModel.setRegion(it)
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
    ): FragmentHospitalBinding = FragmentHospitalBinding.inflate(inflater, container, false)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            with(rvRegion) {
                regionAdapter.selectionType = viewModel.region.value
                adapter = regionAdapter
                regionAdapter.submitList(mutableListOf(*FilterRegion.values()))
            }

            viewModel.region.observe(viewLifecycleOwner) {
                regionAdapter.apply {
                    selectionType = it
                    notifyDataSetChanged()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HospitalFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}