package com.example.qissa.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.qissa.R
import com.example.qissa.databinding.FragmentAboutBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.models.Data
import com.example.qissa.utils.SharedPreference
import com.example.qissa.viewModels.DataShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AboutFragment :
    Fragment(),
    CommonMethods {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentAboutBinding
    private val dataSharedViewModel: DataShareViewModel by viewModels({ requireParentFragment() })

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val name = arguments?.getString("name")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.setAppTitle(getString(R.string.qissa))
        initListeners()

        initObservers()
        Log.d("setUserInfo", "setUserInfo:${dataSharedViewModel.userInfo.value} ")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun initListeners() {
    }

    override fun initObservers() {
        dataSharedViewModel.userInfo.observe(
            viewLifecycleOwner
        ) { items ->
            setProfileInfo(items)
        }
    }

    private fun setProfileInfo(profileInfo: Data) {
        binding.tvUserName.text = profileInfo.name
        binding.tvUserEmail.text = profileInfo.email

        if (profileInfo.profile != null) {

            try {
                if (profileInfo.profile.phone.toString()
                    .isNotEmpty() && !profileInfo.profile.phone?.equals("null")!!
                ) {
                    binding.tvUserPhone.text = profileInfo.profile.phone.toString()
                }
            } catch (e: Exception) {
            }

            try {
                if (profileInfo.profile.address.toString()
                    .isNotEmpty() && !profileInfo.profile.phone?.equals("null")!!
                ) {
                    binding.tvUserAddress.text = profileInfo.profile.address.toString()
                }
            } catch (e: Exception) {
            }
            try {
                if (profileInfo.profile.dateOfBirth.toString()
                    .isNotEmpty() && !profileInfo.profile.phone?.equals(
                            "null"
                        )!!
                ) {
                    binding.tvUserBirth.text =
                        profileInfo.profile?.dateOfBirth.toString()?.split("T")!![0]
                }
            } catch (e: Exception) {
            }

            try {
                if (profileInfo.profile.education.toString()
                    .isNotEmpty() && !profileInfo.profile.phone?.equals("null")!!
                ) {
                    binding.tvUserEducation.text = profileInfo.profile?.education.toString()
                }
            } catch (e: Exception) {
            }

            try {
                if (profileInfo.profile.gender.toString()
                    .isNotEmpty() && !profileInfo.profile.phone?.equals("null")!!
                ) {
                    binding.tvUserGender.text = profileInfo.profile?.gender.toString()
                }
            } catch (e: Exception) {
            }
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
}
