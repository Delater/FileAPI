package com.fk.file_api.di

import com.fk.file_api.network.AuthorizationHeaderInterceptor
import com.fk.file_api.network.FilesRepository
import com.fk.file_api.network.FilesRepositoryImpl
import com.fk.file_api.network.services.FilesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "http://163.172.147.216:8080"

    @Provides
    fun provideOkHttpClient(
        authorizationHeaderInterceptor: AuthorizationHeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationHeaderInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideAuthorizationHeaderInterceptor() = AuthorizationHeaderInterceptor()

    @Provides
    fun providesFilesService(retrofit: Retrofit): FilesService =
        retrofit.create(FilesService::class.java)

    @Provides
    fun provideFileNetworkRepository(filesService: FilesService): FilesRepository {
        return FilesRepositoryImpl(filesService)
    }
}