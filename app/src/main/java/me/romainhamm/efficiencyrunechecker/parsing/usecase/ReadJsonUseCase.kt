package me.romainhamm.efficiencyrunechecker.parsing.usecase

import me.romainhamm.efficiencyrunechecker.parsing.outputmodel.RuneOutput

interface ReadJsonUseCase : FlowUseCase<ReadJsonUseCase.Params, List<RuneOutput>> {
    data class Params(val fileName: String)
}
