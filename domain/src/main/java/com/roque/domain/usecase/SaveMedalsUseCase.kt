package com.roque.domain.usecase

import com.roque.domain.model.Medal
import com.roque.domain.repository.MedalRepository


class SaveMedalsUseCase(
    private val repository: MedalRepository
) {
    suspend operator fun invoke(medals: List<Medal>): Result<Unit> {
        if (medals.isEmpty()) {
            return Result.failure(IllegalArgumentException("Cannot save empty medals list"))
        }

        return try {
            repository.saveMedals(medals)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}