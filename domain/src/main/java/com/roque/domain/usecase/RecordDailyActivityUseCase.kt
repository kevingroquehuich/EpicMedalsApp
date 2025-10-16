package com.roque.domain.usecase

import com.roque.domain.repository.StreakRepository

class RecordDailyActivityUseCase(
    private val streakRepository: StreakRepository
) {
    suspend operator fun invoke() {
        streakRepository.recordDailyActivity()
    }
}