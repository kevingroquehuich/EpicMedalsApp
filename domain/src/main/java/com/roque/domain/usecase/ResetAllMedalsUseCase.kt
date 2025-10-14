package com.roque.domain.usecase

import com.roque.domain.repository.MedalRepository


class ResetAllMedalsUseCase(
    private val repository: MedalRepository
) {
    suspend operator fun invoke() = repository.resetAllMedals()
}