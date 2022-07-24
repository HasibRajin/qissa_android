package com.example.qissa.ui.dashboard

import android.animation.Animator
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.qissa.R
import com.example.qissa.databinding.ActivityDashboardBinding
import com.example.qissa.databinding.NavigationDrawerLayoutBinding
import com.example.qissa.interfaces.CommonMethods
import com.example.qissa.interfaces.OnFragmentInteractionListener
import com.example.qissa.network.ApiException
import com.example.qissa.ui.landing.LandingFragment
import com.example.qissa.ui.landing.LoginViewModel
import com.example.qissa.utils.SharedPreference
import com.example.qissa.utils.extendFunctions.gone
import com.example.qissa.utils.extendFunctions.visible
import com.example.qissa.viewModels.LogoutViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity :
    AppCompatActivity(),
    CommonMethods,
    OnFragmentInteractionListener,
    LandingFragment.ActivityResultLister {

    lateinit var binding: ActivityDashboardBinding
    private lateinit var navLayoutBinding: NavigationDrawerLayoutBinding
    private lateinit var navController: NavController
    private val logoutViewModel: LogoutViewModel by viewModels()
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val loginViewModel: LoginViewModel by viewModels()
    private val RC_SIGN_IN = 1000

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        LandingFragment.activityResultLister = this
        navLayoutBinding = binding.navView
        navController = findNavController(R.id.nav_host_fragment)
        initListeners()
        initObservers()
        if (sharedPreference.getToken() != null) {
            gotoFragment(R.id.homeFragment)
            updateProfileData()
        } else {
            gotoFragment(R.id.landingFragment)
        }
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.appBar.imgMenu.setOnClickListener(this::onClick)
        binding.appBar.imgSearch.setOnClickListener(this::onClick)

        binding.appBar.imgSearch.setOnClickListener(this::onClick)

        navLayoutBinding.topBar.imgBack.setOnClickListener(this::onClick)

        navLayoutBinding.containerProfile.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavTopic.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavWrite.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavChangePassword.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavStories.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavFollowers.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavSaved.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavLanguage.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavHelp.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavPolicies.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavRate.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavShare.setOnClickListener(this::onClick)

        navLayoutBinding.tvNavLogout.setOnClickListener(this::onClick)
    }

    override fun initListeners() {
        super.initListeners()
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    gotoFragment(R.id.homeFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.book -> {
                    gotoFragment(R.id.booksFragment)

                    return@setOnItemSelectedListener true
                }
                R.id.chat -> {
                    gotoFragment(R.id.chatFragment)

                    return@setOnItemSelectedListener true
                }
                R.id.notification -> {
                    gotoFragment(R.id.notificationFragment)

                    return@setOnItemSelectedListener true
                }
                R.id.question -> {
                    gotoFragment(R.id.questionFragment)

                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.homeFragment -> {
                    onDestinationChange(R.id.home)
                    return@addOnDestinationChangedListener
                }
                R.id.questionFragment -> {
                    onDestinationChange(R.id.question)
                    return@addOnDestinationChangedListener
                }
                R.id.chatFragment -> {
                    onDestinationChange(R.id.chat)
                    return@addOnDestinationChangedListener
                }
                R.id.booksFragment -> {
                    onDestinationChange(R.id.book)
                    return@addOnDestinationChangedListener
                }
                R.id.notificationFragment -> {
                    onDestinationChange(R.id.notification)
                    return@addOnDestinationChangedListener
                }
                else -> {
                    binding.containerAppBar.gone()
                    binding.containerDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }
    }

    private fun onDestinationChange(destinationId: Int) {
        binding.bottomNavigation.selectedItemId = destinationId
        binding.containerAppBar.visible()
        binding.containerDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onBackPressed() {
        if (sharedPreference.getToken() != null) {
            super.onBackPressed()
        }
    }

    private fun onClick(v: View?) {
        when (v) {

            binding.appBar.imgMenu -> openCloseNavigationDrawer(v)
            binding.appBar.imgSearch -> {
                when (binding.bottomNavigation.selectedItemId) {
                    R.id.home -> gotoFragment(R.id.searchFragment)
                    R.id.question -> gotoFragment(R.id.searchQuestionFragment)
                    R.id.chat -> gotoFragment(R.id.searchQuestionFragment)
                    R.id.book -> gotoFragment(R.id.searchQuestionFragment)
                    R.id.notification -> gotoFragment(R.id.searchQuestionFragment)
                    else -> gotoFragment(R.id.searchFragment)
                }
            }
            navLayoutBinding.topBar.imgBack -> openCloseNavigationDrawer(v)

            navLayoutBinding.containerProfile -> gotoFragment(R.id.profileFragment)

            navLayoutBinding.tvNavWrite -> gotoFragment(R.id.writePostFragment)

            navLayoutBinding.tvNavTopic -> gotoOtherScreen()

            navLayoutBinding.tvNavChangePassword -> gotoOtherScreen()

            navLayoutBinding.tvNavStories -> gotoFragment(R.id.action_homeFragment_to_myStoriesFragment)

            navLayoutBinding.tvNavFollowers -> gotoFragment(R.id.action_homeFragment_to_myFollowerFragment)

            navLayoutBinding.tvNavSaved -> gotoOtherScreen()

            navLayoutBinding.tvNavLanguage -> gotoOtherScreen()

            navLayoutBinding.tvNavHelp -> gotoOtherScreen()

            navLayoutBinding.tvNavPolicies -> gotoOtherScreen()

            navLayoutBinding.tvNavRate -> gotoOtherScreen()

            navLayoutBinding.tvNavShare -> gotoOtherScreen()

            navLayoutBinding.tvNavLogout -> callLogoutApi()
        }
    }

    private fun callLogoutApi() {
        sharedPreference.getToken()?.let { logoutViewModel.doLogout(it) }
    }

    private fun gotoOtherScreen() {
        // write your code here
    }

    private fun setHomeFragment(): Boolean {
        return true
    }

    override fun initObservers() {
        logoutViewModel.items.observe(
            this,
            Observer {
                it?.let { reactionResponse ->
                    if (reactionResponse.success) {
                        sharedPreference.reset()
                        gotoFragment(R.id.landingFragment)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            reactionResponse.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )

        logoutViewModel.eventShowLoading
            .observe(
                this
            ) {
                it?.run {
                    if (this) {
                        showNavLoading()
                    } else {
                        hideNavLoading()
                    }
                }
            }
    }

    override fun setAppTitle(title: String) {
        setTitle(title)
    }

    override fun gotoFragment(@IdRes destinationResId: Int) {

        if (navController.currentDestination == null) {
            navController.navigate(destinationResId)
        } else {

            navController.currentDestination?.let {
                if (it.id != destinationResId) {
                    navController.navigate(destinationResId)
                }
            }
        }
    }

    override fun updateProfileData() {
        navLayoutBinding.tvUserProfileNameID.text = sharedPreference.getUser()?.name.toString()
        navLayoutBinding.tvNavEmail.text = sharedPreference.getUser()?.email.toString()
        setImage()
    }

    override fun gotoFragment(@IdRes destinationResId: Int, data: Bundle) {
        if (navController.currentDestination == null) {

            navController.navigate(destinationResId, data)
        } else {

            navController.currentDestination?.let {
                if (it.id != destinationResId) {
                    navController.navigate(destinationResId, data)
                }
            }
        }
    }

    override fun gotoFragment(navDirections: NavDirections) {
        navController.navigate(navDirections)
    }

    private fun openCloseNavigationDrawer(view: View) {
        if (binding.containerDrawer.isDrawerOpen(GravityCompat.END)) {
            binding.containerDrawer.closeDrawer(GravityCompat.END)
        } else {
            binding.containerDrawer.openDrawer(GravityCompat.END)
        }
    }

    override fun goBack() {

        onBackPressed()
    }

    override fun showSnackbar(message: String) {
        TODO("Not yet implemented")
    }

    override fun showSnackbar(message: String, buttonText: String, action: (View) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun showLoading() {
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

    override fun hideLoading() {
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

    private fun showNavLoading() {
        try {
            val loadingAnimation = navLayoutBinding.navLoadingView.globalLoadingLayout.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        navLayoutBinding.navLoadingView.globalLoadingLayout.alpha = 0f
                        navLayoutBinding.navLoadingView.globalLoadingLayout.visibility =
                            View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator) {}
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })

            loadingAnimation?.start()
        } catch (e: Exception) {
        }
    }

    private fun hideNavLoading() {
        try {
            val loadingAnimation = navLayoutBinding.navLoadingView.globalLoadingLayout.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        navLayoutBinding.navLoadingView.globalLoadingLayout.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                })

            loadingAnimation?.start()
        } catch (e: Exception) {
        }
    }

    override fun onResume() {
        super.onResume()
        setImage()
    }

    private fun setImage() {
        if (sharedPreference.getUser()?.profile_pic.isNullOrEmpty()
        ) {
            Glide.with(this).load(R.drawable.ic_circle_icons_profile)
                .apply(RequestOptions.circleCropTransform())
                .into(navLayoutBinding.profileImageView)
        } else {
            Glide.with(this).load(sharedPreference.getImageUrl())
                .placeholder(R.drawable.ic_circle_icons_profile)
                .apply(RequestOptions.circleCropTransform())
                .into(navLayoutBinding.profileImageView)
        }
    }

    override fun openSomeActivityForResult() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (resultCode != RESULT_CANCELED) {

            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data)
//            super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun initLoginObservers() {
        loginViewModel.items.observe(
            this
        ) {
            it?.let { loginResponse ->
                if (loginResponse.success) {
                    sharedPreference.setToken("Bearer" + " " + loginResponse.data.token.token)
                    sharedPreference.setUser(loginResponse.data.user)
                    sharedPreference.setImageUrl(loginResponse.data.user.profile_pic.toString())
                    gotoFragment(R.id.action_landingFragment_to_homeFragment)
                    updateProfileData()
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
            account.displayName?.let {
                account.email?.let { it1 ->
                    loginViewModel.doSocialLogin(
                        it,
                        it1, account.photoUrl.toString()
                    )
                }
            }
            doLogout()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.message)
            Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doLogout() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(
                this
            ) {
                Log.d(TAG, "doLogout: success")
                initLoginObservers()
            }
    }
}
