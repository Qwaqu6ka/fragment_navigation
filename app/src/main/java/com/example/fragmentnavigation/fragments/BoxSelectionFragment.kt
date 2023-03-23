package com.example.fragmentnavigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fragmentnavigation.Options
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.BoxItemBinding
import com.example.fragmentnavigation.databinding.FragmentBoxSelectionBinding
import kotlin.properties.Delegates
import kotlin.random.Random

class BoxSelectionFragment : Fragment() {

    private lateinit var options: Options
    private lateinit var binding: FragmentBoxSelectionBinding
    private var winBoxIndex by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options =
            savedInstanceState?.getParcelable(KEY_OPTIONS) ?: arguments?.getParcelable(ARG_OPTIONS)
                    ?: throw IllegalStateException("You need to specify options to run this fragment")
        winBoxIndex =
            savedInstanceState?.getInt(KEY_WIN_BOX_INDEX) ?: Random.nextInt(options.boxCount)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoxSelectionBinding.inflate(inflater, container, false)

        createBoxes()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
        outState.putInt(KEY_WIN_BOX_INDEX, winBoxIndex)
    }

    private fun createBoxes() {
        val boxBindings = (0 until options.boxCount).map { index ->
            val boxBinding = BoxItemBinding.inflate(layoutInflater)
            boxBinding.root.id = View.generateViewId()
            boxBinding.root.tag = index
            boxBinding.root.setOnClickListener { view -> onBoxClicked(view) }
            boxBinding.boxName.text = getString(R.string.box_title, index + 1)
            binding.root.addView(boxBinding.root)
            boxBinding
        }

        binding.flow.referencedIds = boxBindings.map { it.root.id }.toIntArray()
    }

    private fun onBoxClicked(view: View) {
        if (view.tag as Int == winBoxIndex) {
            navigator().showCongratulationsScreen()
        } else {
            Toast.makeText(requireContext(), R.string.empty_box, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val ARG_OPTIONS = "ARG_OPTIONS"
        private const val KEY_OPTIONS = "KEY_OPTIONS"
        private const val KEY_WIN_BOX_INDEX = "WIN_BOX_INDEX"

        fun newInstance(options: Options) =
            BoxSelectionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_OPTIONS, options)
                }
            }
    }
}