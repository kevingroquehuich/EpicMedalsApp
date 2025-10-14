package com.roque.domain.usecase

import com.roque.domain.model.Medal
import com.roque.domain.repository.MedalRepository


class SaveMedalsUseCase(
    private val repository: MedalRepository
) {
    suspend operator fun invoke(medals: List<Medal>) {
        repository.saveMedals(medals)
    }
}