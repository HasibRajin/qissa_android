package com.example.qissa.ui.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.qissa.R
import com.example.qissa.databinding.FragmentLandingBinding
import com.example.qissa.dialogue.LoginBottomDialogue
import com.example.qissa.dialogue.SignupBottomsDialogue
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.ui.dashboard.DashboardActivity
import com.example.qissa.utils.SharedPreference
import com.example.qissa.viewModels.TestViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LandingFragment :
    Fragment(),
    CommonMethods,
    LoginBottomDialogue.LoginDialogueListener,
    SignupBottomsDialogue.LoginDialogueListener {

    private lateinit var binding: FragmentLandingBinding
    private val testViewModel: TestViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private var listener: OnFragmentInteractionListener? = null

    private val EMAIL = "email"
    private lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        LoginBottomDialogue.loginDialogueListener = this
        SignupBottomsDialogue.loginDialogueListener = this
        testViewModel.getTestData()
        binding.buttonGetStart.setOnClickListener {
            val signupDialogue = SignupBottomsDialogue()

            signupDialogue.show(parentFragmentManager, SignupBottomsDialogue.TAG)
//            startActivity(Intent(applicationContext, DashboardActivity::class.java))
        }
        binding.buttonHaveAccount.setOnClickListener {
            val loginDialogue = LoginBottomDialogue()
            loginDialogue.show(parentFragmentManager, LoginBottomDialogue.TAG)
        }

        FacebookSdk.sdkInitialize(requireContext())
        activity?.let { AppEventsLogger.activateApp(it.application) }
        callbackManager = CallbackManager.Factory.create()

        binding.loginButton.setReadPermissions(listOf(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        binding.loginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }

                override fun onSuccess(result: LoginResult?) {
                    Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.googleSignInButton.setOnClickListener {
            activityResultLister?.openSomeActivityForResult()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        listener?.hideLoading()

        initViews()
        initObservers()
        initListeners()
    }

    override fun initObservers() {
        loginViewModel.items.observe(
            viewLifecycleOwner
        ) {
            it?.let { loginResponse ->
                if (loginResponse.success) {
                    Log.d(LoginBottomDialogue.TAG, "initObservers: ${loginResponse.data}")
                    sharedPreference.setToken("Bearer" + " " + loginResponse.data.token.token)
                    sharedPreference.setUser(loginResponse.data.user)
                    sharedPreference.setImageUrl(loginResponse.data.user.profile_pic.toString())
                    Toast.makeText(
                        requireContext(),
                        sharedPreference.getUser().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(requireContext(), DashboardActivity::class.java))
                } else {
                    Toast.makeText(requireContext(), loginResponse.message, Toast.LENGTH_LONG)
                        .show()
                }
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

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        var activityResultLister: ActivityResultLister? = null
    }

    interface ActivityResultLister {
        fun openSomeActivityForResult()
        fun updateProfileData()
    }

    override fun updateProfData() {
        activityResultLister?.updateProfileData()
    }

    override fun gotoHomePage() {
        listener?.gotoFragment(R.id.homeFragment)
    }
}
