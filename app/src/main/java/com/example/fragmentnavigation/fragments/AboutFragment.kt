package com.example.fragmentnavigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fragmentnavigation.BuildConfig
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.contract.HasCustomTitle
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.FragmentAboutBinding

class AboutFragment : Fragment(), HasCustomTitle {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAboutBinding.inflate(inflater, container, false).apply {
        versionNameTextView.text = BuildConfig.VERSION_NAME
        versionCodeTextView.text = BuildConfig.VERSION_CODE.toString()
        okButton.setOnClickListener { onOkPressed() }
    }.root

    private fun onOkPressed() {
        navigator().goBack()
    }

    override fun getTitleRes(): Int = R.string.about
}