package com.example.fragmentnavigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fragmentnavigation.Options
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.databinding.FragmentBoxSelectionBinding

class BoxSelectionFragment : Fragment() {

    private lateinit var binding: FragmentBoxSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_box_selection, container, false)
    }

    companion object {
        private const val ARG_OPTIONS = "ARG_OPTIONS"

        fun newInstance(options: Options) =
            BoxSelectionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_OPTIONS, options)
                }
            }
    }
}