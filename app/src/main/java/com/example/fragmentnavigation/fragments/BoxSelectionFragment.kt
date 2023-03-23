package com.example.fragmentnavigation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fragmentnavigation.Options
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.contract.HasCustomTitle
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.BoxItemBinding
import com.example.fragmentnavigation.databinding.FragmentBoxSelectionBinding
import java.lang.Long.max
import kotlin.properties.Delegates
import kotlin.random.Random

class BoxSelectionFragment : Fragment(), HasCustomTitle {

    private lateinit var options: Options
    private lateinit var binding: FragmentBoxSelectionBinding
    private var winBoxIndex by Delegates.notNull<Int>()

    private var timerStartTimestamp by Delegates.notNull<Long>()
    private var timerHandler: TimerHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options =
            savedInstanceState?.getParcelable(KEY_OPTIONS) ?: arguments?.getParcelable(ARG_OPTIONS)
                    ?: throw IllegalStateException("You need to specify options to run this fragment")
        winBoxIndex =
            savedInstanceState?.getInt(KEY_WIN_BOX_INDEX) ?: Random.nextInt(options.boxCount)

        timerHandler = if (options.isTimerEnabled) TimerHandler() else null
        timerHandler?.onCreate(savedInstanceState)
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

    override fun onStart() {
        super.onStart()
        timerHandler?.onStart()
    }

    override fun onStop() {
        super.onStop()
        timerHandler?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
        outState.putInt(KEY_WIN_BOX_INDEX, winBoxIndex)
        timerHandler?.onSaveInstanceState(outState)
    }

    override fun getTitleRes(): Int = R.string.select_box

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
//            timerAlreadyDone = true
            navigator().showCongratulationsScreen()
        } else {
            Toast.makeText(requireContext(), R.string.empty_box, Toast.LENGTH_SHORT).show()
        }
    }

    private inner class TimerHandler {

        private var timer: CountDownTimer? = null

        fun onCreate(savedInstanceState: Bundle?) {
            timerStartTimestamp =
                savedInstanceState?.getLong(KEY_START_TIMESTAMP) ?: System.currentTimeMillis()
        }

        fun onSaveInstanceState(outState: Bundle) {
            outState.putLong(KEY_START_TIMESTAMP, timerStartTimestamp)
        }

        fun onStart() {
            timer = object : CountDownTimer(getRemainingSeconds() * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    updateTimerUI()
                }

                override fun onFinish() {
                    updateTimerUI()
                    showTimerEndDialog()
                }
            }
            updateTimerUI()
            timer?.start()
        }

        fun onStop() {
            timer?.cancel()
            timer = null
        }

        private fun getRemainingSeconds(): Long {
            val finishedAt = timerStartTimestamp + TIMER_DURATION
            return max(0, (finishedAt - System.currentTimeMillis()) / 1000)
        }

        private fun updateTimerUI() {
            binding.timerTextView.text = getString(R.string.timer_text, getRemainingSeconds())
        }

        private fun showTimerEndDialog() {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.the_end)
                .setMessage(R.string.timer_end_message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ -> navigator().goBack() }
                .create()
            dialog.show()
        }
    }

    companion object {
        private const val ARG_OPTIONS = "ARG_OPTIONS"
        private const val KEY_OPTIONS = "KEY_OPTIONS"
        private const val KEY_WIN_BOX_INDEX = "WIN_BOX_INDEX"
        private const val KEY_START_TIMESTAMP = "KEY_START_TIMESTAMP"
        private const val TIMER_DURATION = 10_000L

        fun newInstance(options: Options) =
            BoxSelectionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_OPTIONS, options)
                }
            }
    }
}