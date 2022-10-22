package me.romainhamm.efficiencyrunechecker.parsing.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatEffect(
    val effectType: EffectType,
    val value: Int
) : Parcelable {

    enum class EffectType(val value: Int, val subStatEfficiency: Double) {
        HP(1, 1875.0), HP_PERCENT(2, 40.0), ATK(3, 100.0), ATK_PERCENT(4, 40.0), DEF(5, 100.0), DEF_PERCENT(6, 40.0),
        SPEED(8, 30.0), CRIT_RATE(9, 30.0), CRIT_DMG(10, 35.0), RES(11, 40.0), ACC(12, 40.0),

        UNKNOWN(10000, -1.0);

        companion object {
            private val VALUES = values()
            fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
