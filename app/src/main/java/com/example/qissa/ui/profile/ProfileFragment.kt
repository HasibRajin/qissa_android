package com.example.qissa.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.qissa.databinding.FragmentProfileBinding
import com.example.qissa.dialogue.ImageUploadDialogue
import com.example.qissa.dialogue.UpdateProfileDialogue
import com.example.qissa.dialogue.UpdateUserDialogue
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.models.Data
import com.example.qissa.utils.SharedPreference
import com.example.qissa.viewModels.DataShareViewModel
import com.example.qissa.viewModels.UpdateProfileViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment :
    Fragment(),
    CommonMethods,
    ImageUploadDialogue.ImageListener,
    UpdateUserDialogue.UpdateDialogueListener,
    UpdateProfileDialogue.UpdateDialogueListener {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: UserViewModel by viewModels()
    private val updateViewModel: UpdateProfileViewModel by viewModels()

    private val dataSharedViewModel: DataShareViewModel by viewModels()

    lateinit var bottomSheetDialog: ImageUploadDialogue
    private var IS_CAMERAORGELLERAYIMAGE_PICK_CODE = 2000
    private val REQUEST_IMAGE_CAPTURE = 200
    private val IMAGE_PICK_CODE = 1000
    private var currentPhotoPath: File? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private var filePath: Uri? = null

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImageUploadDialogue.imageListener = this
        UpdateUserDialogue.updateDialogueListener = this
        UpdateProfileDialogue.updateDialogueListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        storage = FirebaseStorage.getInstance()
        try {
            if (!sharedPreference.getUsrProfile()?.name.isNullOrEmpty()) {
                setProfileInfo(sharedPreference.getUsrProfile()!!)
            } else {
                callUserApi()
            }
        } catch (e: Exception) {
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setAppTitle(getString(R.string.qissa))
        initListeners()
        initObservers()
        binding.profileImageView.setOnClickListener {
            bottomSheetDialog = ImageUploadDialogue()
            bottomSheetDialog.show(parentFragmentManager, "this")
        }
        binding.tvUserFollower.setOnClickListener {
            listener?.gotoFragment(R.id.action_profileFragment_to_myFollowerFragment)
        }
        binding.tvUserPost.setOnClickListener {
            listener?.gotoFragment(R.id.action_profileFragment_to_myStoriesFragment)
        }
        binding.editImageView2.setOnClickListener {
            val updateDialogue = UpdateProfileDialogue()
            activity?.let { it1 ->
                updateDialogue.show(
                    it1.supportFragmentManager,
                    UpdateProfileDialogue.TAG
                )
            }
        }
        binding.editImageView.setOnClickListener {
            val updateDialogue = UpdateUserDialogue()
            activity?.let { it1 ->
                updateDialogue.show(
                    it1.supportFragmentManager,
                    UpdateUserDialogue.TAG
                )
            }
        }
//        binding.editImageView2.setOnClickListener {
//            val updateDialogue = UpdateUserDialogue()
//            updateDialogue.show(childFragmentManager, LoginBottomDialogue.TAG)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data?.data != null) {
                filePath = data.data

                context?.let {
                    Glide.with(it)
                        .load(data.data)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileImageView)
                }
                uploadPicture()
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            context?.let {
                Glide.with(it)
                    .load(currentPhotoPath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)

                filePath = Uri.fromFile(currentPhotoPath)
                uploadPicture()
            }
        }
    }

    private fun uploadPicture() {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        storageRef = storage.reference.child("profile/$fileName.jpg")

        filePath?.let { storageRef.putFile(it) }?.addOnSuccessListener { it ->
            val result = it.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                var imageLink = it.toString()
                sharedPreference.setImageUrl(imageLink)
                sharedPreference.getToken()
                    ?.let { it1 -> updateViewModel.updateProfileImage(it1, imageLink) }
            }
            Toast.makeText(requireContext(), "upload success", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener {
            Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callUserApi() {
        sharedPreference.getUser()?.let { viewModel.getSingleUserData(it.id, null) }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun initListeners() {

        binding.cancelImageView.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun initObservers() {

        viewModel.items.observe(
            viewLifecycleOwner
        ) { items ->
            if (items != null) {
                Log.d("userInfo", "initObservers:${items.data[0]} ")
                sharedPreference.setUserProfile(items.data[0])
                sharedPreference.setImageUrl(items.data[0].profilePic.toString())
                dataSharedViewModel.setUserInfo(items.data[0])
                setProfileInfo(items.data[0])
                Toast.makeText(requireContext(), items.data[0].toString(), Toast.LENGTH_SHORT)
                    .show()
            } else {
                listener?.showLoading()
            }
        }
        viewModel.eventShowMessage.observe(
            viewLifecycleOwner
        ) {
            it?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
    }

    private fun setProfileInfo(data: Data) {
        binding.tvUserProfileNameID.text =
            data.name
        try {

            if (data.profilePic.toString().isNullOrBlank() || data.profilePic.toString()
                .isNullOrEmpty()

            ) {
                binding.profileImageView.setPadding(16, 16, 16, 16)
                Glide.with(this).load(R.drawable.ic_profile_icon)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            } else {
//                imagePath = data.profilePic?.toString()
                binding.profileImageView.setPadding(3, 3, 3, 3)
                Glide.with(this).load(sharedPreference.getImageUrl())
                    .placeholder(R.drawable.ic_profile_icon)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)
            }
        } catch (e: Exception) {
        }

        binding.tvUserName.text = data.name
        binding.tvUserEmail.text = data.email
        binding.tvUserFollowerCount.text = "${data.meta.followerCount} followers"

        try {
            if (!data.profile.phone.toString()
                .isNullOrEmpty() && !data.profile.phone?.equals("null")!!
            ) {
                binding.tvUserPhone.text = data.profile.phone.toString()
            }
        } catch (e: Exception) {
        }

        try {
            if (!data.profile.address.toString()
                .isNullOrEmpty() && !data.profile.phone?.equals("null")!!
            ) {
                binding.tvUserAddress.text = data.profile.address.toString()
            }
        } catch (e: Exception) {
        }
        try {
            if (!data.profile.dateOfBirth.toString().isNullOrEmpty() && !data.profile.phone?.equals(
                    "null"
                )!!
            ) {
                binding.tvUserBirth.text = data.profile?.dateOfBirth.toString()?.split("T")!![0]
            }
        } catch (e: Exception) {
        }

        try {
            if (!data.profile.education.toString()
                .isNullOrEmpty() && !data.profile.phone?.equals("null")!!
            ) {
                binding.tvUserEducation.text = data.profile?.education.toString()
            }
        } catch (e: Exception) {
        }

        try {
            if (!data.profile.gender.toString()
                .isNullOrEmpty() && !data.profile.phone?.equals("null")!!
            ) {
                binding.tvUserGender.text = data.profile?.gender.toString()
            }
        } catch (e: Exception) {
        }
        listener?.hideLoading()
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

    override fun updateProfileInfo() {
        callUserApi()
    }
}
