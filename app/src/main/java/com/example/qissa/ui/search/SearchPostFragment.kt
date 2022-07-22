package com.example.qissa.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qissa.R
import com.example.qissa.adapter.SearchPostAdapter
import com.example.qissa.databinding.FragmentSearchPostBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.Post
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.inVisible
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.ReactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SearchPostFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<Post>,
    SearchPostAdapter.CommentShowListener {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentSearchPostBinding

    private val viewModel: SearchViewModel by viewModels({ requireParentFragment() })
    private val reactionViewModel: ReactionViewModel by viewModels()

    private lateinit var adapter: SearchPostAdapter

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SearchPostAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        listener?.hideLoading()
        initViews()
        initListeners()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun initViews() {
        // List
        val layoutManager = LinearLayoutManager(activity)
        binding.recycleViewPost.layoutManager = layoutManager
        binding.recycleViewPost.adapter = adapter
//        Log.d("loadComment", "findComments: getting comments done }")
    }

    override fun initObservers() {

        viewModel.items.observe(
            viewLifecycleOwner
        ) { items ->
            if (items != null) {
                adapter.setItems(items.data.post)
            }
        }

        viewModel.eventShowLoading
            .observe(
                viewLifecycleOwner,
                Observer {
                    it?.apply {
                        if (it == true) {
                            binding.loadingView.visible()
                        } else {
                            binding.loadingView.inVisible()
                        }
                    }
                }
            )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach")

        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach")
        listener = null
    }

    override fun showEmptyView() {
        binding.emptyView.emptyLayout.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        binding.emptyView.emptyLayout.visibility = View.GONE
    }

    override fun onClick(position: Int, dataObject: Post, value: Boolean) {
        if (sharedPreference.getToken().isNullOrEmpty()) {
            Toast.makeText(context, " login first", Toast.LENGTH_LONG).show()
        } else if (value) {
            sharedPreference.getToken().toString().let {
                reactionViewModel.doReaction(it, dataObject.id, "like")
            }
        } else {
            sharedPreference.getToken()?.let { reactionViewModel.deleteReaction(dataObject.id, it) }
        }
    }

    override fun onLongClick(position: Int, dataObject: Post) {
        TODO("Not yet implemented")
    }

    override fun showComments(postId: Int) {
//        Toast.makeText(requireContext(), "show commnet callde", Toast.LENGTH_SHORT).show()
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        listener?.gotoFragment(R.id.action_searchFragment_to_commentsFragment, bundle)
    }
}
