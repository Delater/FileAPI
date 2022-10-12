package com.fk.file_api.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fk.file_api.network.FilesRepository
import com.fk.file_api.util.AppSchedulers
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val filesRepository: FilesRepository,
    private val schedulers: AppSchedulers
) : ViewModel() {

    val state: LiveData<State>
        get() = mutableState

    private val compositeDisposable = CompositeDisposable()

    private val mutableState = MutableLiveData<State>(State.Loading)

    fun fetchItemData(id: String) {
        val disposable = filesRepository.getItemData(id)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe(
                { imageBytes ->
                    mutableState.value = State.Content(imageBytes)
                },
                { error ->
                    Log.e(
                        ImageViewModel::class.qualifiedName,
                        error.message ?: "fetch image error"
                    )
                    mutableState.value = State.Error
                }
            )

        compositeDisposable.add(disposable)
    }

    sealed class State {
        object Loading : State()
        object Error : State()
        class Content(val imageBytes: ByteArray) : State()
    }
}