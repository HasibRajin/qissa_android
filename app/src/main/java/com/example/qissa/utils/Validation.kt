package com.example.qissa.utils

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.qissa.R
import java.util.regex.Matcher
import java.util.regex.Pattern

class Validation {

    companion object {

        private const val phone_pattern = "^(?:(?:\\+|00)88|01)?\\d{11}$"
        private const val phone_pattern_login = "[0-9]{10}"
        private const val phone_email = ".*[a-zA-Z]+.*"
        private val rPhone: Pattern = Pattern.compile(phone_pattern)
        private val login_phone: Pattern = Pattern.compile(phone_pattern_login)
        private val rEmail: Pattern = Pattern.compile(phone_email)

        fun emailPhoneValidation(emailEt: EditText, emailError: TextView):
            Boolean {

            val email_phone = emailEt.text.toString().replace("\\s".toRegex(), "")
            var m: Matcher = rPhone.matcher(email_phone.trim())
            var m2: Matcher = rEmail.matcher(email_phone.trim())

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_phone).matches() ||
                m.find()
            ) {

                emailError.visibility = View.GONE
                return true
            } else {

                emailError.visibility = View.VISIBLE
                if (email_phone.isEmpty()) {
                    emailError.text = "Email or Phone Number required"
                    return false
                } else if (m2.find()) {
                    emailError.text = "Email is not valid"
                    return false
                } else {
                    emailError.text = "Phone Number is not valid"
                    return false
                    // binding.etEmailID.filters= arrayOf(InputFilter.LengthFilter(11))
                }
            }
        }

        fun emailValidation(emailEt: EditText, emailError: TextView): Boolean {
            val email = emailEt.text.toString().replace("\\s".toRegex(), "")

            var isEmailValid = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError.visibility = View.VISIBLE
                emailError.setText(R.string.email_required)
                false
            } else {
                emailError.visibility = View.GONE
                true
            }
            return isEmailValid
        }

        fun phoneValidation(emailEt: EditText, phoneError: TextView):
            Boolean {

            val phone = emailEt.text.toString().replace("\\s".toRegex(), "")
            var m: Matcher = rPhone.matcher(phone.trim())
            return if (m.find()) {

                phoneError.visibility = View.GONE
                true
            } else {
                phoneError.text = "Phone Number is not valid"
                false
            }
        }

        fun phoneNumberValidateEt(phoneEt: EditText): Boolean {

            val phone = phoneEt.text.toString()
            if (phone.isEmpty()) {

                return false
            } else return phone.length == 10
        }

        fun phoneNumberValidateEtForgot(phoneEt: EditText): Boolean {

            val phone = phoneEt.text.toString()
            if (phone.isEmpty()) {
                return false
            } else return phone.length == 10
        }

        fun loginPhoneNumberValidate(emailEt: EditText, emailError: TextView, context: Context):
            Boolean {

            val email = emailEt.text.toString().replace("\\s".toRegex(), "")
            var m: Matcher = login_phone.matcher(email.trim())
            var tureBoolean = if (m.find()
            ) {
                emailError.visibility = View.GONE
                true
            } else {

                emailError.visibility = View.VISIBLE
                if (email.isEmpty() || email.length < 11) {
                    emailError.text = context.getString(R.string.email_hint)
                } else {
                    emailError.text = context.getText(R.string.email_hint)
                }

                false
            }

            return tureBoolean
        }

        fun passwordValidation(etPassword: EditText, passError: TextView):
            Boolean {

            var isPasswordValid = if (etPassword.text.toString().length < 5) {
                passError.visibility = View.VISIBLE
                passError.setText(R.string.password_required)
                false
            } else {
                passError.visibility = View.GONE
                true
            }
            return isPasswordValid
        }

        fun nameValidation(etName: EditText, nameError: TextView): Boolean {

            val nameInput: String = etName.text.toString()
            return if (nameInput.length >= 2) {
                nameError.visibility = View.GONE
                true
            } else {
                nameError.visibility = View.VISIBLE
                nameError.setText(R.string.name_error)
                false
            }
        }

        fun addressValidation(etAddress: EditText, addressError: TextView): Boolean {

            val addressInput: String = etAddress.text.toString()
            if (!addressInput.isEmpty()) {
                addressError.visibility = View.GONE
                return true
            } else {
                addressError.visibility = View.VISIBLE
                addressError.text = "Enter your address"
                return false
            }
        }

        fun commonFieldValidation(commonEt: EditText): Boolean {

            val common = commonEt.text.toString()
            if (common.isEmpty()) {
                commonEt.error = "This field is empty"
                return false
            } else {
                return true
            }
        }

        fun passwordShowAndHide(etPassword: EditText, isPasswordShow: Boolean): Boolean {
            if (isPasswordShow) {
                etPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()

                etPassword.setSelection(etPassword.text.toString().length)
                return false
            } else {
                etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                etPassword.setSelection(etPassword.text.toString().length)
                return true
            }
        }
    }
}
