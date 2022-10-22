package me.romainhamm.efficiencyrunechecker

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hoc081098.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.romainhamm.efficiencyrunechecker.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.runeState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    when (state) {
                        RuneResult.Empty -> {
                            // no-op
                        }
                        is RuneResult.Error -> {
                            binding.progressBarLayout.isVisible = false
                            binding.jsonDisplayText.text = state.toString()
                        }
                        RuneResult.Loading -> {
                            binding.progressBarLayout.isVisible = true
                        }
                        is RuneResult.Success -> {
                            binding.progressBarLayout.isVisible = false
                            binding.jsonDisplayText.text = state.runeList.toString()
                        }
                    }
                }
        }

        binding.addJsonButton.setOnClickListener { viewModel.readJson() }
        binding.parseJsonButton.setOnClickListener { }
    }
}
