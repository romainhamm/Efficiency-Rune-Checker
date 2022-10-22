package me.romainhamm.efficiencyrunechecker.parsing.interactor

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import it.czerwinski.android.hilt.annotations.Bound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapLatest
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

    override suspend fun buildUseCase(param: ReadJsonUseCase.Params) =
        readJson(param).flatMapLatest {
            parseJson(it)
        }.flowOn(Dispatchers.IO)

    private fun readJson(param: ReadJsonUseCase.Params) = flow {
        emit(context.assets.open(param.fileName).bufferedReader().use { it.readText() })
    }.flowOn(Dispatchers.IO)

    private fun parseJson(json: String) = flow {
        val listType = Types.newParameterizedType(List::class.java, RuneOutput::class.java)
        val adapter = moshi.adapter<List<RuneOutput>>(listType)

        val jsonList = (adapter.fromJson(json) ?: emptyList())
            .map(converter)
            .filter { it.stars == 6 && it.efficiency >= 100 }
        emit(jsonList)
    }.flowOn(Dispatchers.IO)
}
