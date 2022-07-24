package com.example.qissa.ui.question

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
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qissa.R
import com.example.qissa.adapter.AnswerAdapter
import com.example.qissa.adapter.DemoPostPagedLoadStateAdapter
import com.example.qissa.adapter.QuestionAdapter
import com.example.qissa.databinding.FragmentQuestionBinding
import com.example.qissa.databinding.QuestionItemLayoutBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.interfaces.SecondaryObjectListener
import com.example.qissa.models.DataXXXXXXXXXXXXXXX
import com.example.qissa.models.DataXXXXXXXXXXXXXXXX
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.AnswerViewModel
import com.example.qissa.viewModels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuestionFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<DataXXXXXXXXXXXXXXX>,
    SecondaryObjectListener<DataXXXXXXXXXXXXXXXX>,
    QuestionAdapter.QuestionAdapterListener {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentQuestionBinding

    private val viewModel: QuestionViewModel by viewModels()
    private val answerViewModel: AnswerViewModel by viewModels()

    private lateinit var adapter: QuestionAdapter

    private lateinit var answerAdapter: AnswerAdapter

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(ContentValues.TAG, "Fragmentfasefownhfefjwoifjwofiwoeifey: fragment called called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        answerAdapter = AnswerAdapter(requireContext(), this)

        adapter = QuestionAdapter(
            requireContext(),
            this, answerAdapter, answerViewModel, viewLifecycleOwner
        )
        QuestionAdapter.questionAdapterListener = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        listener?.hideLoading()

        initViews()

        initListeners()

        initAdapterObserver()
        initObservers()
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
        binding.recycleViewQuestion.layoutManager = layoutManager

        binding.recycleViewQuestion.adapter = adapter.withLoadStateFooter(
            DemoPostPagedLoadStateAdapter { adapter.retry() }
        )
        // Add paging data
        Toast.makeText(requireContext(), "viewmodel called", Toast.LENGTH_SHORT).show()
        val pagingData = viewModel.getQuestionPaged().distinctUntilChanged()

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

    override fun showEmptyView() {
        binding.emptyView.emptyLayout.visible()
    }

    override fun hideEmptyView() {
        binding.emptyView.emptyLayout.gone()
    }

    override fun initObservers() {
    }

    override fun onClick(position: Int, dataObject: DataXXXXXXXXXXXXXXX, value: Boolean) {
    }

    override fun onLongClick(position: Int, dataObject: DataXXXXXXXXXXXXXXX) {
        TODO("Not yet implemented")
    }

    override fun onSecObjClick(position: Int, dataObject: DataXXXXXXXXXXXXXXXX) {
        TODO("Not yet implemented")
    }

    override fun showSecObjEmptyView() {
        binding.emptyView.emptyLayout.visible()
    }

    override fun hideSecObjEmptyView() {
        binding.emptyView.emptyLayout.gone()
    }

    override fun showAnswer(questionId: Int, binding: QuestionItemLayoutBinding) {
    }
}
