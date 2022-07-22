package com.example.qissa.ui.stories

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qissa.R
import com.example.qissa.adapter.DemoPostPagedLoadStateAdapter
import com.example.qissa.adapter.StoriesAdapter
import com.example.qissa.databinding.FragmentStoriesBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataX
import com.example.qissa.ui.home.HomeFragment
import com.example.qissa.ui.home.PostViewModel
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.DataShareViewModel
import com.example.qissa.viewModels.ReactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StoriesFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<DataX>,
    StoriesAdapter.CommentShowListener {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentStoriesBinding

    private val reactionViewModel: ReactionViewModel by viewModels()

    private val viewModel: PostViewModel by viewModels()
    private val dataSharedViewModel: DataShareViewModel by viewModels({ requireParentFragment() })

    private lateinit var adapter: StoriesAdapter
//    private  var reactionId: Int = 0

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(ContentValues.TAG, "Fragmentfasefownhfefjwoifjwofiwoeifey: fragment called called")
        adapter = StoriesAdapter(
            requireContext(),
            this,
            dataSharedViewModel.userInfo.value!!
        )
        StoriesAdapter.commentShowListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoriesBinding.inflate(inflater, container, false)
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
        binding.recycleViewPost.layoutManager = layoutManager

        binding.recycleViewPost.adapter = adapter.withLoadStateFooter(
            DemoPostPagedLoadStateAdapter { adapter.retry() }
        )
        // Add paging data
        Toast.makeText(requireContext(), "viewmodel called", Toast.LENGTH_SHORT).show()
        val pagingData =
            dataSharedViewModel.userInfo.value?.let {
                viewModel.getSingleUserPosts(it.id).distinctUntilChanged()
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

    override fun onClick(position: Int, dataObject: DataX, value: Boolean) {
        if (sharedPreference.getToken().isNullOrEmpty()) {
            Toast.makeText(context, " login first", Toast.LENGTH_LONG).show()
        } else if (value) {
            sharedPreference.getToken().toString().let {
                reactionViewModel.doReaction(it, dataObject.id, "like")
            }
        } else {
            sharedPreference.getToken()?.let { reactionViewModel.deleteReaction(dataObject.id, it) }
        }
//        initObservers()
    }

    override fun onLongClick(position: Int, dataObject: DataX) {
    }

    override fun showEmptyView() {
        binding.emptyView.emptyLayout.visible()
    }

    override fun hideEmptyView() {
        binding.emptyView.emptyLayout.gone()
    }

    override fun initObservers() {
        reactionViewModel.items.observe(
            this,
            Observer {
                it?.let { reactionResponse ->
                    if (reactionResponse.success) {
                        Toast.makeText(context, reactionResponse.toString(), Toast.LENGTH_LONG)
                            .show()

//                        Snackbar.make(binding.root, loginResponse.message, Snackbar.LENGTH_SHORT).show()4
                    } else {
                        Toast.makeText(
                            context,
                            reactionResponse.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
//        reactionViewModel.eventShowMessage.observe(
//            this,
//            Observer {
//                it?.let { message ->
// //                    binding.loadingLayout.root.visibility = View.GONE
//                    Toast.makeText(context,message, Toast.LENGTH_LONG).show()
//                    Snackbar.make(
//                        binding.root,
//                        message,
//                        Snackbar.LENGTH_LONG
//                    ).show()
// //                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                }
//            })
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun showComments(postId: Int) {
//        Toast.makeText(requireContext(), "show commnet callde", Toast.LENGTH_SHORT).show()
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        listener?.gotoFragment(R.id.action_otherProfileFragment_to_commentsFragment, bundle)
    }
}
