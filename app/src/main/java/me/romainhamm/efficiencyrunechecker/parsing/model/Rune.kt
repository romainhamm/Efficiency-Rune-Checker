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
    val rank: Rank,
    val baseRank: Rank,
    val stars: Int
) : Parcelable {

    @IgnoredOnParcel
    val efficiency: Double
        get() {
            var sum = 0.0
            val statEffects = mutableListOf<StatEffect>()
            statEffects += secondaryStatEffect
            innateStatEffect?.let {
                statEffects += it
            }

            statEffects.forEach { effect ->
                val max = effect.effectType.subStatEfficiency
                sum += (effect.value / max)
            }

            val sumResult = ((sum + 1.0) / 2.8) * 100
            return (sumResult * 100.0).roundToInt() / 100.0
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
            fun isMoreCommonType(type: SetType) = VALUES.any {
                type == DESPAIR || type == DESTROY || type == VIOLENT || type == WILL || type == REVENGE
            }
        }
    }

    enum class Rank(val value: Int) {
        COMMON(1), MAGIC(2), RARE(3), HEROIC(4), LEGENDARY(5),
        ANCIENT_COMMON(11), ANCIENT_MAGIC(12), ANCIENT_RARE(13), ANCIENT_HEROIC(14), ANCIENT_LEGENDARY(15), UNKNOWN(10000);

        companion object {
            private val VALUES = values()
            fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
