package com.example.qissa.ui.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.qissa.R
import com.example.qissa.databinding.ActivityMainBinding
import com.example.qissa.dialogue.LoginBottomDialogue
import com.example.qissa.dialogue.SignupBottomsDialogue
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.network.ApiException
import com.example.qissa.ui.dashboard.DashboardActivity
import com.example.qissa.ui.landing.LoginViewModel
import com.example.qissa.utils.SharedPreference
import com.example.qissa.viewModels.DataShareViewModel
import com.example.qissa.viewModels.TestViewModel
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CommonMethods {
    private lateinit var binding: ActivityMainBinding
    private val RC_SIGN_IN = 1000
    private val testViewModel: TestViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val sharedViewModel: DataShareViewModel by viewModels()

    private val EMAIL = "email"
    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initObservers()
        testViewModel.getTestData()
//        postViewModel.getPost(1)
        binding.buttonGetStart.setOnClickListener {
            Toast.makeText(applicationContext, "BTN", Toast.LENGTH_SHORT).show()
            val signupDialogue = SignupBottomsDialogue()

//            signupDialogue.show(supportFragmentManager, SignupBottomsDialogue.TAG)
            startActivity(Intent(applicationContext, DashboardActivity::class.java))

//            val modalBottomSheet = ModalBottomSheet()
//            modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
        }
        binding.buttonHaveAccount.setOnClickListener {
            Toast.makeText(applicationContext, "BTNLogin", Toast.LENGTH_SHORT).show()
            val loginDialogue = LoginBottomDialogue()

            loginDialogue.show(supportFragmentManager, LoginBottomDialogue.TAG)
        }

//        FacebookSdk.sdkInitialize(applicationContext)
//        AppEventsLogger.activateApp(application)
//        callbackManager = CallbackManager.Factory.create()
//
//        binding.loginButton.setReadPermissions(listOf(EMAIL))
//        // If you are using in a fragment, call loginButton.setFragment(this);
//
//        // Callback registration
//        // If you are using in a fragment, call loginButton.setFragment(this);
//
//        // Callback registration
//        binding.loginButton.registerCallback(
//            callbackManager,
//            object : FacebookCallback<LoginResult?> {
//
//                override fun onCancel() {
//                    // App code
//                }
//
//                override fun onError(exception: FacebookException) {
//                    // App code
//                }
//
//                override fun onSuccess(result: LoginResult?) {
//                    Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//        )

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.googleSignInButton.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

//        binding.buttonGetStart.setOnClickListener {
//            mGoogleSignInClient.signOut()
//                .addOnCompleteListener(
//                    this
//                ) {
//                    Toast.makeText(applicationContext, "logout success", Toast.LENGTH_SHORT).show()
//                }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data)
//            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun initObservers() {
        loginViewModel.items.observe(
            this
        ) {
            it?.let { loginResponse ->
                if (loginResponse.success) {
                    Log.d(LoginBottomDialogue.TAG, "initObservers: ${loginResponse.data}")
                    sharedPreference.setToken("Bearer" + " " + loginResponse.data.token.token)
                    sharedPreference.setUser(loginResponse.data.user)
                    sharedPreference.setImageUrl(loginResponse.data.user.profile_pic.toString())
                    Toast.makeText(
                        applicationContext,
                        sharedPreference.getUser().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, loginResponse.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Toast.makeText(applicationContext, account.displayName.toString(), Toast.LENGTH_SHORT)
                .show()
            account.displayName?.let {
                account.email?.let { it1 ->
                    loginViewModel.doSocialLogin(
                        it,
                        it1, account.photoUrl.toString()
                    )
                }
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.message)
            Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
        }
    }
}
