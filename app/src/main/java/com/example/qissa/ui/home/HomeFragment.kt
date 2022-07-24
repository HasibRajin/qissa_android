package com.example.qissa.ui.home

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.qissa.R
import com.example.qissa.adapter.DemoPostPagedLoadStateAdapter
import com.example.qissa.adapter.HomeAdapter
import com.example.qissa.databinding.FragmentHomeBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataX
import com.example.qissa.ui.writepost.CreatePostViewModel
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.ReactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<DataX>,
    HomeAdapter.CommentShowListener {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentHomeBinding

    private val reactionViewModel: ReactionViewModel by viewModels()

    private val viewModel: PostViewModel by viewModels()
    private val deletePostViewModel: CreatePostViewModel by viewModels()
    var adaptePosition = 0
    private lateinit var adapter: HomeAdapter
//    private lateinit var postList: ArrayList<DataX>
//    private  var reactionId: Int = 0

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(ContentValues.TAG, "Fragmentfasefownhfefjwoifjwofiwoeifey: fragment called called")

        adapter = HomeAdapter(requireContext(), this, sharedPreference)
        HomeAdapter.commentShowListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.tvUserProfileNameID.text = sharedPreference.getUser()?.name.toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        listener?.hideLoading()
        refreshPage()
        initViews()

        initListeners()
        initObservers()

        initAdapterObserver()
        binding.profileImageView.setOnClickListener {
            listener?.gotoFragment(R.id.action_home_fragment_to_profile_fragment)
        }
        binding.textviewWritePost.setOnClickListener {
            listener?.gotoFragment(R.id.action_homeFragment_to_writeFragment)
        }
//        binding.profileImageView.setOnTouchListener { v, _ ->
//            v.performClick()
//            listener?.gotoFragment(R.id.action_home_fragment_to_profile_fragment)
//            true
//        }

        binding.recycleViewPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var computeVerticalValue =
                    binding.recycleViewPost.computeVerticalScrollOffset().toFloat()
                binding.containerWritePost.translationY = -computeVerticalValue
            }
        })
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
        val pagingData = viewModel.getPostsPaged(null, -1, null).distinctUntilChanged()
        lifecycleScope.launch {
            pagingData.collect {

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

    private fun refreshPage() {

        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            val pagingData = viewModel.getPostsPaged(null, -1, null).distinctUntilChanged()
            lifecycleScope.launch {
                pagingData.collect {

                    adapter.submitData(it)
                }
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

    override fun onPause() {
        super.onPause()
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
            viewLifecycleOwner,
            Observer {
                it?.let { reactionResponse ->
                    if (reactionResponse.success) {
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
        deletePostViewModel.items.observe(
            viewLifecycleOwner,
            Observer { data ->
                data?.let { createPostResponse ->
                    if (createPostResponse.success) {
                        Toast.makeText(context, createPostResponse.message, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            createPostResponse.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreference.getUser()?.profile_pic.isNullOrEmpty()
        ) {

            Glide.with(this).load(R.drawable.ic_circle_icons_profile)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageView)
        } else {
            Glide.with(this).load(sharedPreference.getImageUrl())
                .placeholder(R.drawable.ic_circle_icons_profile)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageView)
        }
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun showComments(postId: Int) {
//        Toast.makeText(requireContext(), "show commnet callde", Toast.LENGTH_SHORT).show()
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        listener?.gotoFragment(R.id.action_homeFragment_to_commentsFragment, bundle)
    }

    override fun showProfile(userId: Int) {
        val bundle = Bundle()
        bundle.putInt("userId", userId)
        if ((sharedPreference.getUser()?.id ?: Int) == userId) {
            listener?.gotoFragment(R.id.action_home_fragment_to_profile_fragment, bundle)
        } else {
            listener?.gotoFragment(R.id.action_home_fragment_to_otherProfile_fragment, bundle)
        }
    }

    override fun deletePost(item: DataX, position: Int) {
        sharedPreference.getToken()?.let {
            deletePostViewModel.deletePost(it, item.id)
        }
        item.flag = true
//        var list = adapter.snapshot().toMutableList()
//        Timber.tag(ContentValues.TAG)
//            .d("jwoifjwofiwoeifey: getPostsPaged  REMOVE BEFORE" + list.size.toString())
//        var list2 =
//            list.apply {
//                removeAt(position)
//            } as ArrayList<DataX>
        /* = java.util.ArrayList<com.example.qissa.models.DataX> */

//        val data = adapter.snapshot().toMutableList()
        adapter.snapshot().toMutableList().apply { removeAt(position) }
        adapter.notifyItemRemoved(position)
//        Toast.makeText(requireContext(), list.size.toString(), Toast.LENGTH_SHORT).show()
//
//        // val pagingData = viewModel.getPostsPaged(null, position, list).distinctUntilChanged()
//        lifecycleScope.launch {
//            Timber.tag(ContentValues.TAG)
//                .d("jwoifjwofiwoeifey: getPostsPaged  cALLED" + list.size.toString())
//            adapter.submitData(list2 as PagingData<DataX>)
//        }
    }

    override fun editPost(item: DataX, position: Int) {
    }

    override fun savePost(item: DataX, position: Int) {
    }

    override fun reportPost(item: DataX, position: Int) {
    }
}
