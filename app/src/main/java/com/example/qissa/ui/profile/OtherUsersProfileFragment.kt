package com.example.qissa.ui.profile

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.qissa.R
import com.example.qissa.adapter.ProfileViewPagerAdapter
import com.example.qissa.databinding.FragmentOtherUsersProfileBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.models.Data
import com.example.qissa.ui.follower.UserRelationViewModel
import com.example.qissa.utils.CommonFunction.Companion.setIconAndColor
import com.example.qissa.utils.SharedPreference
import com.example.qissa.viewModels.DataShareViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class OtherUsersProfileFragment :
    Fragment(),
    CommonMethods {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentOtherUsersProfileBinding
    private lateinit var adapter: ProfileViewPagerAdapter

    private val viewModel: UserViewModel by viewModels()
    private val dataSharedViewModel: DataShareViewModel by viewModels()

    private val relationViewModel: UserRelationViewModel by viewModels()
    private val colorDark = "#4F5D73"
    private val colorLight = "#664F5D73"
    private var isRelated = false
    private var relatedType: String = ""
    private var isClicked = false

    @Inject
    lateinit var sharedPreference: SharedPreference
    private var userId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getInt("userId")!!
        callUserApi()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherUsersProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        initListeners()
        initObservers()
        adapter = ProfileViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewpagerProfile.adapter = adapter
        TabLayoutMediator(binding.layoutTab, binding.viewpagerProfile) { tab, position ->
            when (position) {
                0 -> tab.text = "About"
                1 -> tab.text = "Stories"
                2 -> tab.text = "Follower"
            }
        }.attach()
    }

    private fun callUserApi() {
        sharedPreference.getUser()?.let { viewModel.getSingleUserData(userId, it.id) }
    }

    override fun initListeners() {

        binding.cancelImageView.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.tvUserFollow.clickWithDebounce {
            callApi()
        }
        binding.tvUserFav.clickWithDebounce {
            callApi()
        }
        binding.tvUserBlock.clickWithDebounce {
            callApi()
        }
    }

    private fun callApi() {
        if (!isClicked) {
            isClicked = true
            Handler(Looper.getMainLooper()).postDelayed({
                if (isRelated) {
                    sharedPreference.getToken()
                        ?.let { relationViewModel.doRelation(it, userId, relatedType) }
                } else {
                    sharedPreference.getToken()
                        ?.let { relationViewModel.deleteRelation(userId, it) }
                }
                isClicked = false
            }, 3000)
        }
    }

    private fun View.clickWithDebounce(debounceTime: Long = 3000L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                if (!isRelated) {
                    isRelated = true
                    when (v) {
                        binding.tvUserFollow -> {
                            relatedType = "follow"
                            setIconAndColor(
                                binding.tvUserFollow,
                                R.drawable.ic_user_follow_black,
                                colorDark
                            )
                            setIconAndColor(
                                binding.tvUserFav,
                                R.drawable.ic_user_favorite,
                                colorLight
                            )
                            setIconAndColor(
                                binding.tvUserBlock,
                                R.drawable.ic_user_block,
                                colorLight
                            )
                        }
                        binding.tvUserFav -> {
                            relatedType = "favourite"
                            setIconAndColor(
                                binding.tvUserFav,
                                R.drawable.ic_user_fav_black,
                                colorDark
                            )
                            setIconAndColor(
                                binding.tvUserFollow,
                                R.drawable.ic_user_follow,
                                colorLight
                            )
                            setIconAndColor(
                                binding.tvUserBlock,
                                R.drawable.ic_user_block,
                                colorLight
                            )
                        }
                        binding.tvUserBlock -> {
                            relatedType = "block"
                            setIconAndColor(
                                binding.tvUserBlock,
                                R.drawable.ic_user_block_black,
                                colorDark
                            )
                            setIconAndColor(
                                binding.tvUserFollow,
                                R.drawable.ic_user_follow,
                                colorLight
                            )
                            setIconAndColor(
                                binding.tvUserFav,
                                R.drawable.ic_user_favorite,
                                colorLight
                            )
                        }
                    }
                } else {
                    when (v) {
                        binding.tvUserFollow -> {
                            if (relatedType == "follow") {
                                isRelated = false
                                setIconAndColor(
                                    binding.tvUserFollow,
                                    R.drawable.ic_user_follow,
                                    colorLight
                                )
                            } else {
                                relatedType = "follow"
                                setIconAndColor(
                                    binding.tvUserFollow,
                                    R.drawable.ic_user_follow_black,
                                    colorDark
                                )
                                setIconAndColor(
                                    binding.tvUserFav,
                                    R.drawable.ic_user_favorite,
                                    colorLight
                                )
                                setIconAndColor(
                                    binding.tvUserBlock,
                                    R.drawable.ic_user_block,
                                    colorLight
                                )
                            }
                        }
                        binding.tvUserFav -> {
                            if (relatedType == "favourite") {
                                isRelated = false

                                setIconAndColor(
                                    binding.tvUserFav,
                                    R.drawable.ic_user_favorite,
                                    colorLight
                                )
                            } else {
                                relatedType = "favourite"
                                setIconAndColor(
                                    binding.tvUserFav,
                                    R.drawable.ic_user_fav_black,
                                    colorDark
                                )
                                setIconAndColor(
                                    binding.tvUserFollow,
                                    R.drawable.ic_user_follow,
                                    colorLight
                                )
                                setIconAndColor(
                                    binding.tvUserBlock,
                                    R.drawable.ic_user_block,
                                    colorLight
                                )
                            }
                        }
                        binding.tvUserBlock -> {
                            if (relatedType == "block") {
                                isRelated = false
                                setIconAndColor(
                                    binding.tvUserBlock,
                                    R.drawable.ic_user_block,
                                    colorLight
                                )
                            } else {
                                relatedType = "block"
                                setIconAndColor(
                                    binding.tvUserBlock,
                                    R.drawable.ic_user_block_black,
                                    colorDark
                                )
                                setIconAndColor(
                                    binding.tvUserFollow,
                                    R.drawable.ic_user_follow,
                                    colorLight
                                )
                                setIconAndColor(
                                    binding.tvUserFav,
                                    R.drawable.ic_user_favorite,
                                    colorLight
                                )
                            }
                        }
                    }
                }
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()

                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }

    override fun initObservers() {

        viewModel.items.observe(
            viewLifecycleOwner
        ) { items ->
            if (items != null) {
                Log.d("userInfo", "initObservers:${items.data[0]} ")
                setProfileInfo(items.data[0])
                dataSharedViewModel.setUserInfo(items.data[0])
            } else {
                listener?.showLoading()
            }
        }
        viewModel.eventShowMessage.observe(
            viewLifecycleOwner
        ) {
            it?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                Snackbar.make(
                    binding.root,
                    message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

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

    private fun setProfileInfo(data: Data) {
        binding.tvUserProfileNameID.text =
            data.name
        try {

            if (data.profilePic.toString().isBlank() || data.profilePic.toString()
                .isEmpty() || data.profilePic.toString() == "null"

            ) {
                binding.profileImageView.setPadding(16, 16, 16, 16)
                Glide.with(this).load(R.drawable.ic_profile_icon)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            } else {
                binding.profileImageView.setPadding(3, 3, 3, 3)
                Glide.with(this).load(data.profilePic)
                    .placeholder(R.drawable.ic_profile_icon)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            }
        } catch (e: Exception) {
        }
        binding.tvUserProfileNameID.text = data.name
        binding.tvUserFollowerCount.text = data.meta.followerCount + " followers"
        if (data.follower?.isNotEmpty() == true) {
            isRelated = true
            when (data.follower[0].relatableType) {
                "follow" -> {
                    setIconAndColor(
                        binding.tvUserFollow,
                        R.drawable.ic_user_follow_black,
                        colorDark
                    )
                    relatedType = "follow"
                }
                "favourite" -> {
                    setIconAndColor(
                        binding.tvUserFav,
                        R.drawable.ic_user_fav_black,
                        colorDark
                    )
                    relatedType = "favourite"
                }
                "block" -> {
                    setIconAndColor(
                        binding.tvUserBlock,
                        R.drawable.ic_user_block_black,
                        colorDark
                    )
                    relatedType = "block"
                }
            }
        } else isRelated = false
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
