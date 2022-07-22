package com.example.qissa.ui.follower

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.example.qissa.adapter.FollowerAdapter
import com.example.qissa.adapter.FollowingAdapter
import com.example.qissa.databinding.FragmentMyFollowerBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.interfaces.SecondaryObjectListener
import com.example.qissa.models.DataXXXXXXXXX
import com.example.qissa.models.DataXXXXXXXXXXX
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFollowerFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<DataXXXXXXXXX>,
    SecondaryObjectListener<DataXXXXXXXXXXX> {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentMyFollowerBinding

    private val viewModel: UserRelationViewModel by viewModels()
//    private val dataSharedViewModel: DataShareViewModel by viewModels({ requireParentFragment() })

    private lateinit var adapter: FollowerAdapter
    private lateinit var followingAdapter: FollowingAdapter

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FollowerAdapter(requireContext(), this)
        followingAdapter = FollowingAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyFollowerBinding.inflate(inflater, container, false)
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

        binding.recycleViewFollow.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var computeVerticalValue =
                    binding.recycleViewFollow.computeVerticalScrollOffset().toFloat()
                binding.containerWritePost.translationY = -computeVerticalValue
            }
        })
        binding.closeImageView.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.tvUserFollower.setOnClickListener(this::onClick)
        binding.tvUserFollowing.setOnClickListener(this::onClick)
        binding.tvUserFav.setOnClickListener(this::onClick)
        binding.tvUserBlock.setOnClickListener(this::onClick)
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
        val pagingData = sharedPreference.getToken()
            ?.let {
                sharedPreference.getUser()
                    ?.let { it1 ->
                        viewModel.getUserFollower(it, it1.id, "follower").distinctUntilChanged()
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
                if (isLoading) {
                    listener?.showLoading()
                } else {
                    listener?.hideLoading()
                }
            }
        }
    }

    private fun initFollowAdapterObserver() {
        lifecycleScope.launch {
            followingAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && followingAdapter.itemCount == 0
                val isLoading = loadState.refresh is LoadState.Loading

                binding.emptyView.root.isVisible = isListEmpty
                binding.loadingView.isVisible = isLoading
                if (isLoading) {
                    listener?.showLoading()
                } else {
                    listener?.hideLoading()
                }
            }
        }
    }

    private fun onClick(v: View?) {
        when (v) {
            binding.tvUserFollower -> callApi(
                "follower",
                binding.tvUserFollower,
                binding.tvUserFollowing,
                binding.tvUserFav,
                binding.tvUserBlock
            )
            binding.tvUserFollowing -> callApi(
                "follow",
                binding.tvUserFollowing,
                binding.tvUserFollower,
                binding.tvUserFav,
                binding.tvUserBlock
            )
            binding.tvUserFav -> callApi(
                "favourite",
                binding.tvUserFav,
                binding.tvUserFollowing,
                binding.tvUserFollower,
                binding.tvUserBlock
            )
            binding.tvUserBlock -> callApi(
                "block",
                binding.tvUserBlock,
                binding.tvUserFollowing,
                binding.tvUserFav,
                binding.tvUserFollower
            )
        }
    }

    private fun callApi(
        relatableType: String,
        view1: TextView,
        view2: TextView,
        view3: TextView,
        view4: TextView
    ) {
        if (relatableType == "follower") {
            initAdapterObserver()
            val layoutManager = LinearLayoutManager(activity)
            binding.recycleViewFollow.layoutManager = layoutManager

            binding.recycleViewFollow.adapter = adapter.withLoadStateFooter(
                DemoPostPagedLoadStateAdapter { adapter.retry() }
            )
            val pagingData = sharedPreference.getToken()
                ?.let {
                    sharedPreference.getUser()
                        ?.let { it1 ->
                            viewModel.getUserFollower(it, it1.id, "follower").distinctUntilChanged()
                        }
                }

            lifecycleScope.launch {
                pagingData?.collect {
                    adapter.submitData(it)
                }
            }
        } else {
            initFollowAdapterObserver()
            val followlayoutManager = LinearLayoutManager(activity)
            binding.recycleViewFollow.layoutManager = followlayoutManager

            binding.recycleViewFollow.adapter = followingAdapter.withLoadStateFooter(
                DemoPostPagedLoadStateAdapter { followingAdapter.retry() }
            )
            val pagingData = sharedPreference.getToken()
                ?.let {
                    sharedPreference.getUser()
                        ?.let { it1 ->
                            viewModel.getUserFollowing(it, it1.id, relatableType)
                                .distinctUntilChanged()
                        }
                }

            lifecycleScope.launch {
                pagingData?.collect {
                    followingAdapter.submitData(it)
                }
            }
        }

        view1.setTextColor(Color.parseColor("#FFFFFF"))
        view1.setBackgroundResource(R.drawable.follow_button_blue_bg)
        view2.setTextColor(R.color.blue_100_20)
        view2.setBackgroundResource(R.drawable.follow_button_bg)
        view3.setTextColor(R.color.blue_100_20)
        view3.setBackgroundResource(R.drawable.follow_button_bg)
        view4.setTextColor(R.color.blue_100_20)
        view4.setBackgroundResource(R.drawable.follow_button_bg)
    }

    override fun onClick(position: Int, dataObject: DataXXXXXXXXX, value: Boolean) {
        val userId = dataObject.followers[0].id
        val bundle = Bundle()
        bundle.putInt("userId", userId)
        if ((sharedPreference.getUser()?.id ?: Int) == userId) {
            listener?.gotoFragment(R.id.action_myFollowerFragment_to_profileFragment, bundle)
        } else {
            listener?.gotoFragment(R.id.action_myFollowerFragment_to_otherProfileFragment, bundle)
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
        viewModel.eventShowLoading
            .observe(
                viewLifecycleOwner,
                Observer {

                    it?.apply {
                        if (it == true) {
                            listener?.showLoading()
                        } else {
                            listener?.hideLoading()
                        }
                    }
                }
            )
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSecObjClick(position: Int, dataObject: DataXXXXXXXXXXX) {
        val userId = dataObject.user.id
        val bundle = Bundle()
        bundle.putInt("userId", userId)
        if ((sharedPreference.getUser()?.id ?: Int) == userId) {
            listener?.gotoFragment(R.id.action_myFollowerFragment_to_profileFragment, bundle)
        } else {
            listener?.gotoFragment(R.id.action_myFollowerFragment_to_otherProfileFragment, bundle)
        }
    }

    override fun showSecObjEmptyView() {
        binding.emptyView.emptyLayout.visible()
    }

    override fun hideSecObjEmptyView() {
        binding.emptyView.emptyLayout.gone()
    }
}
