package com.example.qissa.dialogue

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.qissa.R
import com.example.qissa.databinding.ImageUplaodDialogueLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImageUploadDialogue : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var binding: ImageUplaodDialogueLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.image_uplaod_dialogue_layout, container, false
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.takephotoTVID.setOnClickListener(this)
        binding.selectagalaryID.setOnClickListener(this)

        return binding.root
    }

    companion object {
        var imageListener: ImageListener? = null
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.takephotoTVID) {
                imageListener?.takePhoto()
            } else if (v.id == R.id.selectagalaryID) {

                imageListener?.selectFromGallery()
            }
        }
    }

    interface ImageListener {
        fun takePhoto()
        fun selectFromGallery()
    }
}
