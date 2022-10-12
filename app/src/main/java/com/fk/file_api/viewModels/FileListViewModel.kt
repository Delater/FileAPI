package com.fk.file_api.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fk.file_api.entity.Item
import com.fk.file_api.network.FilesRepository
import com.fk.file_api.util.AppSchedulers
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor(
    private val filesRepository: FilesRepository,
    private val schedulers: AppSchedulers
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mutableState = MutableLiveData<State>(State.Loading)

    val state: LiveData<State>
        get() = mutableState

    fun fetchItemData(itemId: String?) {
        val disposable = filesRepository.getItems(itemId)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe(
                { fileList ->
                    mutableState.value = State.Content(fileList)
                },
                { error ->
                    Log.e(
                        FileListViewModel::class.qualifiedName,
                        error.message ?: "fetch file list error"
                    )
                    mutableState.value = State.Error
                }
            )

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class State {
        object Loading : State()
        object Error : State()
        class Content(val fileList: List<Item>) : State()
    }
}