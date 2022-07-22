package com.example.qissa.ui.follower

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qissa.R
import com.example.qissa.adapter.DemoPostPagedLoadStateAdapter
import com.example.qissa.adapter.FollowerAdapter
import com.example.qissa.databinding.FragmentFollowerBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataXXXXXXXXX
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.DataShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FollowerFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<DataXXXXXXXXX> {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentFollowerBinding

    private val viewModel: UserRelationViewModel by viewModels()
    private val dataSharedViewModel: DataShareViewModel by viewModels({ requireParentFragment() })

    private lateinit var adapter: FollowerAdapter

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FollowerAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
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
        binding.recycleViewFollow.layoutManager = layoutManager

        binding.recycleViewFollow.adapter = adapter.withLoadStateFooter(
            DemoPostPagedLoadStateAdapter { adapter.retry() }
        )
        // Add paging data
        Toast.makeText(requireContext(), "viewmodel called", Toast.LENGTH_SHORT).show()
        val pagingData = sharedPreference.getToken()
            ?.let {
                dataSharedViewModel.userInfo.value?.let { it1 ->
                    viewModel.getUserFollower(
                        it,
                        it1.id, "follower"
                    ).distinctUntilChanged()
                }
            }

        lifecycleScope.launch {
            pagingData?.collect {
                adapter.submitData(it)
            }
        }
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

    override fun onClick(position: Int, dataObject: DataXXXXXXXXX, value: Boolean) {
        val userId = dataObject.followers[0].id
        val bundle = Bundle()
        bundle.putInt("userId", userId)
        if ((sharedPreference.getUser()?.id ?: Int) == userId) {
            listener?.gotoFragment(R.id.action_otherProfileFragment_to_profileFragment, bundle)
        } else {
            listener?.gotoFragment(R.id.action_otherProfileFragment_self, bundle)
        }
    }

    override fun onLongClick(position: Int, dataObject: DataXXXXXXXXX) {
    }

    override fun showEmptyView() {
        binding.emptyView.emptyLayout.visible()
    }

    override fun hideEmptyView() {
        binding.emptyView.emptyLayout.gone()
    }

    override fun initObservers() {
    }

    override fun onResume() {
        super.onResume()
    }
}
