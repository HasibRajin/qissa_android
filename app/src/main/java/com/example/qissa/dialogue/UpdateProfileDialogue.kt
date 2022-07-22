package com.example.qissa.dialogue

import android.animation.Animator
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.qissa.R
import com.example.qissa.databinding.UpdateProfileInfoBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.models.Data
import com.example.qissa.ui.profile.UserViewModel
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.hideKeyboard
import com.example.qissa.viewModels.UpdateProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UpdateProfileDialogue :
    BottomSheetDialogFragment(), CommonMethods, View.OnClickListener {

    private val updateViewModel: UpdateProfileViewModel by viewModels()
    private val viewModel: UserViewModel by viewModels()

    @Inject
    lateinit var sharedPreference: SharedPreference
    private lateinit var binding: UpdateProfileInfoBinding

    var isUserAddressValid: Boolean = false
    var isUserQualificationValid: Boolean = false
    private var isUserGenderValid: Boolean = false
    private var isUserBirthValid: Boolean = false
    var gender = ""
    var isGenderCheck = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UpdateProfileInfoBinding.inflate(inflater, container, false)
        initObservers()
        checkUserInputIsValidate()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = (dialog as BottomSheetDialog).behavior
        sharedPreference.getUsrProfile()?.let { setProfileInfo(it) }
        binding.buttonUpdate.setOnClickListener(this)
        binding.containerQualificationLayout.setEndIconOnClickListener {
            binding.edittextQualification.setText("")
        }
        binding.containerAddressLayout.setEndIconOnClickListener {
            binding.edittextAddress.setText("")
        }
        binding.edittextQualification.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.edittextAddress.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.containerBirth.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.containerGender.setOnFocusChangeListener { _, _ ->
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.edittextAddress.addTextChangedListener(textWatchers)
        binding.edittextQualification.addTextChangedListener(textWatchers)

        binding.edittextBirth.setOnClickListener {
            setDate()
            if (binding.edittextBirth.text.toString().isNotEmpty()) {
                isUserBirthValid = true
            }
            checkUserInputIsValidate()
        }
        binding.containerBirthInputLayout.setEndIconOnClickListener {
            setDate()
            if (binding.edittextBirth.text.toString().isNotEmpty()) {
                isUserBirthValid = true
            }
            checkUserInputIsValidate()
        }

        binding.rbGender.setOnCheckedChangeListener { group, checkedId ->
            checkUserInputIsValidate()
            if (checkedId == R.id.rb_male) {
                isGenderCheck()
                gender = "male"
                checkUserInputIsValidate()
                // / setRbButtonColor(binding.rbMale)
                binding.rbMale.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.yellow_100
                )
                binding.rbFemale.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
                binding.rbOthers.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
            } else if (checkedId == R.id.rb_female) {
                isGenderCheck()
                gender = "female"
                checkUserInputIsValidate()
                // setRbButtonColor(binding.rbFemale)

                binding.rbFemale.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.yellow_100
                )
                binding.rbMale.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
                binding.rbOthers.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
            } else {
                isGenderCheck()
                gender = "other"
                checkUserInputIsValidate()
                //   setRbButtonColor(binding.rbOthers)
                binding.rbOthers.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.yellow_100
                )
                binding.rbMale.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
                binding.rbFemale.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
            }
        }
    }

    private fun isGenderCheck() {
        isUserGenderValid = isGenderCheck != 0
        isGenderCheck++
    }

    private var textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (binding.edittextQualification.isFocused) {
                isUserAddressValid = true
                checkUserInputIsValidate()
            }
            if (binding.edittextAddress.isFocused) {
                isUserQualificationValid = true
                checkUserInputIsValidate()
            }
//            if (binding.edittextPhone.isFocused) {
//                isUserGenderValid =
//                    Validation.phoneValidation(binding.edittextPhone, binding.tvPhoneError)
//                checkUserInputIsValidate()
//            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // do something
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // do something
        }
    }

    private fun checkUserInputIsValidate() {
        val isEnable =
            isUserQualificationValid || isUserGenderValid || isUserAddressValid || isUserBirthValid
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

        const val TAG = "com.example.qissa.dialogue.UpdateProfileInfoDialogue"
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
            updateViewModel.updateProfileInfo(
                it,
                binding.edittextBirth.text.toString(),
                gender,
                binding.edittextQualification.text.toString(),
                binding.edittextAddress.text.toString(),
            )
        }
    }

    private fun goToSignup() {
        // write your code here
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -8)
        val datepicker = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy.MMM.dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.edittextBirth.setText(sdf.format(cal.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datepicker.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        datepicker.datePicker.maxDate = calendar.timeInMillis
        datepicker.show()
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

    private fun setProfileInfo(profileInfo: Data) {

        if (profileInfo.profile != null) {

            try {
                if (profileInfo.profile.address.toString()
                    .isNotEmpty() && !profileInfo.profile.address?.equals("null")!!
                ) {
                    binding.edittextAddress.setText(profileInfo.profile.address.toString())
                }
            } catch (e: Exception) {
            }

            try {
                if (profileInfo.profile.education.toString()
                    .isNotEmpty() && !profileInfo.profile.phone?.equals("null")!!
                ) {
                    binding.edittextQualification.setText(profileInfo.profile.education.toString())
                }
            } catch (e: Exception) {
            }
            try {
                binding.edittextBirth.setText(
                    profileInfo.profile.dateOfBirth.toString().split("T")[0]
                )
            } catch (e: Exception) {
            }
            try {
                if (profileInfo.profile?.gender.toString() == "male") {
                    binding.rbMale.isChecked = true
                    gender = "male"
                    //  setRbButtonColor(binding.rbMale)
                    binding.rbMale.buttonTintList = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.yellow_100
                    )
                } else if (profileInfo.profile?.gender.toString() == "female") {
                    gender = "female"
                    binding.rbFemale.isChecked = true
                    //  setRbButtonColor(binding.rbFemale)
                    binding.rbFemale.buttonTintList = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.yellow_100
                    )
                } else if (profileInfo.profile?.gender.toString() == "other") {
                    gender = "other"
                    binding.rbOthers.isChecked = true
                    binding.rbOthers.buttonTintList = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.yellow_100
                    )
                }
            } catch (e: Exception) {
            }

//            try {
//                if (profileInfo.profile.phone.toString()
//                    .isNotEmpty() && !profileInfo.profile.phone?.equals("null")!!
//                ) {
//                    binding.edittextPhone.setText(profileInfo.profile.phone.toString())
//                }
//            } catch (e: Exception) {
//            }
//
//            try {
//                if (profileInfo.profile.phone.toString()
//                    .isNotEmpty() && !profileInfo.profile.phone?.equals("null")!!
//                ) {
//                    binding.edittextPhone.setText(profileInfo.profile.phone.toString())
//                }
//            } catch (e: Exception) {
//            }
        }
    }

    interface UpdateDialogueListener {
        fun updateProfileInfo()
    }
}
