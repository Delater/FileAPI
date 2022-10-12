package com.fk.file_api.network

import com.fk.file_api.entity.Item
import io.reactivex.Single

interface FilesRepository {
    fun getItems(id: String?): Single<List<Item>>
    fun getItemData(id: String): Single<ByteArray>
}