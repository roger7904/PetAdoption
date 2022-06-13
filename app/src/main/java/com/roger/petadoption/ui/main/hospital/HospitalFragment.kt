package com.roger.petadoption.ui.main.hospital

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.roger.petadoption.databinding.FragmentHospitalBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.MainViewModel
import com.roger.petadoption.ui.main.hospital.detail.HospitalDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalFragment : BaseFragment<FragmentHospitalBinding>() {

    private val viewModel: HospitalViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
    private val regionAdapter: FilterRegionAdapter by lazy {
        FilterRegionAdapter {
            viewModel.setRegion(it)
            viewModel.getFilterPagingList()
        }
    }
    private val hospitalListPagingAdapter: HospitalListPagingAdapter by lazy {
        HospitalListPagingAdapter {
            val intent = Intent(activity, HospitalDetailActivity::class.java).apply {
                putExtra(HospitalDetailActivity.ARG_HOSPITAL_ID, it.number)
                putParcelableArrayListExtra(HospitalDetailActivity.ARG_HOSPITAL_LIST,
                    activityViewModel.hospitalLocationList.value)
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

            with(rvHospitalList) {
                adapter = hospitalListPagingAdapter
            }

            viewModel.hospitalListPagingData.observe(viewLifecycleOwner) {
                hospitalListPagingAdapter.submitData(lifecycle, it)
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