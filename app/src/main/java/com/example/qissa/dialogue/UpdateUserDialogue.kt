package com.example.qissa.dialogue

import android.animation.Animator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.qissa.R
import com.example.qissa.databinding.UpdateUserInfoBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.models.Data
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.Validation
import com.example.qissa.utils.extendFunctions.hideKeyboard
import com.example.qissa.viewModels.UpdateProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpdateUserDialogue :
    BottomSheetDialogFragment(), CommonMethods, View.OnClickListener {

    private val updateViewModel: UpdateProfileViewModel by viewModels()

    @Inject
    lateinit var sharedPreference: SharedPreference
    private lateinit var binding: UpdateUserInfoBinding

    var isUserNameValid: Boolean = false
    var isUserEmailValid: Boolean = false
    var isUserPhoneValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UpdateUserInfoBinding.inflate(inflater, container, false)
        initObservers()
        checkUserInputIsValidate()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = (dialog as BottomSheetDialog).behavior
        sharedPreference.getUsrProfile()?.let { setProfileInfo(it) }
        binding.buttonUpdate.setOnClickListener(this)
        binding.containerPhoneLayout.setEndIconOnClickListener {
            binding.edittextPhone.setText("")
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
        binding.edittextPhone.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.edittextName.addTextChangedListener(textWatchers)
        binding.edittextEmail.addTextChangedListener(textWatchers)
        binding.edittextPhone.addTextChangedListener(textWatchers)
    }

    private var textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (binding.edittextName.isFocused) {
                isUserNameValid =
                    Validation.nameValidation(binding.edittextName, binding.tvNameError)
                checkUserInputIsValidate()
            }
            if (binding.edittextEmail.isFocused) {
                isUserEmailValid =
                    Validation.emailValidation(binding.edittextEmail, binding.tvEmailError)
                checkUserInputIsValidate()
            }
            if (binding.edittextPhone.isFocused) {
                isUserPhoneValid =
                    Validation.phoneValidation(binding.edittextPhone, binding.tvPhoneError)
                checkUserInputIsValidate()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // do something
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // do something
        }
    }

    private fun checkUserInputIsValidate() {
        val isEnable = isUserEmailValid || isUserPhoneValid || isUserNameValid
        binding.buttonUpdate.isEnabled = isEnable
        if (isEnable) {
            binding.buttonUpdate.setBackgroundResource(R.drawable.button_gradiant)
            binding.buttonUpdate.translationZ = resources.getDimension(R.dimen.margin_d)
        } else {
            binding.buttonUpdate.setBackgroundResource(R.drawable.button_grey)
            binding.buttonUpdate.translationZ = resources.getDimension(R.dimen.margin_z)
        }
    }

    companion object {

        const val TAG = "com.example.qissa.dialogue.UpdateUserInfoDialogue"
        var updateDialogueListener: UpdateDialogueListener? = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.buttonUpdate -> doUpdate()
            binding.textviewLogin -> goToSignup()
        }
    }

    private fun doUpdate() {
        // write your code here
        activity?.hideKeyboard()
        sharedPreference.getToken()?.let {
            updateViewModel.updateUserInfo(
                it,
                binding.edittextName.text.toString(),
                binding.edittextEmail.text.toString(),
                binding.edittextPhone.text.toString(),
            )
        }
    }

    private fun goToSignup() {
        // write your code here
    }

    override fun initObservers() {
        updateViewModel.items.observe(
            this
        ) {
            it?.let { signupResponse ->
                if (signupResponse.success) {
                    updateDialogueListener?.updateProfileInfo()
                } else {
                    Toast.makeText(context, signupResponse.message, Toast.LENGTH_LONG)
                        .show()
                    Snackbar.make(
                        binding.root,
                        signupResponse.message,
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        }
        updateViewModel.eventShowLoading
            .observe(
                viewLifecycleOwner
            ) {
                it?.run {
                    if (this) {
                        showLoading()
                    } else {
                        dialog?.dismiss()
                        hideLoading()
                    }
                }
            }
    }

    private fun setProfileInfo(profileInfo: Data) {
        Toast.makeText(context, profileInfo.toString(), Toast.LENGTH_SHORT).show()
        binding.edittextName.setText(profileInfo.name)
        binding.edittextEmail.setText(profileInfo.email)

        if (profileInfo.profile != null) {

            try {
                if (profileInfo.profile.phone.toString()
                    .isNotEmpty() && !profileInfo.profile.phone?.equals("null")!!
                ) {
                    binding.edittextPhone.setText(profileInfo.profile.phone.toString())
                }
            } catch (e: Exception) {
            }
        }
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

    interface UpdateDialogueListener {
        fun updateProfileInfo()
    }
}
