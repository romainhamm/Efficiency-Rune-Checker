package me.romainhamm.efficiencyrunechecker.parsing.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatEffect(
    val effectType: EffectType,
    val value: Int
) : Parcelable {

    // subStatEfficiency flat count as 50% so multiply base * 2 because there is a division after
    enum class EffectType(val value: Int, val mainStatEfficiency: Map<Int, Int>, val subStatEfficiency: Map<Int, Int>) {
        HP(
            1,
            mapOf(1 to 804, 2 to 1092, 3 to 1380, 4 to 1704, 5 to 2088, 6 to 2448),
            mapOf(1 to 600, 2 to 1050, 3 to 1650, 4 to 2250, 5 to 1500 * 2, 6 to 3750)
        ),
        HP_PERCENT(2, mapOf(1 to 18, 2 to 20, 3 to 38, 4 to 43, 5 to 51, 6 to 63), mapOf(1 to 10, 2 to 15, 3 to 25, 4 to 30, 5 to 35, 6 to 40)),
        ATK(3, mapOf(1 to 54, 2 to 74, 3 to 93, 4 to 113, 5 to 135, 6 to 160), mapOf(1 to 40, 2 to 50, 3 to 80, 4 to 100, 5 to 150, 6 to 200)),
        ATK_PERCENT(4, mapOf(1 to 18, 2 to 20, 3 to 38, 4 to 43, 5 to 51, 6 to 63), mapOf(1 to 10, 2 to 15, 3 to 25, 4 to 30, 5 to 35, 6 to 40)),
        DEF(5, mapOf(1 to 54, 2 to 74, 3 to 93, 4 to 113, 5 to 135, 6 to 160), mapOf(1 to 40, 2 to 50, 3 to 80, 4 to 100, 5 to 150, 6 to 200)),
        DEF_PERCENT(6, mapOf(1 to 18, 2 to 20, 3 to 38, 4 to 43, 5 to 51, 6 to 63), mapOf(1 to 10, 2 to 15, 3 to 25, 4 to 30, 5 to 35, 6 to 40)),
        SPEED(8, mapOf(1 to 18, 2 to 19, 3 to 25, 4 to 30, 5 to 39, 6 to 42), mapOf(1 to 5, 2 to 10, 3 to 15, 4 to 20, 5 to 25, 6 to 30)),
        CRIT_RATE(9, mapOf(1 to 18, 2 to 20, 3 to 37, 4 to 41, 5 to 47, 6 to 58), mapOf(1 to 5, 2 to 10, 3 to 15, 4 to 20, 5 to 25, 6 to 30)),
        CRIT_DMG(10, mapOf(1 to 20, 2 to 37, 3 to 43, 4 to 58, 5 to 65, 6 to 80), mapOf(1 to 10, 2 to 15, 3 to 20, 4 to 25, 5 to 30, 6 to 35)),
        RES(11, mapOf(1 to 18, 2 to 20, 3 to 38, 4 to 44, 5 to 51, 6 to 64), mapOf(1 to 10, 2 to 15, 3 to 25, 4 to 30, 5 to 35, 6 to 40)),
        ACC(12, mapOf(1 to 18, 2 to 20, 3 to 38, 4 to 44, 5 to 51, 6 to 64), mapOf(1 to 10, 2 to 15, 3 to 25, 4 to 30, 5 to 35, 6 to 40)),

        UNKNOWN(10000, emptyMap(), emptyMap());

        companion object {
            private val VALUES = values()
            fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
