package me.romainhamm.efficiencyrunechecker.parsing.usecase

import me.romainhamm.efficiencyrunechecker.parsing.model.Rune

interface ReadJsonUseCase : FlowUseCase<ReadJsonUseCase.Params, Map<Rune.SetType, Pair<Int, Triple<Map<Int, Int>, Map<Int, Int>, Map<Int, Int>>>>> {
    data class Params(val fileName: String)
}
