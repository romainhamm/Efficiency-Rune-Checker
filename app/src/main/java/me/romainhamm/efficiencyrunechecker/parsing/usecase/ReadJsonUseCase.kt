package me.romainhamm.efficiencyrunechecker.parsing.usecase

import me.romainhamm.efficiencyrunechecker.parsing.model.Rune

interface ReadJsonUseCase : FlowUseCase<ReadJsonUseCase.Params, List<Rune>> {
    data class Params(val fileName: String)
}
