package com.fk.file_api.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fk.file_api.R
import com.fk.file_api.viewModels.FileListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileListFragment : Fragment(R.layout.file_list_fragment) {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var itemAdapter: ItemListAdapter

    private val viewModel: FileListViewModel by viewModels()

    private var itemId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        setUpAdapter()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            showState(state)
        }
        refreshButton.setOnClickListener {
            viewModel.fetchItemData(itemId)
        }
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        hideBackIconIfRoot()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId = arguments?.getString(ITEM_ID_KEY, null)
        viewModel.fetchItemData(itemId)
    }

    private fun hideBackIconIfRoot() {
        if (itemId == null) {
            toolbar.navigationIcon = null
        }
    }

    private fun findViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        recyclerView = view.findViewById(R.id.recyclerView)
        errorText = view.findViewById(R.id.errorText)
        refreshButton = view.findViewById(R.id.refreshButton)
        loadingSpinner = view.findViewById(R.id.progressBar)
    }

    private fun setUpAdapter() {
        itemAdapter = ItemListAdapter(
            onClick = { item ->
                if (item.isDir) {
                    findNavController().navigate(
                        R.id.to_fileList,
                        bundleOf(ITEM_ID_KEY to item.id))
                } else if (item.contentType.contains("image")) {
                    findNavController().navigate(
                        R.id.to_image,
                        bundleOf(ITEM_ID_KEY to item.id))
                }
            },
            onLongClick = { item ->
                // delete
            })
        recyclerView.adapter = itemAdapter
    }

    private fun showState(state: FileListViewModel.State) {
        when (state) {
            is FileListViewModel.State.Loading -> {
                loadingSpinner.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                errorText.visibility = View.GONE
                refreshButton.visibility = View.GONE
            }
            is FileListViewModel.State.Error -> {
                loadingSpinner.visibility = View.GONE
                recyclerView.visibility = View.GONE
                errorText.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
            }
            is FileListViewModel.State.Content -> {
                loadingSpinner.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                errorText.visibility = View.GONE
                refreshButton.visibility = View.GONE
                itemAdapter.submitList(state.fileList)
            }
        }
    }

    companion object {
        const val ITEM_ID_KEY = "itemId"
    }
}