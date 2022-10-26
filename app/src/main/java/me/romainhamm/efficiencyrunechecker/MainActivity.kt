package me.romainhamm.efficiencyrunechecker

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.database.getStringOrNull
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
import me.romainhamm.efficiencyrunechecker.activitycontract.SelectFileParams
import me.romainhamm.efficiencyrunechecker.activitycontract.SelectFileResultContract
import me.romainhamm.efficiencyrunechecker.chart.extensions.initCommonValues
import me.romainhamm.efficiencyrunechecker.databinding.ActivityMainBinding
import java.io.FileNotFoundException

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    private val getJsonContract = registerForActivityResult(SelectFileResultContract()) { uri: Uri? ->
        if (uri == null) {
            showError("No file selected")
            return@registerForActivityResult
        }
        contentResolver.query(uri, null, null, null, null)?.let { cursor ->
            if (cursor.moveToFirst()) {
                val mimeType = cursor.getStringOrNull(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))
                if (mimeType != null && mimeType.contains("json")) {
                    try {
                        contentResolver.openInputStream(uri)?.let {
                            viewModel.readJson(it)
                        } ?: run {
                            showError("Can't open json")
                        }
                    } catch (e: FileNotFoundException) {
                        showError("Can't open json")
                    }
                } else {
                    showError("Please select a valid json")
                }
            } else {
                showError("Please select a valid json")
            }
            cursor.close()
        }
    }

    private val radarDataSet100 by lazy {
        RadarDataSet(emptyList(), "100").initCommonValues(ContextCompat.getColor(this@MainActivity, R.color.grey_500))
    }

    private val radarDataSet110 by lazy {
        RadarDataSet(emptyList(), "110").initCommonValues(ContextCompat.getColor(this@MainActivity, R.color.yellow_400))
    }

    private val radarDataSet120 by lazy {
        RadarDataSet(emptyList(), "120").initCommonValues(Color.rgb(168, 44, 44))
    }

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

                            val set1 = radarDataSet100.apply { values = data100 }
                            val set2 = radarDataSet110.apply { values = data110 }
                            val set3 = radarDataSet120.apply { values = data120 }
                            val data = RadarData(listOf(set1, set2, set3)).apply {
                                setDrawValues(false)
                            }

                            setAxis(labels)
                            binding.spiderChart.data = data
                            binding.spiderChart.invalidate()

                            binding.spiderChart.animateXY(1400, 1400, Easing.EaseInOutQuad)
                        }
                    }
                }
        }

        binding.addJsonButton.setOnClickListener {
            getJsonContract.launch(SelectFileParams("application/json"))
        }
    }

    private fun initGraph() {
        with(binding.spiderChart) {
            legend.textColor = ContextCompat.getColor(this@MainActivity, R.color.grey_200)
            setBackgroundColor(Color.TRANSPARENT)
            setTouchEnabled(false)
            description.isEnabled = false
            webLineWidth = 1f
            webColor = Color.LTGRAY
            webLineWidthInner = 1f
            webColorInner = Color.LTGRAY
            webAlpha = 100
            extraRightOffset = 6f
            extraLeftOffset = 6f
            yAxis.textColor = Color.LTGRAY
            xAxis.textSize = 9f
            xAxis.textColor = ContextCompat.getColor(this@MainActivity, R.color.grey_200)
        }
    }

    private fun setAxis(labels: List<String>) {
        binding.spiderChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (labels.isNotEmpty())
                    labels[value.toInt() % labels.size]
                else value.toString()
            }
        }
    }

    private fun Context.showError(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
