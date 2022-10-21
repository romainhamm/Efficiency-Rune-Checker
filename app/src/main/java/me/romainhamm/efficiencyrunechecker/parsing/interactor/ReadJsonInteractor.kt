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
import me.romainhamm.efficiencyrunechecker.parsing.outputmodel.RuneOutput
import me.romainhamm.efficiencyrunechecker.parsing.usecase.ReadJsonUseCase
import javax.inject.Inject

@Bound(ViewModelComponent::class)
class ReadJsonInteractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi
) : ReadJsonUseCase {

    override suspend fun buildUseCase(param: ReadJsonUseCase.Params) = flow {
        val listType = Types.newParameterizedType(List::class.java, RuneOutput::class.java)
        val adapter = moshi.adapter<List<RuneOutput>>(listType)

        val json = context.assets.open(param.fileName).bufferedReader().use { it.readText() }

        emit(adapter.fromJson(json) ?: emptyList())
    }.flowOn(Dispatchers.IO)
}
