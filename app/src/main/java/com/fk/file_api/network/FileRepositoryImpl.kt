package com.fk.file_api.network

import com.fk.file_api.entity.Item
import com.fk.file_api.network.services.FilesService
import io.reactivex.Single
import javax.inject.Inject

class FilesRepositoryImpl @Inject constructor(private val filesService: FilesService) :
    FilesRepository {

    override fun getItems(id: String?): Single<List<Item>> {
        return if (id == null) {
            filesService.getCurrentUser()
                .flatMap { user -> filesService.getItems(user.rootItem.id) }
        } else {
            filesService.getItems(id)
        }
    }

    override fun getItemData(id: String): Single<ByteArray> {
        return filesService.getItemData(id).map { responseBody -> responseBody.bytes() }
    }
}