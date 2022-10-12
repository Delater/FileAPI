package com.fk.file_api.network.services

import com.fk.file_api.entity.Item
import com.fk.file_api.entity.User
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface FilesService {

    @GET("/me")
    fun getCurrentUser(): Single<User>

    @GET("/items/{id}")
    fun getItems(@Path("id") id: String): Single<List<Item>>

    @GET("/items/{id}/data")
    fun getItemData(@Path("id") id: String): Single<ResponseBody>
}