package com.example.qissa.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qissa.R
import com.example.qissa.adapter.DemoPostPagedListAdapter
import com.example.qissa.adapter.DemoPostPagedLoadStateAdapter
import com.example.qissa.databinding.DemoFragmentPostPagedBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DemoPostPagedFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<DataX> {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: DemoFragmentPostPagedBinding

    private val viewModel: PostViewModel by viewModels()

    private lateinit var adapter: DemoPostPagedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = DemoPostPagedListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DemoFragmentPostPagedBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))

        listener?.hideLoading()

        initViews()

        initListeners()

        initAdapterObserver()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun initViews() {

        // List
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            DemoPostPagedLoadStateAdapter { adapter.retry() }
        )
        // Add paging data
        /* val pagingData = viewModel.getPostsPaged(null, -1).distinctUntilChanged()

         lifecycleScope.launch {
             pagingData.collect {
                 adapter.submitData(it)
             }
         }*/
    }

    private fun initAdapterObserver() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                val isLoading = loadState.refresh is LoadState.Loading

                binding.emptyView.root.isVisible = isListEmpty
                binding.loadingView.isVisible = isLoading
            }
        }
    }

    override fun onLongClick(position: Int, dataObject: DataX) {
    }

    override fun showEmptyView() {
        binding.emptyView.emptyLayout.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        binding.emptyView.emptyLayout.visibility = View.GONE
    }

    override fun onClick(position: Int, dataObject: DataX, value: Boolean) {
        TODO("Not yet implemented")
    }
}
