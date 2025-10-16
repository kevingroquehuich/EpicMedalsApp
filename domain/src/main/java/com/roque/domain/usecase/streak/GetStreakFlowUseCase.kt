package com.roque.domain.usecase.streak

import com.roque.domain.model.Streak
import com.roque.domain.repository.StreakRepository
import kotlinx.coroutines.flow.Flow

class GetStreakFlowUseCase(
    private val streakRepository: StreakRepository
) {
    operator fun invoke(): Flow<Streak> = streakRepository.getStreakFlow()
}