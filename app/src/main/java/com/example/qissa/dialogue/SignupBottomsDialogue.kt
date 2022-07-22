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
import com.example.qissa.databinding.SignupDialogueBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.ui.landing.SignupViewModel
import com.example.qissa.utils.CommonFunction
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.Validation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupBottomsDialogue :
    BottomSheetDialogFragment(), CommonMethods, View.OnClickListener {
    private val signupViewModel: SignupViewModel by viewModels()

    @Inject
    lateinit var sharedPreference: SharedPreference
    private lateinit var binding: SignupDialogueBinding
//    var activity = activity

    var isUserNameValid: Boolean = false
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
            inflater, R.layout.signup_dialogue, container, false
        )
        initObservers()
        checkUserInputIsValidate()
        isPasswordShow = true
        state = true
        isPasswordShow = CommonFunction.passwordShowAndHide(
            requireContext(),
            isPasswordShow,
            binding.containerNewPasswordLayout,
            binding.edittextNewPassword
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = (dialog as BottomSheetDialog).behavior
/*     behavior.addBottomSheetCallback(
        object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    /* write your code here */
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //do something
            }


        }) */
//        checkIsKeyboardOpen(view)
        binding.buttonSignup.setOnClickListener(this)
        binding.containerNewPasswordLayout.setEndIconOnClickListener {
            isPasswordShow =
                CommonFunction.passwordShowAndHide(
                    requireContext(),
                    isPasswordShow,
                    binding.containerNewPasswordLayout,
                    binding.edittextNewPassword
                )
        }
        binding.containerNameInputLayout.setEndIconOnClickListener {
            binding.edittextName.setText("")
        }
        binding.containerEmailLayout.setEndIconOnClickListener {
            binding.edittextEmail.setText("")
        }
        binding.edittextName.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.edittextEmail.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.edittextNewPassword.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.edittextName.addTextChangedListener(textWatchers)
        binding.edittextEmail.addTextChangedListener(textWatchers)
        binding.edittextNewPassword.addTextChangedListener(textWatchers)
    }

    private var textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (binding.edittextName.isFocused) {
                checkNameIsValid()
                checkUserInputIsValidate()
                if (isUserNameValid) {
                    binding.imageName.setImageResource(R.drawable.ic_user_name_black)
                } else {
                    binding.imageName.setImageResource(R.drawable.ic_user_name)
                }
            }
            if (binding.edittextEmail.isFocused) {
                checkEmailIsValid()
                checkUserInputIsValidate()
                if (isUserEmailValid) {
                    binding.imgUserEmail.setImageResource(R.drawable.ic_user_email_black)
                } else {
                    binding.imgUserEmail.setImageResource(R.drawable.ic_user_email)
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

    private fun checkNameIsValid() {

        isUserNameValid = Validation.nameValidation(binding.edittextName, binding.tvNameError)
    }

    private fun checkEmailIsValid() {
        isUserEmailValid = Validation.emailValidation(binding.edittextEmail, binding.tvEmailError)
    }

    private fun checkPasswordIsValid() {

        isUserPasswordValid =
            Validation.passwordValidation(binding.edittextNewPassword, binding.tvPasswordError)
    }

    private fun checkUserInputIsValidate() {
        val isEnable = isUserEmailValid && isUserPasswordValid && isUserNameValid
        binding.buttonSignup.isEnabled = isEnable
        if (isEnable) {
            binding.buttonSignup.setBackgroundResource(R.drawable.button_gradiant)
            binding.buttonSignup.translationZ = resources.getDimension(R.dimen.margin_d)
        } else {
            binding.buttonSignup.setBackgroundResource(R.drawable.button_grey)
            binding.buttonSignup.translationZ = resources.getDimension(R.dimen.margin_z)
        }
    }

    companion object {

        const val TAG = "com.example.qissa.dialogue.LoginBottomSheet"
        var loginDialogueListener: LoginDialogueListener? = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.buttonSignup -> doSignup()
            binding.textviewLogin -> goToSignup()
        }
    }

    private fun doSignup() {
        // write your code here
        Log.d(TAG, "doSignIn: Signing clicked")
        signupViewModel.doSignup(
            binding.edittextName.text.toString(),
            binding.edittextEmail.text.toString(),
            binding.edittextNewPassword.text.toString(),
            binding.edittextNewPassword.text.toString(),
        )
    }

    private fun goToSignup() {
        // write your code here
    }

    override fun initObservers() {

        signupViewModel.items.observe(
            this
        ) {
            it?.let { signupResponse ->
                if (signupResponse.success) {
                    Log.d(TAG, "initObservers: ${signupResponse.data}")
                    sharedPreference.setToken("Bearer" + " " + signupResponse.data.token.token)
                    sharedPreference.setUser(signupResponse.data.user)
                    sharedPreference.setImageUrl(signupResponse.data.user.profile_pic.toString())
                    loginDialogueListener?.gotoHomePage()
                    loginDialogueListener?.updateProfData()
                    dialog?.dismiss()
                } else {
                    Toast.makeText(context, signupResponse.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        signupViewModel.eventShowLoading
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
