package me.romainhamm.efficiencyrunechecker.chart.extensions

import com.github.mikephil.charting.data.RadarDataSet

fun RadarDataSet.initCommonValues(color: Int): RadarDataSet {
    this.color = color
    this.fillColor = color
    setDrawFilled(true)
    fillAlpha = 180
    lineWidth = 2f
    return this
}
