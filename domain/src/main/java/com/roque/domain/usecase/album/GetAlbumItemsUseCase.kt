package com.roque.domain.usecase.album

import com.roque.domain.repository.AlbumRepository

class GetAlbumItemsUseCase(private val repository: AlbumRepository) {

    operator fun invoke() = repository.getAlbumItems()
}