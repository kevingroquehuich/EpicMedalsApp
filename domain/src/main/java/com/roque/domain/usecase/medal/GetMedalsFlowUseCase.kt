package com.roque.domain.usecase.medal

import com.roque.domain.model.Medal
import com.roque.domain.repository.MedalRepository
import kotlinx.coroutines.flow.Flow

class GetMedalsFlowUseCase(
    private val repository: MedalRepository
) {
    operator fun invoke(): Flow<List<Medal>> = repository.medalsFlow()
}