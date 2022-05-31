package com.roger.petadoption.ui.main.home.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityFilterBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterActivity : BaseActivity<ActivityFilterBinding>() {

    private val viewModel: FilterViewModel by viewModels()
    private val typeAdapter: FilterTypeAdapter by lazy {
        FilterTypeAdapter {
            viewModel.setType(it)
        }
    }
    private val genderAdapter: FilterGenderAdapter by lazy {
        FilterGenderAdapter {
            viewModel.setGender(it)
        }
    }
    private val bodyTypeAdapter: FilterBodyTypeAdapter by lazy {
        FilterBodyTypeAdapter {
            viewModel.setBodyType(it)
        }
    }
    private val colorAdapter: FilterColorAdapter by lazy {
        FilterColorAdapter {
            viewModel.setColor(it)
        }
    }

    override fun initParam(data: Bundle) {
        viewModel.setType(data.getParcelable(ARG_TYPE))
        viewModel.setGender(data.getParcelable(ARG_GENDER))
        viewModel.setBodyType(data.getParcelable(ARG_BODY_TYPE))
        viewModel.setColor(data.getParcelable(ARG_COLOR))
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityFilterBinding =
        ActivityFilterBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            setActionBar(
                toolbar = appBar.tbDefault,
                titleId = R.string.filter_title,
                showBackButton = true
            )

            with(rvType) {
                typeAdapter.selectionType = viewModel.type.value
                adapter = typeAdapter
                typeAdapter.submitList(mutableListOf(*FilterType.values()))
            }

            with(rvGender) {
                genderAdapter.selectionType = viewModel.gender.value
                adapter = genderAdapter
                genderAdapter.submitList(mutableListOf(*FilterGender.values()))
            }

            with(rvBodyType) {
                bodyTypeAdapter.selectionType = viewModel.bodyType.value
                adapter = bodyTypeAdapter
                bodyTypeAdapter.submitList(mutableListOf(*FilterBodyType.values()))
            }

            with(rvColor) {
                colorAdapter.selectionType = viewModel.color.value
                adapter = colorAdapter
                colorAdapter.submitList(mutableListOf(*FilterColor.values()))
            }

            viewModel.type.observe(this@FilterActivity) {
                typeAdapter.apply {
                    selectionType = it
                    notifyDataSetChanged()
                }
            }

            viewModel.gender.observe(this@FilterActivity) {
                genderAdapter.apply {
                    selectionType = it
                    notifyDataSetChanged()
                }
            }

            viewModel.bodyType.observe(this@FilterActivity) {
                bodyTypeAdapter.apply {
                    selectionType = it
                    notifyDataSetChanged()
                }
            }

            viewModel.color.observe(this@FilterActivity) {
                colorAdapter.apply {
                    selectionType = it
                    notifyDataSetChanged()
                }
            }

            btnFilter.setOnClickListener {
                val intent = Intent().apply {
                    putParcelableExtra(ARG_TYPE, viewModel.type.value)
                    putParcelableExtra(ARG_GENDER, viewModel.gender.value)
                    putParcelableExtra(ARG_BODY_TYPE, viewModel.bodyType.value)
                    putParcelableExtra(ARG_COLOR, viewModel.color.value)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    companion object {
        const val ARG_TYPE = "type"
        const val ARG_GENDER = "gender"
        const val ARG_BODY_TYPE = "bodyType"
        const val ARG_COLOR = "color"

        private fun Intent.putParcelableExtra(key: String, value: Parcelable?) {
            putExtra(key, value)
        }

        fun createIntent(
            context: Context?,
            type: FilterType?,
            gender: FilterGender?,
            bodyType: FilterBodyType?,
            color: FilterColor?,
        ): Intent {
            return Intent(context, FilterActivity::class.java).apply {
                putParcelableExtra(ARG_TYPE, type)
                putParcelableExtra(ARG_GENDER, gender)
                putParcelableExtra(ARG_BODY_TYPE, bodyType)
                putParcelableExtra(ARG_COLOR, color)
            }
        }
    }
}