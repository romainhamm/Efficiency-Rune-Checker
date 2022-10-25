package me.romainhamm.efficiencyrunechecker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.romainhamm.efficiencyrunechecker.parsing.model.Rune
import me.romainhamm.efficiencyrunechecker.parsing.usecase.ReadJsonUseCase
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: ReadJsonUseCase,
) : ViewModel() {

    private val _runeState: MutableStateFlow<RuneResult> = MutableStateFlow(RuneResult.Empty)
    val runeState: StateFlow<RuneResult> = _runeState

    fun readJson(inputStream: InputStream) {
        viewModelScope.launch {
            _runeState.value = RuneResult.Loading
            interactor.invoke(ReadJsonUseCase.Params(inputStream))
                .catch {
                    Timber.e("Error $it")
                    _runeState.value = RuneResult.Error(it)
                }
                .collect { runeList ->
                    val list = runeList
                        .groupBy { it.setType }
                        .mapValues { (_, g) ->
                            val infTo110 = g.filter { it.efficiency < 110 }.size
                            val supTo110 = g.filter { it.efficiency >= 110 && it.efficiency < 120 }.size
                            val supTo120 = g.filter { it.efficiency >= 120 }.size
                            Triple(infTo110, supTo110, supTo120)
                        }

                    val totalSize = list.values.sumOf { it.first + it.second + it.third }
                    _runeState.value = RuneResult.Success(totalSize, list)
                }
        }
    }
}

sealed class RuneResult {
    data class Success(val totalSize: Int, val runeList: Map<Rune.SetType, Triple<Int, Int, Int>>) : RuneResult()
    data class Error(val throwable: Throwable) : RuneResult()
    object Loading : RuneResult()
    object Empty : RuneResult()
}
