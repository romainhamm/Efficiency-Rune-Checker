package me.romainhamm.efficiencyrunechecker

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
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
        initGraph()
        lifecycleScope.launch {
            viewModel.runeState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    when (state) {
                        RuneResult.Empty -> {
                            // no-op
                        }
                        is RuneResult.Error -> binding.progressBarLayout.isVisible = false
                        RuneResult.Loading -> binding.progressBarLayout.isVisible = true
                        is RuneResult.Success -> {
                            binding.progressBarLayout.isVisible = false
                            binding.totalSizeText.text = getString(R.string.erc_total_size, state.totalSize)

                            val data100 = mutableListOf<RadarEntry>()
                            val data110 = mutableListOf<RadarEntry>()
                            val data120 = mutableListOf<RadarEntry>()
                            val labels = mutableListOf<String>()

                            state.runeList.forEach { (rune, data) ->
                                labels.add(rune.name)
                                data100.add(RadarEntry(data.first.toFloat()))
                                data110.add(RadarEntry(data.second.toFloat()))
                                data120.add(RadarEntry(data.third.toFloat()))
                            }

                            val set1 = RadarDataSet(data100, "100").apply {
                                color = Color.rgb(103, 110, 129)
                                fillColor = Color.rgb(103, 110, 129)
                                setDrawFilled(true)
                                fillAlpha = 180
                                lineWidth = 2f
                                isDrawHighlightCircleEnabled = true
                                setDrawHighlightIndicators(false)
                            }
                            val set2 = RadarDataSet(data110, "110").apply {
                                color = Color.rgb(121, 162, 175)
                                fillColor = Color.rgb(121, 162, 175)
                                setDrawFilled(true)
                                fillAlpha = 180
                                lineWidth = 2f
                                isDrawHighlightCircleEnabled = true
                                setDrawHighlightIndicators(false)
                            }

                            val set3 = RadarDataSet(data120, "120").apply {
                                color = Color.rgb(168, 44, 44)
                                fillColor = Color.rgb(168, 44, 44)
                                setDrawFilled(true)
                                fillAlpha = 180
                                lineWidth = 2f
                                isDrawHighlightCircleEnabled = true
                                setDrawHighlightIndicators(false)
                            }

                            val data = RadarData(listOf(set1, set2, set3)).apply {
                                setValueTextSize(8f)
                                setDrawValues(false)
                                setValueTextColor(Color.WHITE)
                            }

                            binding.spiderChart.data = data
                            binding.spiderChart.invalidate()

                            binding.spiderChart.animateXY(1400, 1400, Easing.EaseInOutQuad)

                            setAxis(labels)
                        }
                    }
                }
        }

        binding.addJsonButton.setOnClickListener { }
        binding.parseJsonButton.setOnClickListener { viewModel.readJson() }
    }

    private fun initGraph() {
        binding.spiderChart.setBackgroundColor(Color.TRANSPARENT)
        binding.spiderChart.setTouchEnabled(false)
        binding.spiderChart.description.isEnabled = false
        binding.spiderChart.webLineWidth = 1f
        binding.spiderChart.webColor = Color.LTGRAY
        binding.spiderChart.webLineWidthInner = 1f
        binding.spiderChart.webColorInner = Color.LTGRAY
        binding.spiderChart.webAlpha = 100
        binding.spiderChart.extraRightOffset = 32f
        binding.spiderChart.extraLeftOffset = 32f
    }

    private fun setAxis(labels: List<String>) {
        binding.spiderChart.yAxis.textColor = Color.WHITE

        val xAxis = binding.spiderChart.xAxis
        xAxis.textSize = 9f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels[value.toInt() % labels.size]
            }
        }
        xAxis.textColor = Color.WHITE
    }
}
