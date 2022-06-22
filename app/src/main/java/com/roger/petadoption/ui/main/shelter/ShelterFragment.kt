package com.roger.petadoption.ui.main.shelter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.roger.petadoption.databinding.FragmentShelterBinding
import com.roger.petadoption.ui.base.BaseFragment
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShelterFragment : BaseFragment<FragmentShelterBinding>() {

    private val viewModel: ShelterViewModel by viewModels()
    private val shelterListPagingAdapter: ShelterListPagingAdapter by lazy {
        ShelterListPagingAdapter {
//            val intent = Intent(activity, HospitalDetailActivity::class.java).apply {
//                putExtra(HospitalDetailActivity.ARG_HOSPITAL_ID, it.number)
//            }
//            startActivity(intent)
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
            with(rvShelterList) {
                adapter = shelterListPagingAdapter
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