package com.example.fragmentnavigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.example.fragmentnavigation.Options
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.FragmentOptionsBinding

class OptionsFragment : Fragment() {

    private lateinit var binding: FragmentOptionsBinding
    private lateinit var options: Options
    private lateinit var boxItems: List<BoxCountItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options =
            savedInstanceState?.getParcelable(KEY_OPTIONS) ?: arguments?.getParcelable(ARG_OPTIONS)
                    ?: throw IllegalStateException("You have to specify options to run this fragment")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOptionsBinding.inflate(inflater, container, false)

        setupSpinner()
        setupCheckbox()
        updateUI()

        binding.cancelButton.setOnClickListener { onCancelPressed() }
        binding.confirmButton.setOnClickListener { onConfirmPressed() }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private fun setupSpinner() {
        boxItems = (1..6).map { BoxCountItem(it, resources.getQuantityString(R.plurals.boxes, it, it)) }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            boxItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        binding.boxCountSpinner.adapter = adapter
        binding.boxCountSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val count = boxItems[position].count
                options = options.copy(boxCount = count)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupCheckbox() {
        binding.enableTimerCheckbox.setOnClickListener {
            options = options.copy(isTimerEnabled = binding.enableTimerCheckbox.isChecked)
        }
    }

    private fun updateUI() {
        binding.enableTimerCheckbox.isChecked = options.isTimerEnabled

        val currentIndex = boxItems.indexOfFirst { it.count == options.boxCount }
        binding.boxCountSpinner.setSelection(currentIndex)
    }

    private fun onCancelPressed() {
        navigator().goBack()
    }

    private fun onConfirmPressed() {
        navigator().publishResult(options)
        navigator().goBack()
    }

    companion object {
        private const val ARG_OPTIONS = "ARG_OPTIONS"
        private const val KEY_OPTIONS = "KEY_OPTIONS"

        fun newInstance(options: Options) =
            OptionsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_OPTIONS, options)
                }
            }
    }

    private class BoxCountItem(
        val count: Int,
        private val optionsTitle: String
    ) {
        override fun toString(): String {
            return optionsTitle
        }
    }
}