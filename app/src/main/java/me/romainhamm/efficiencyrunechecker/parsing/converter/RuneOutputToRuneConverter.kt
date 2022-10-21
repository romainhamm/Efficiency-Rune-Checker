package me.romainhamm.efficiencyrunechecker.parsing.converter

import it.czerwinski.android.hilt.annotations.Bound
import me.romainhamm.efficiencyrunechecker.parsing.model.Rune
import me.romainhamm.efficiencyrunechecker.parsing.model.StatEffect
import me.romainhamm.efficiencyrunechecker.parsing.outputmodel.RuneOutput
import me.romainhamm.efficiencyrunechecker.parsing.util.Converter
import javax.inject.Inject

@Bound
class RuneOutputToRuneConverter @Inject constructor() : Converter<RuneOutput, Rune> {

    override fun convert(t: RuneOutput): Rune {
        return Rune(
            runeId = t.runeId,
            slot = Rune.SlotType.getByValue(t.slot),
            setType = Rune.SetType.getByValue(t.setType),
            upgradeCurrent = t.upgradeCurrent,
            mainStatEffect = t.mainStatEffect.toStatEffect(),
            innateStatEffect = t.innateStatEffect.toInnateStatEffect(),
            secondaryStatEffect = t.secondaryStatEffect.map { it.toStatSecondaryEffect() },
            rank = Rune.Rank.getByValue(t.rank),
            baseRank = Rune.Rank.getByValue(t.baseRank),
            stars = t.stars
        )
    }

    private fun List<Int>.toStatEffect() =
        StatEffect(
            effectType = StatEffect.EffectType.getByValue(getOrNull(0) ?: StatEffect.EffectType.UNKNOWN.value),
            value = getOrNull(1) ?: -1
        )

    private fun List<Int>.toInnateStatEffect(): StatEffect? {
        val effectType = StatEffect.EffectType.getByValue(getOrNull(0) ?: StatEffect.EffectType.UNKNOWN.value)
        if (effectType == StatEffect.EffectType.UNKNOWN) return null
        return StatEffect(
            effectType = effectType,
            value = getOrNull(1) ?: -1
        )
    }

    private fun List<Int>.toStatSecondaryEffect(): StatEffect {
        val baseValue = getOrNull(1) ?: 0
        val grindValue = getOrNull(3) ?: 0
        return StatEffect(
            effectType = StatEffect.EffectType.getByValue(getOrNull(0) ?: StatEffect.EffectType.UNKNOWN.value),
            value = baseValue + grindValue
        )
    }
}
