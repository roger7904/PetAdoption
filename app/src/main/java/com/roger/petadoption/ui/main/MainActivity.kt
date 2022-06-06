package com.roger.petadoption.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityMainBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.favorite.FavoriteFragment
import com.roger.petadoption.ui.main.home.HomeFragment
import com.roger.petadoption.ui.main.hospital.HospitalFragment
import com.roger.petadoption.ui.main.map.MapFragment
import com.roger.petadoption.ui.main.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModels()

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            with(bnvMain) {
                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.menuItem_bnv_home -> {
                            val homeFragment = findFragmentByTag(HomeFragment::class.java.name)
                                ?: HomeFragment.newInstance()
                            switchBottomNavigationFragment(R.id.fcv_main, homeFragment)
                        }

                        R.id.menuItem_bnv_favorite -> {
                            viewModel.getFavoritePetList()
                            val favoriteFragment =
                                findFragmentByTag(FavoriteFragment::class.java.name)
                                    ?: FavoriteFragment.newInstance()
                            switchBottomNavigationFragment(R.id.fcv_main, favoriteFragment)
                        }

                        R.id.menuItem_bnv_theme -> {
                            val themeFragment = findFragmentByTag(MapFragment::class.java.name)
                                ?: MapFragment.newInstance()
                            switchBottomNavigationFragment(R.id.fcv_main, themeFragment)
                        }

                        R.id.menuItem_bnv_hospital -> {
                            val hospitalFragment =
                                findFragmentByTag(HospitalFragment::class.java.name)
                                    ?: HospitalFragment.newInstance()
                            switchBottomNavigationFragment(R.id.fcv_main, hospitalFragment)
                        }

                        R.id.menuItem_bnv_profile -> {
                            val profileFragment =
                                findFragmentByTag(ProfileFragment::class.java.name)
                                    ?: ProfileFragment.newInstance()
                            switchBottomNavigationFragment(R.id.fcv_main, profileFragment)
                        }
                    }

                    true
                }

                if (savedInstanceState == null) {
                    selectedItemId = R.id.menuItem_bnv_home
                }
            }
        }
    }
}