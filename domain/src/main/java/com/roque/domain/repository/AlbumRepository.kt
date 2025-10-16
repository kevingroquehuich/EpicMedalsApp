package com.roque.domain.repository

import com.roque.domain.model.AlbumItem
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun getAlbumItems(): Flow<List<AlbumItem>>

}