package com.example.fragmentnavigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.contract.HasCustomTitle
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.FragmentCongratulationsBinding

class CongratulationsFragment : Fragment(), HasCustomTitle {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentCongratulationsBinding.inflate(inflater, container, false).apply {
            mainMenuButton.setOnClickListener { onMainMenuClicked() }
        }.root

    private fun onMainMenuClicked() {
        navigator().goToMenu()
    }

    override fun getTitleRes(): Int = R.string.box
}