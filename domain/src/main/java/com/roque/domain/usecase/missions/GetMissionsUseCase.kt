package com.roque.domain.usecase.missions

import com.roque.domain.repository.MissionsRepository

class GetMissionsUseCase (
    private val missionsRepository: MissionsRepository
) {

    operator fun invoke() = missionsRepository.getMissions()
}