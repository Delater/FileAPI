package com.fk.file_api.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fk.file_api.R
import com.fk.file_api.viewModels.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment : Fragment(R.layout.image_fragment) {

    private lateinit var toolbar: Toolbar
    private lateinit var imageView: ImageView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var loadingSpinner: ProgressBar

    private val viewModel: ImageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            showState(state)
        }
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun findViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        imageView = view.findViewById(R.id.imageView)
        errorText = view.findViewById(R.id.errorText)
        refreshButton = view.findViewById(R.id.refreshButton)
        loadingSpinner = view.findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = arguments?.getString("itemId")
        if (id == null) {
            findNavController().popBackStack()
        } else {
            viewModel.fetchItemData(id)
        }
    }

    private fun showState(state: ImageViewModel.State) {
        when (state) {
            is ImageViewModel.State.Loading -> {
                loadingSpinner.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                errorText.visibility = View.GONE
                refreshButton.visibility = View.GONE
            }
            is ImageViewModel.State.Error -> {
                loadingSpinner.visibility = View.GONE
                imageView.visibility = View.GONE
                errorText.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
            }
            is ImageViewModel.State.Content -> {
                loadImage(state.imageBytes)
                loadingSpinner.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                errorText.visibility = View.GONE
                refreshButton.visibility = View.GONE
            }
        }
    }

    private fun loadImage(imageBytes: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
        imageView.setImageBitmap(bitmap)
    }
}