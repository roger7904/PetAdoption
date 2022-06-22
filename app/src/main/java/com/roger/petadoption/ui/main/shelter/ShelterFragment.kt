package com.roger.petadoption.ui.main.shelter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.roger.petadoption.R

class ShelterFragment : Fragment() {

    companion object {
        fun newInstance() = ShelterFragment()
    }

    private lateinit var viewModel: ShelterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_shelter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShelterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}