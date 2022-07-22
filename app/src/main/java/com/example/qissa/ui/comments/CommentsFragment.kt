package com.example.qissa.ui.comments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qissa.R
import com.example.qissa.adapter.CommentAdapter
import com.example.qissa.databinding.FragmentCommentsBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.DataXXX
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.hideKeyboard
import com.example.qissa.viewModels.CreateCommentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class CommentsFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<DataXXX> {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentCommentsBinding

    private val viewModel: CommentViewModel by viewModels()

    private val createViewModel: CreateCommentViewModel by viewModels()

    private lateinit var adapter: CommentAdapter

    @Inject
    lateinit var sharedPreference: SharedPreference

    private var postId by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        postId = arguments?.getInt("postId")!!
        Toast.makeText(requireContext(), postId.toString(), Toast.LENGTH_SHORT).show()
        adapter = CommentAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView")
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.topBar.tvTopBarTitle.text = "Comments"
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        checkCommentText()
        initViews()
        initListeners()
        load(postId)

        binding.uploadImageview.setOnClickListener {
            callCreateComment(postId)
            activity?.hideKeyboard()
            initObservers()
        }
        binding.writeCommentsEdittext.addTextChangedListener(textWatchers)
        binding.topBar.imgBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private var textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (binding.writeCommentsEdittext.isFocused) checkCommentText()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // do something
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // do something
        }
    }

    private fun checkCommentText(): Boolean {
        return if (binding.writeCommentsEdittext.text.length >= 2) {
            binding.uploadImageview.setImageResource(R.drawable.ic_upload)
            binding.uploadImageview.isClickable = true
            true
        } else {
            binding.uploadImageview.setImageResource(R.drawable.ic_upload_transparent)
            binding.uploadImageview.isClickable = false
            false
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    private fun load(postId: Int) {
        sharedPreference.getToken()?.let { viewModel.getComments(postId, it) }
    }

    private fun callCreateComment(postId: Int) {
        sharedPreference.getToken()?.let {
            createViewModel.createComment(
                it,
                binding.writeCommentsEdittext.text.toString(),
                postId
            )
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun initViews() {
        // List
        val layoutManager = LinearLayoutManager(activity)
        binding.recycleViewComment.layoutManager = layoutManager
        binding.recycleViewComment.adapter = adapter
//        Log.d("loadComment", "findComments: getting comments done }")
    }

    override fun initObservers() {

        createViewModel.items.observe(
            viewLifecycleOwner
        ) { items ->
            if (items != null) {
                load(postId)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.recycleViewComment.layoutManager?.scrollToPosition(0)
                }, 500)
            }
        }

        createViewModel.eventShowLoading
            .observe(
                viewLifecycleOwner
            ) {
                it?.run {
                    if (this) {
                        listener?.showLoading()
                    } else {
                        binding.writeCommentsEdittext.text.clear()
                        binding.recycleViewComment.requestFocus()
                        listener?.hideLoading()
                    }
                }
            }
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

    override fun onClick(position: Int, dataObject: DataXXX, value: Boolean) {
        // do something
    }

    override fun onLongClick(position: Int, dataObject: DataXXX) {
        // do something
    }

    override fun showEmptyView() {
        binding.emptyView.emptyLayout.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        binding.emptyView.emptyLayout.visibility = View.GONE
    }
}
