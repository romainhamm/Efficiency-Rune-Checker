package me.romainhamm.efficiencyrunechecker.parsing.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class Rune(
    val runeId: Long,
    val slot: SlotType,
    val setType: SetType,
    val upgradeCurrent: Int,
    val mainStatEffect: StatEffect,
    val innateStatEffect: StatEffect?,
    val secondaryStatEffect: List<StatEffect>,
    val quality: Quality,
    val baseQuality: Quality,
    val runeClass: Int
) : Parcelable {

    @IgnoredOnParcel
    val efficiency: Double
        get() {
            var ratio = 0.0

            ratio += (mainStatEffect.effectType.mainStatEfficiency[realRuneStar] ?: 1) / (mainStatEffect.effectType.mainStatEfficiency[6] ?: 1).toDouble()

            val statEffects = mutableListOf<StatEffect>()
            statEffects += secondaryStatEffect
            innateStatEffect?.let {
                statEffects += it
            }
            statEffects.forEach {
                ratio += it.value / (it.effectType.subStatEfficiency[6] ?: 1).toFloat()
            }

            val ratioResult = (ratio / 2.8) * 100
            return (ratioResult * 100.0).roundToInt() / 100.0
        }

    @IgnoredOnParcel
    val realRuneStar: Int
        get() {
            return if (Quality.isAncientRune(quality)) runeClass - 10 else runeClass
        }

    enum class SlotType(val value: Int) {
        UP(1), UP_RIGHT(2), DOWN_RIGHT(3), DOWN(4), DOWN_LEFT(5), UP_LEFT(6), UNKNOWN(10000);

        companion object {
            private val VALUES = values()
            fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    enum class SetType(val value: Int) {
        ENERGY(1), GUARD(2), SWIFT(3), BLADE(4), RAGE(5), FOCUS(6), ENDURE(7), FATAL(8), DESPAIR(10), VAMPIRE(11), VIOLENT(13), NEMESIS(14),
        WILL(15), SHIELD(16), REVENGE(17), DESTROY(18), FIGHT(19), DETERMINATION(20), ENHANCE(21), ACCURACY(22), TOLERANCE(23), IMMEMORIAL(99), UNKNOWN(10000);

        companion object {
            private val VALUES = values()
            fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }

    enum class Quality(val value: Int) {
        COMMON(1), MAGIC(2), RARE(3), HEROIC(4), LEGENDARY(5),
        ANCIENT_COMMON(11), ANCIENT_MAGIC(12), ANCIENT_RARE(13), ANCIENT_HEROIC(14), ANCIENT_LEGENDARY(15), UNKNOWN(10000);

        companion object {
            private val VALUES = values()
            fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
            fun isAncientRune(type: Quality) = VALUES.any { type >= ANCIENT_COMMON }
        }
    }
}
