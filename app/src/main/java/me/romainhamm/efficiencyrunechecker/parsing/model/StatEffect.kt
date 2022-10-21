package me.romainhamm.efficiencyrunechecker.parsing.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatEffect(
    val effectType: EffectType,
    val value: Int
) : Parcelable {

    enum class EffectType(val value: Int, val mainStatEfficiency: Double, val subStatEfficiency: Double) {
        HP(1, 2448.0, 1875.0), HP_PERCENT(2, 63.0, 40.0), ATK(3, 160.0, 100.0), ATK_PERCENT(4, 63.0, 40.0), DEF(5, 160.0, 100.0), DEF_PERCENT(6, 63.0, 40.0),
        SPEED(8, 42.0, 30.0), CRIT_RATE(9, 58.0, 30.0), CRIT_DMG(10, 80.0, 35.0), RES(11, 64.0, 40.0), ACC(12, 64.0, 40.0),

        UNKNOWN(10000, -1.0, -1.0);

        companion object {
            private val VALUES = values()
            fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
