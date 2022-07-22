package com.example.qissa.dialogue

import android.animation.Animator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.qissa.R
import com.example.qissa.databinding.LoginDialogueBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.ui.landing.LoginViewModel
import com.example.qissa.utils.CommonFunction
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.Validation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginBottomDialogue :
    BottomSheetDialogFragment(), CommonMethods, View.OnClickListener {
    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sharedPreference: SharedPreference
    private lateinit var binding: LoginDialogueBinding

    var isUserEmailValid: Boolean = false
    var isUserPasswordValid: Boolean = false
    private var isPasswordShow: Boolean = true
    var state: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.login_dialogue, container, false
        )
        checkUserInputIsValidate()
//        initObservers()
        isPasswordShow = true
        isPasswordShow = CommonFunction.passwordShowAndHide(
            requireContext(),
            isPasswordShow,
            binding.containerNewPasswordLayout,
            binding.edittextNewPassword
        )
        state = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.addBottomSheetCallback(
            object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    // do something
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // do something
                }
            })
//        checkIsKeyboardOpen(view)
        binding.buttonLogin.setOnClickListener(this)
        binding.containerNewPasswordLayout.setEndIconOnClickListener {
            isPasswordShow =
                CommonFunction.passwordShowAndHide(
                    requireContext(),
                    isPasswordShow,
                    binding.containerNewPasswordLayout,
                    binding.edittextNewPassword
                )
        }
        binding.containerEmailLayout.setEndIconOnClickListener {
            binding.edittextEmail.setText("")
        }
//        binding.edittextEmail.setOnTouchListener { v, _ ->
//            v.performClick()
//            if(checkIsKeyboardOpen(view)) {
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//            false
//        }
        binding.edittextEmail.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.edittextNewPassword.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.edittextEmail.addTextChangedListener(textWatchers)
        binding.edittextNewPassword.addTextChangedListener(textWatchers)
    }

    private var textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (binding.edittextEmail.isFocused) {
                checkEmailIsValid()
                checkUserInputIsValidate()
                if (isUserEmailValid) {
                    binding.imageEmail.setImageResource(R.drawable.ic_user_email_black)
                } else {
                    binding.imageEmail.setImageResource(R.drawable.ic_user_email)
                }
            }
            if (binding.edittextNewPassword.isFocused) {
                checkPasswordIsValid()
                checkUserInputIsValidate()
                if (isUserPasswordValid) {
                    binding.imagePassword.setImageResource(R.drawable.ic_lock_black)
                } else {
                    binding.imagePassword.setImageResource(R.drawable.ic_lock_grey)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // do something
//            if(checkIsKeyboardOpen(view)) {
//                val behavior = (dialog as BottomSheetDialog).behavior
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                state =false
//            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // do something
        }
    }

    private fun checkEmailIsValid() {
        isUserEmailValid = Validation.emailValidation(binding.edittextEmail, binding.tvEmailError)
    }

    private fun checkPasswordIsValid() {

        isUserPasswordValid =
            Validation.passwordValidation(binding.edittextNewPassword, binding.tvPasswordError)
    }

    private fun checkUserInputIsValidate() {
        val isEnable = isUserEmailValid && isUserPasswordValid
        binding.buttonLogin.isEnabled = isEnable
        if (isEnable) {
            binding.buttonLogin.setBackgroundResource(R.drawable.button_gradiant)
            binding.buttonLogin.translationZ = resources.getDimension(R.dimen.margin_d)
        } else {
            binding.buttonLogin.setBackgroundResource(R.drawable.button_grey)
            binding.buttonLogin.translationZ = resources.getDimension(R.dimen.margin_z)
        }
    }

    companion object {

        const val TAG = "com.example.qissa.dialogue.LoginBottomSheet"
        var loginDialogueListener: LoginDialogueListener? = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.buttonLogin -> doSignIn()
            binding.textviewForgetPassword -> gotoForgotPassword()
            binding.textviewSignup -> goToSignup()
        }
    }

    private fun doSignIn() {
        // write your code here
        initObservers()
        Log.d(TAG, "doSignInWDFEWRHTRYJTUKYILUYKUTJYWDDEFWGRTRHYJUKILKUYJEFWDW: Signing clicked")
        loginViewModel.doLogin(
            binding.edittextEmail.text.toString(),
            binding.edittextNewPassword.text.toString()
        )
    }

    private fun gotoForgotPassword() {
        // write your code here
    }

    private fun goToSignup() {
        // write your code here
    }

    override fun initObservers() {

        loginViewModel.items.observe(
            this
        ) {
            it?.let { loginResponse ->
                if (loginResponse.success) {
                    Log.d(TAG, "initObservers: ${loginResponse.data}")
                    sharedPreference.setToken("Bearer" + " " + loginResponse.data.token.token)
                    sharedPreference.setUser(loginResponse.data.user)
                    sharedPreference.setImageUrl(loginResponse.data.user.profile_pic.toString())
                    loginDialogueListener?.gotoHomePage()
                    dialog?.dismiss()
                    loginDialogueListener?.updateProfData()
                } else {
                    Toast.makeText(context, loginResponse.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        loginViewModel.eventShowLoading
            .observe(
                viewLifecycleOwner
            ) {
                it?.run {
                    if (this) {
                        showLoading()
                    } else {
                        hideLoading()
                    }
                }
            }
    }

    interface LoginDialogueListener {
        fun gotoHomePage()
        fun updateProfData()
    }

    private fun showLoading() {
        try {
            val loadingAnimation = binding.loadingView.globalLoadingLayout.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        binding.loadingView.globalLoadingLayout.alpha = 0f
                        binding.loadingView.globalLoadingLayout.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator) {}
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })

            loadingAnimation?.start()
        } catch (e: Exception) {
        }
    }

    private fun hideLoading() {
        try {
            val loadingAnimation = binding.loadingView.globalLoadingLayout.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        binding.loadingView.globalLoadingLayout.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                })

            loadingAnimation?.start()
        } catch (e: Exception) {
        }
    }
}
