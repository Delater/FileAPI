package com.fk.file_api.di

import com.fk.file_api.util.AppSchedulers
import com.fk.file_api.util.AppSchedulersImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    fun provideSchedulers(): AppSchedulers = AppSchedulersImpl()
}