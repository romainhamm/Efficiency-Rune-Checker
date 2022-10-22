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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: ReadJsonUseCase,
) : ViewModel() {

    private val _runeState: MutableStateFlow<RuneResult> = MutableStateFlow(RuneResult.Empty)
    val runeState: StateFlow<RuneResult> = _runeState

    fun readJson() {
        viewModelScope.launch {
            _runeState.value = RuneResult.Loading
            interactor.invoke(ReadJsonUseCase.Params("Neosyder-13183017.json"))
                .catch {
                    Timber.e("Error $it")
                    _runeState.value = RuneResult.Error(it)
                }
                .collect {
                    _runeState.value = RuneResult.Success(it)
                }
        }
    }
}

sealed class RuneResult {
    data class Success(val runeList: Map<Rune.SetType, Pair<Int, Triple<Map<Int, Int>, Map<Int, Int>, Map<Int, Int>>>>) : RuneResult()
    data class Error(val throwable: Throwable) : RuneResult()
    object Loading : RuneResult()
    object Empty : RuneResult()
}
