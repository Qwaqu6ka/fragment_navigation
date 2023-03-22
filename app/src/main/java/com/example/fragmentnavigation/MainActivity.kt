package com.example.fragmentnavigation

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.fragmentnavigation.contract.Navigator
import com.example.fragmentnavigation.contract.ResultListener
import com.example.fragmentnavigation.databinding.ActivityMainBinding
import com.example.fragmentnavigation.fragments.*

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.fragmentContainer.id, MenuFragment())
                .commit()
        }
    }

    override fun showBoxSelectionScreen(options: Options) {
        launchFragment(BoxSelectionFragment.newInstance(options))
    }

    override fun showOptionsScreen(options: Options) {
        launchFragment(OptionsFragment.newInstance(options))
    }

    override fun showAboutScreen() {
        launchFragment(AboutFragment())
    }

    override fun showCongratulationsScreen() {
        launchFragment(CongratulationsFragment())
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun goToMenu() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
    }

    override fun <T : Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: ResultListener<T>) {
        supportFragmentManager.setFragmentResultListener(clazz.name, owner) { key, bundle ->
            listener.invoke(bundle.getParcelable(KEY_RESULT)!!)
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val KEY_RESULT = "result"
    }
}