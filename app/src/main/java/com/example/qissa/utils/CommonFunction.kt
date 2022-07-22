package com.example.qissa.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.example.qissa.R
import com.example.qissa.dialogue.LoginBottomDialogue
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

class CommonFunction {

    companion object {

        fun passwordShowAndHide(
            context: Context,
            isPassword: Boolean,
            containerNewPasswordLayout: TextInputLayout,
            edittextNewPassword: EditText
        ): Boolean {
            var isPasswordShow = isPassword

            if (isPasswordShow) {
                isPasswordShow = false
                containerNewPasswordLayout.endIconDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.ic_eye)
                edittextNewPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                isPasswordShow = true
                containerNewPasswordLayout.endIconDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.ic_eye_cross)
                edittextNewPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            edittextNewPassword.setSelection(edittextNewPassword.text.toString().length)
            return isPasswordShow
        }

        fun checkIsKeyboardOpen(v: View?): Boolean {
            val visibleBounds = Rect()
            v?.getWindowVisibleDisplayFrame(visibleBounds)
            val heightDiff = v?.height?.minus(visibleBounds.height())
            val isOpen = heightDiff!! > 0
            Timber.tag(LoginBottomDialogue.TAG).d("checkIsKeyboardOpen: %s", isOpen)
            return isOpen
        }

        fun setIconAndColor(view: TextView, drawable: Int, color: String) {
            view.setCompoundDrawablesWithIntrinsicBounds(
                drawable,
                0,
                0,
                0
            )
            view.setTextColor(Color.parseColor(color))
        }
    }
}
