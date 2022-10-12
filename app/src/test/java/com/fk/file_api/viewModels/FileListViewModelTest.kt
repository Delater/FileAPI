package com.fk.file_api.viewModels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fk.file_api.TestScheduler
import com.fk.file_api.entity.Item
import com.fk.file_api.network.FilesRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.util.*

@RunWith(JUnit4::class)
class FileListViewModelTest {
    private val testScheduler = TestScheduler()

    @MockK
    lateinit var filesRepository: FilesRepository

    lateinit var fileListViewModel: FileListViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        MockKAnnotations.init(this)
        fileListViewModel = FileListViewModel(filesRepository, testScheduler)
    }

    @Test
    fun `loading as initial state`() {
        every { filesRepository.getItems(any()) } returns Single.never()


        assertEquals(FileListViewModel.State.Loading, fileListViewModel.state.value)
    }

    @Test
    fun `sets error state if exception is thrown while retriving items`() {
        every { filesRepository.getItems(any()) } returns Single.error(IOException())

        fileListViewModel.fetchItemData("")

        assertEquals(FileListViewModel.State.Error, fileListViewModel.state.value)
    }

    @Test
    fun `returns same item data in state as from repository`() {
        every { filesRepository.getItems(any()) } returns Single.just(defaultUserList())

        fileListViewModel.fetchItemData("")

        assertEquals(
            defaultUserList(),
            (fileListViewModel.state.value as FileListViewModel.State.Content).fileList
        )
    }

    private fun defaultUserList() = listOf(defaultItem(), defaultItem())

    private fun defaultItem() = Item("image/png", "id", false, Date().toString(), "item", "id")
}