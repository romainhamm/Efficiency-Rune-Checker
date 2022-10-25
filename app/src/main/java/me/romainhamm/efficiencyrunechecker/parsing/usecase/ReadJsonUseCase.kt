package me.romainhamm.efficiencyrunechecker.parsing.usecase

import me.romainhamm.efficiencyrunechecker.parsing.model.Rune
import java.io.InputStream

interface ReadJsonUseCase : FlowUseCase<ReadJsonUseCase.Params, List<Rune>> {
    data class Params(val inputStream: InputStream)
}
