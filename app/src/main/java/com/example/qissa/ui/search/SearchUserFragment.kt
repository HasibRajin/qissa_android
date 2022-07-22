package com.example.qissa.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qissa.R
import com.example.qissa.adapter.SearchUserAdapter
import com.example.qissa.databinding.FragmentSearchUserBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.interfaces.OnObjectListInteractionListener
import com.example.qissa.models.UserXXXXX
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.inVisible
import com.example.qissa.utils.extendFunctions.visible
import timber.log.Timber
import javax.inject.Inject

class SearchUserFragment :
    Fragment(),
    CommonMethods,
    OnObjectListInteractionListener<UserXXXXX> {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentSearchUserBinding

    private val viewModel: SearchViewModel by viewModels({ requireParentFragment() })

    private lateinit var adapter: SearchUserAdapter

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SearchUserAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView")
        binding = FragmentSearchUserBinding.inflate(inflater, container, false)
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
        Timber.d("onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun initViews() {
        // List
        val layoutManager = LinearLayoutManager(activity)
        binding.recycleViewUser.layoutManager = layoutManager
        binding.recycleViewUser.adapter = adapter
//        Log.d("loadComment", "findComments: getting comments done }")
    }

    override fun initObservers() {

        viewModel.items.observe(
            viewLifecycleOwner
        ) { items ->
            if (items != null) {
                adapter.setItems(items.data.user)
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

    override fun onClick(position: Int, dataObject: UserXXXXX, value: Boolean) {
        val userId = dataObject.id
        val bundle = Bundle()
        bundle.putInt("userId", userId)
        if ((sharedPreference.getUser()?.id ?: Int) == userId) {
            listener?.gotoFragment(R.id.profileFragment, bundle)
        } else {
            listener?.gotoFragment(R.id.otherProfileFragment, bundle)
        }
    }

    override fun onLongClick(position: Int, dataObject: UserXXXXX) {
        TODO("Not yet implemented")
    }
}
