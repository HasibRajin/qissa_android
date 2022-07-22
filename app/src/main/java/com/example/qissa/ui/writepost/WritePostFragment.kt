package com.example.qissa.ui.writepost

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.qissa.BuildConfig
import com.example.qissa.R
import com.example.qissa.databinding.FragmentWritePostBinding
import com.example.qissa.dialogue.ImageUploadDialogue
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.models.DataXXXXXXXXXXXX
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.TopicViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class WritePostFragment :
    Fragment(),
    CommonMethods,
    ImageUploadDialogue.ImageListener {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentWritePostBinding
    lateinit var bottomSheetDialog: ImageUploadDialogue
    private var IS_CAMERAORGELLERAYIMAGE_PICK_CODE = 2000
    private val REQUEST_IMAGE_CAPTURE = 200
    private val IMAGE_PICK_CODE = 1000
    private var currentPhotoPath: File? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private var filePath: Uri? = null
    private var imageLink: String? = null
    private var topicId = 1
    var topicList = ArrayList<String>()
    var topicIdList = ArrayList<Int>()

    private val viewModel: CreatePostViewModel by viewModels()
    private val topicViewModel: TopicViewModel by viewModels()

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topicViewModel.getTopics()
        ImageUploadDialogue.imageListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWritePostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.setAppTitle(getString(R.string.qissa))
        checkPostText()
        initListeners()
        initObservers()
        storage = FirebaseStorage.getInstance()
        binding.addImage.setOnClickListener {
            bottomSheetDialog = ImageUploadDialogue()
            bottomSheetDialog.show(parentFragmentManager, "this")
        }
        binding.buttonPost.setOnClickListener {
            if (checkPostText()) {
                callCreatePost()
            } else {
                Toast.makeText(context, "please add stories with title", Toast.LENGTH_SHORT).show()
            }
        }
        binding.cancelImageView.setOnClickListener {
            filePath = null
            binding.image.gone()
            binding.addImage.visible()
        }
        binding.writePostEdittext.addTextChangedListener(textWatchers)
    }

    private var textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (binding.writePostEdittext.isFocused) checkPostText()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // do something
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // do something
        }
    }

    private fun checkPostText(): Boolean {
        return if (binding.writePostEdittext.text.length >= 2) {
//            binding.buttonPost.isEnabled = true
            binding.buttonPost.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#4F5D73"))
            true
        } else {
//            binding.buttonPost.isEnabled = false
            binding.buttonPost.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#C4C4C4"))
            false
        }
    }

    private fun callCreatePost() {
        sharedPreference.getToken()?.let {
            viewModel.createPost(
                it,
                binding.textviewSearch.text.toString(),
                binding.writePostEdittext.text.toString(),
                imageLink,
                topicId
            )
        }
    }

    private fun setTopic(data: List<DataXXXXXXXXXXXX>) {

        for (topic in data) {
            topic.name.let { topicList.add(it) }
        }
        val option = binding.spinnerTopic
//        var topicList =
//            arrayOf("Select Type", "Post Graduate", "Graduate", "Under Graduate", "HSC", "SSC")
        option.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            topicList
        )
        binding.spinnerTopic.setSelection(0)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                topicId = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
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

        viewModel.items.observe(
            viewLifecycleOwner
        ) {
            it?.let { createPostResponse ->
                if (createPostResponse.success) {
                    listener?.gotoFragment(R.id.action_writePostFragment_to_homeFragment)
                } else {
                    Toast.makeText(context, createPostResponse.message, Toast.LENGTH_LONG).show()

                    Snackbar.make(
                        binding.root,
                        createPostResponse.message,
                        Snackbar.LENGTH_LONG
                    )
                }
            }
        }

        viewModel.eventShowLoading
            .observe(
                viewLifecycleOwner,
                Observer {

                    it?.apply {
                        if (it == true) {
                            listener?.showLoading()
                        } else {
                            listener?.hideLoading()
                        }
                    }
                }
            )
        topicViewModel.items.observe(
            viewLifecycleOwner
        ) { items ->
            if (items != null) {
                setTopic(items.data)
            } else {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data?.data != null) {
                filePath = data.data
                binding.image.visible()
                binding.addImage.gone()
                context?.let {
                    Glide.with(it)
                        .load(data.data)
                        .apply(RequestOptions.noTransformation())
                        .into(binding.image)
                }
                uploadPicture()
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            context?.let {
                binding.image.visible()
                binding.addImage.gone()
                Glide.with(it)
                    .load(currentPhotoPath)
                    .centerCrop()
                    .into(binding.image)
                filePath = Uri.fromFile(currentPhotoPath)
                uploadPicture()
            }
        }
    }

    private fun uploadPicture() {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        storageRef = storage.reference.child("post/$fileName.jpg")

        filePath?.let { storageRef.putFile(it) }?.addOnSuccessListener { it ->
            val result = it.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLink = it.toString()
            }
        }?.addOnFailureListener {
            Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun takePhoto() {
        bottomSheetDialog.dismiss()
        IS_CAMERAORGELLERAYIMAGE_PICK_CODE = 2000
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cameraPermissionNotGranted = ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

            if (!cameraPermissionNotGranted) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                pickImageCamera()
            }
        } else {
            pickImageCamera()
        }

        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             if (context?.checkSelfPermission(Manifest.permission.CAMERA) ==
                 PackageManager.PERMISSION_DENIED
             ) {
                 // permission denied
                // val permissions = arrayOf(Manifest.permission.CAMERA)
                 // show popup to request runtime permission
               //  requestPermissions(permissions, CAMERAPERMISSION_CODE)
                 requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
             } else {
                 // permission already granted
                 pickImageCamera()
             }
         } else {
             // system OS is < Marshmallow
             pickImageCamera()
         }*/
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (IS_CAMERAORGELLERAYIMAGE_PICK_CODE == 2000) {
                pickImageCamera()
            } else {
                pickImageFromGallery()
            }
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImageCamera() {

//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent

            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                try {
                    currentPhotoPath = createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
                // Continue only if the File was successfully created
                currentPhotoPath?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${BuildConfig.APPLICATION_ID}.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        // Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
            .apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
                Log.e("aaa", "absolutePath: " + absolutePath)
            }
    }

    override fun selectFromGallery() {
        bottomSheetDialog.dismiss()
        IS_CAMERAORGELLERAYIMAGE_PICK_CODE = 3000
        checkGelleryPermission()
    }

    private fun checkGelleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }
}
