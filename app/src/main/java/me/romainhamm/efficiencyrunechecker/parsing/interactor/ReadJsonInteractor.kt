package me.romainhamm.efficiencyrunechecker.parsing.interactor

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import it.czerwinski.android.hilt.annotations.Bound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import me.romainhamm.efficiencyrunechecker.parsing.model.Rune
import me.romainhamm.efficiencyrunechecker.parsing.outputmodel.RuneOutput
import me.romainhamm.efficiencyrunechecker.parsing.usecase.ReadJsonUseCase
import me.romainhamm.efficiencyrunechecker.parsing.util.Converter
import javax.inject.Inject

@Bound(ViewModelComponent::class)
class ReadJsonInteractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi,
    private val converter: Converter<RuneOutput, Rune>
) : ReadJsonUseCase {

    override suspend fun buildUseCase(param: ReadJsonUseCase.Params) = flow {
        val listType = Types.newParameterizedType(List::class.java, RuneOutput::class.java)
        val adapter = moshi.adapter<List<RuneOutput>>(listType)

        val json = context.assets.open(param.fileName).bufferedReader().use { it.readText() }

        val jsonList = (adapter.fromJson(json) ?: emptyList())
            .map(converter)
            .filter { it.stars == 6 && it.efficiency >= 100 }

        val runeList = jsonList.groupBy { it.setType }
            .mapValues { (_, g) ->
                val infTo110 = g.associateBy({ 100 }, { g.filter { it.efficiency < 110 }.size })
                val supTo110 = g.associateBy({ 110 }, { g.filter { it.efficiency >= 110 && it.efficiency < 120 }.size })
                val supTo120 = g.associateBy({ 120 }, { g.filter { it.efficiency >= 120 }.size })
                g.size to Triple(infTo110, supTo110, supTo120)
            }

        emit(runeList)
    }.flowOn(Dispatchers.IO)
}
