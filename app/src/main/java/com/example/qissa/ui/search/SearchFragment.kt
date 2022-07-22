package com.example.qissa.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.postDelayed
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.qissa.R
import com.example.qissa.adapter.SearchViewPagerAdapter
import com.example.qissa.databinding.FragmentSearchBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.utils.SharedPreference
import com.example.qissa.viewModels.DataShareViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment :
    Fragment(),
    CommonMethods {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchViewPagerAdapter

    private val viewModel: SearchViewModel by viewModels()
    private val dataSharedViewModel: DataShareViewModel by viewModels()

//    private val relationViewModel: UserRelationViewModel by viewModels()
//    private val colorDark = "#4F5D73"
//    private val colorLight = "#664F5D73"
//    private var isRelated = false
//    private var relatedType: String = ""
//    private var isClicked = false

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        callUserApi()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        initListeners()
        initObservers()
        adapter = SearchViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewpagerProfile.adapter = adapter
        TabLayoutMediator(binding.layoutTab, binding.viewpagerProfile) { tab, position ->
            when (position) {
                0 -> tab.text = "Users"
                1 -> tab.text = "Stories"
                2 -> tab.text = "Topics"
            }
        }.attach()
//        binding.edittextSearch.textChanges().debounce(300)
//            .onEach { ... }
//            .launchIn(lifecycleScope)
        with(binding) {
            edittextSearch.debounce(1200L) {
                if (edittextSearch.text.isNotEmpty()) {
                    callUserApi(edittextSearch.text.toString())
                }
            }
        }
    }

    private fun EditText.debounce(delay: Long, action: (Editable?) -> Unit) {
        doAfterTextChanged { text ->
            var counter = getTag(id) as? Int ?: 0
            handler.removeCallbacksAndMessages(counter)
            handler.postDelayed(delay, ++counter) { action(text) }
            setTag(id, counter)
        }
    }

    private fun callUserApi(text: String) {
        sharedPreference.getUser()
            ?.let { viewModel.getSearchResponse(text, it.id) }
    }

    override fun initListeners() {

        binding.closeImageView.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.imageView.setOnClickListener {
            binding.edittextSearch.setText("")
        }
    }

    override fun initObservers() {

        viewModel.items.observe(
            viewLifecycleOwner
        ) { items ->
            if (items != null) {
                Log.d("userInfo", "initObservers:${items.data} ")
                dataSharedViewModel.setSearchData(items.data)
            } else {
            }
        }

//        viewModel.eventShowLoading
//            .observe(
//                viewLifecycleOwner,
//                Observer {
//
//                    it?.apply {
//                        if (it == true) {
//                            listener?.showLoading()
//                        } else {
//                            listener?.hideLoading()
//                        }
//                    }
//                }
//            )
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
}
