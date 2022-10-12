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
import java.nio.charset.Charset
import java.util.*

@RunWith(JUnit4::class)
class ImageViewModelTest {
    private val testScheduler = TestScheduler()

    @MockK
    lateinit var filesRepository: FilesRepository

    lateinit var imageViewModel: ImageViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        MockKAnnotations.init(this)
        imageViewModel = ImageViewModel(filesRepository, testScheduler)
    }

    @Test
    fun `loading as initial state`() {
        every { filesRepository.getItems(any()) } returns Single.never()


        assertEquals(ImageViewModel.State.Loading, imageViewModel.state.value)
    }

    @Test
    fun `sets error state if exception is thrown while retriving image`() {
        every { filesRepository.getItemData(any()) } returns Single.error(IOException())

        imageViewModel.fetchItemData("")

        assertEquals(ImageViewModel.State.Error, imageViewModel.state.value)
    }

    @Test
    fun `returns same byte data in state as from repository`() {
        every { filesRepository.getItemData(any()) } returns Single.just(defaultByteArray())

        imageViewModel.fetchItemData("")

        assertEquals(
            byteArrayContent(),
            (imageViewModel.state.value as ImageViewModel.State.Content).imageBytes.decodeToString()
        )
    }

    private fun byteArrayContent() = "ByteArray"

    private fun defaultByteArray() = byteArrayContent().toByteArray()

}