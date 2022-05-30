package com.roger.petadoption.ui.main.home.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.roger.petadoption.databinding.ActivityFilterBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterActivity : BaseActivity<ActivityFilterBinding>() {

    private val viewModel: FilterViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityFilterBinding =
        ActivityFilterBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            btnFilter.setOnClickListener {
                val intent = Intent()
                    .putExtra(ARG_TYPE, viewModel.type.value)
                    .putExtra(ARG_GENDER, viewModel.gender.value)
                    .putExtra(ARG_BODY_TYPE, viewModel.bodyType.value)
                    .putExtra(ARG_COLOR, viewModel.color.value)
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

        fun createIntent(
            context: Context?,
            type: String?,
            gender: String?,
            bodyType: String?,
            color: String?,
        ): Intent {
            return Intent(context, FilterActivity::class.java).apply {
                putExtra(ARG_TYPE, type)
                putExtra(ARG_GENDER, gender)
                putExtra(ARG_BODY_TYPE, bodyType)
                putExtra(ARG_COLOR, color)
            }
        }
    }
}