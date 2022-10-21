package me.romainhamm.efficiencyrunechecker.parsing.usecase

import kotlinx.coroutines.flow.Flow

interface UseCase<in Params, out Type> where Type : Any {
    suspend fun buildUseCase(param: Params): Type

    suspend operator fun invoke(param: Params) = buildUseCase(param)
}

interface FlowUseCase<T, R> : UseCase<T, Flow<R>>

suspend operator fun <R> FlowUseCase<Unit, R>.invoke() = invoke(Unit)
