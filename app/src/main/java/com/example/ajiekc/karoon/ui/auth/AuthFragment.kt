package com.example.ajiekc.karoon.ui.auth

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ajiekc.karoon.R
import com.example.ajiekc.karoon.extensions.*
import com.example.ajiekc.karoon.repository.SessionRepository
import com.example.ajiekc.karoon.ui.base.BaseFragment
import com.example.ajiekc.karoon.ui.main.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.VKServiceActivity
import com.vk.sdk.api.VKError
import kotlinx.android.synthetic.main.fragment_auth.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException

class AuthFragment : BaseFragment() {

    private lateinit var mFBCallbackManager: CallbackManager
    private var mGoogleSignInClient: GoogleApiClient? = null

    private val mViewModel by lazy(LazyThreadSafetyMode.NONE) {
        createViewModel<AuthViewModel>()
    }

    override fun onPause() {
        super.onPause()

        mGoogleSignInClient?.stopAutoManage(activity!!)
        mGoogleSignInClient?.disconnect()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? MainActivity)?.setToolbarVisible(false)
        (activity as? MainActivity)?.setNavigationVisible(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        val fbButton = view.findViewById<Button>(R.id.fb_button)
        val vkButton = view.findViewById<Button>(R.id.vk_button)
        val googleButton = view.findViewById<Button>(R.id.google_button)
        fbButton.setOnClickListener { onLoginButtonClick(it) }
        vkButton.setOnClickListener { onLoginButtonClick(it) }
        googleButton.setOnClickListener { onLoginButtonClick(it) }
        registerSdkCallbacks()
        mViewModel.viewState.observe(this, Observer {
            renderState(it)
        })

        return view
    }

    private fun renderState(authState: AuthViewState?) {
        when (authState?.state) {
            LceAuthState.LOADING -> {
                progress.show()
                vk_button.hide()
                fb_button.hide()
                google_button.hide()
            }
            LceAuthState.CONTENT -> {
                progress.hide()
                onAuthSuccess()
            }
            LceAuthState.ERROR -> {
                //toast(getString(R.string.auth_error))
                progress.hide()
                vk_button.show()
                fb_button.show()
                google_button.show()
            }
        }
    }

    private fun onAuthSuccess() {
        val preferences = PreferenceHelper.defaultPrefs(activity!!)
        preferences[SessionRepository.AUTH_PREF] = true
        mViewModel.navigateToNewsfeed()
    }

    private fun registerSdkCallbacks() {
        mFBCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mFBCallbackManager, FBAuthCallback())
    }

    private fun onLoginButtonClick(view: View) {
        when (view.id) {
            R.id.vk_button -> {
                val intent = Intent(activity!!, VKServiceActivity::class.java)
                intent.putExtra("arg1", "Authorization")
                val scopes = arrayListOf("wall", "friends")
                intent.putStringArrayListExtra("arg2", scopes)
                intent.putExtra("arg4", VKSdk.isCustomInitialize())
                startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.outerCode)
            }
            R.id.fb_button -> LoginManager.getInstance()
                .logInWithReadPermissions(this, arrayListOf("public_profile"))
            R.id.google_button -> signIn()
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("294611782255-a8rfjhnm0l2naa4b9qia69ig31okf0lv.apps.googleusercontent.com")
            .requestEmail()
            .requestScopes(com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/youtube.readonly"))
            .build()
        mGoogleSignInClient = GoogleApiClient.Builder(context!!)
            .enableAutoManage(activity!!) { Timber.e(it.errorMessage) }
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult")
        if (VKSdk.onActivityResult(requestCode, resultCode, data, VKAuthCallback())) {
            return
        }
        if (mFBCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return
        }
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                val authCode = account?.serverAuthCode
                mViewModel.getUserData(AuthType.GOOGLE, null, null, account, authCode)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class VKAuthCallback : VKCallback<VKAccessToken> {

        override fun onResult(res: VKAccessToken) {
            Timber.d("onResult VK: ${res.accessToken}")
            mViewModel.getUserData(AuthType.VK, res.accessToken, res.userId)
        }

        override fun onError(error: VKError?) {
            Timber.e("onError VK:")
        }
    }

    inner class FBAuthCallback : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            Timber.d("onResult FB: ${loginResult.accessToken.token}")
            mViewModel.getUserData(AuthType.FB, loginResult.accessToken.token)
        }

        override fun onCancel() {
            Timber.d("onCancel FB")
        }

        override fun onError(exception: FacebookException) {
            Timber.e("onError FB")
        }
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}
