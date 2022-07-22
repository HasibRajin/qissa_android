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
import androidx.recyclerview.widget.RecyclerView
import com.example.qissa.R
import com.example.qissa.adapter.DemoPostPagedLoadStateAdapter
import com.example.qissa.adapter.MyStoriesAdapter
import com.example.qissa.adapter.TopicAdapter
import com.example.qissa.databinding.FragmentMyStoriesBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.interfaces.SecondaryObjectListener
import com.example.qissa.models.DataX
import com.example.qissa.models.DataXXXXXXXXXXXX
import com.example.qissa.ui.home.HomeFragment
import com.example.qissa.ui.home.PostViewModel
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.ReactionViewModel
import com.example.qissa.viewModels.TopicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyStoriesFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<DataX>,
    SecondaryObjectListener<DataXXXXXXXXXXXX>,
    MyStoriesAdapter.CommentShowListener {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentMyStoriesBinding

    private val reactionViewModel: ReactionViewModel by viewModels()

    private val viewModel: PostViewModel by viewModels()
    private val topicViewModel: TopicViewModel by viewModels()

    private lateinit var adapter: MyStoriesAdapter

    private lateinit var topicAdapter: TopicAdapter

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(ContentValues.TAG, "Fragmentfasefownhfefjwoifjwofiwoeifey: fragment called called")
        adapter = MyStoriesAdapter(
            requireContext(),
            this,
            sharedPreference
        )
        MyStoriesAdapter.commentShowListener = this
        topicViewModel.getTopics()
        topicAdapter = TopicAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyStoriesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.topicViewModel = topicViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        listener?.hideLoading()

        initViews()

        initListeners()

        initAdapterObserver()
        binding.recycleViewStories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var computeVerticalValue =
                    binding.recycleViewStories.computeVerticalScrollOffset().toFloat()
                binding.containerWritePost.translationY = -computeVerticalValue
            }
        })
        binding.closeImageView.setOnClickListener {
            activity?.onBackPressed()
        }
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

        val topicLayoutManager = LinearLayoutManager(activity)
        topicLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recycleViewTopic.layoutManager = topicLayoutManager
        binding.recycleViewTopic.adapter = topicAdapter

        val layoutManager = LinearLayoutManager(activity)
        binding.recycleViewStories.layoutManager = layoutManager

        binding.recycleViewStories.adapter = adapter.withLoadStateFooter(
            DemoPostPagedLoadStateAdapter { adapter.retry() }
        )
        // Add paging data
        val pagingData = sharedPreference.getUser()?.let { viewModel.getSingleUserPosts(it.id) }

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
        listener?.gotoFragment(R.id.action_myStoriesFragment_to_commentsFragment, bundle)
    }

    override fun onSecObjClick(position: Int, dataObject: DataXXXXXXXXXXXX) {
        binding.tvSubtitle.text = "Stories about ${dataObject.name}"
        initAdapterObserver()
        val pagingData = sharedPreference.getUser()
            ?.let { viewModel.getTopicUserPosts(dataObject.id, it.id) }
        lifecycleScope.launch {
            pagingData?.collect {
                adapter.submitData(it)
            }
        }
    }

    override fun showSecObjEmptyView() {
        binding.emptyView.emptyLayout.visible()
    }

    override fun hideSecObjEmptyView() {
        binding.emptyView.emptyLayout.gone()
    }
}
