package com.roger.petadoption.ui.main.shelter.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityShelterFilterBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.home.filter.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShelterFilterActivity : BaseActivity<ActivityShelterFilterBinding>() {

    private val viewModel: ShelterFilterViewModel by viewModels()
    private val cityAdapter: FilterCityAdapter by lazy {
        FilterCityAdapter {
            viewModel.setCity(it)
        }
    }

    override fun initParam(data: Bundle) {
        viewModel.setCity(data.getParcelable(ARG_CITY))
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityShelterFilterBinding =
        ActivityShelterFilterBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            setActionBar(
                toolbar = appBar.tbDefault,
                titleId = R.string.shelter_title,
                showBackButton = true
            )

            with(rvCity) {
                cityAdapter.selectionType = viewModel.city.value
                adapter = cityAdapter
                cityAdapter.submitList(mutableListOf(*FilterCity.values()))
            }

            viewModel.city.observe(this@ShelterFilterActivity) {
                cityAdapter.apply {
                    selectionType = it
                    notifyDataSetChanged()
                }
            }


            btnFilter.setOnClickListener {
                val intent = Intent().apply {
                    putParcelableExtra(ARG_CITY, viewModel.city.value)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    companion object {
        const val ARG_CITY = "city"

        private fun Intent.putParcelableExtra(key: String, value: Parcelable?) {
            putExtra(key, value)
        }

        fun createIntent(
            context: Context?,
            city: FilterCity?,
        ): Intent {
            return Intent(context, ShelterFilterActivity::class.java).apply {
                putParcelableExtra(ARG_CITY, city)
            }
        }
    }
}