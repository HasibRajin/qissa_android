package com.example.qissa.dialogue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.qissa.R
import com.example.qissa.databinding.ModalBottomSheetContentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: ModalBottomSheetContentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.modal_bottom_sheet_content, container, false
        )
        return binding.root
    }

    companion object {
        const val TAG = "com.example.qissa.dialogue.ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
