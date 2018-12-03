package com.example.ajiekc.karoon.ui.profile

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ajiekc.karoon.R
import com.example.ajiekc.karoon.databinding.FragmentProfileBinding
import com.example.ajiekc.karoon.ui.auth.AuthType
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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.VKServiceActivity
import com.vk.sdk.api.VKError
import timber.log.Timber

class ProfileFragment : BaseFragment(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        Timber.e("CONNECTION FAILED")
    }

    private lateinit var mFBCallbackManager: CallbackManager
    private var mGoogleSignInClient: GoogleApiClient? = null

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        createViewModel<ProfileViewModel>()
    }

    override fun onDestroy() {
        super.onDestroy()

        mGoogleSignInClient?.disconnect()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? MainActivity)?.setToolbarVisible(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)
        binding.vm = viewModel

        initObservers()
        registerSdkCallbacks()

        return binding.root
    }

    private fun initObservers() {
        viewModel.loginClickEvent.observe(this, Observer {
            onLoginButtonClick(it ?: "")
        })
        viewModel.logoutClickEvent.observe(this, Observer {
            logout(it ?: "")
        })
    }

    private fun registerSdkCallbacks() {

        mFBCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mFBCallbackManager, FBAuthCallback())
    }

    private fun onLoginButtonClick(type: String) {
        when (type) {
            AuthType.VK.name -> {
                val intent = Intent(activity!!, VKServiceActivity::class.java)
                intent.putExtra("arg1", "Authorization")
                val scopes = arrayListOf("wall", "friends")
                intent.putStringArrayListExtra("arg2", scopes)
                intent.putExtra("arg4", VKSdk.isCustomInitialize())
                startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.outerCode)
            }
            AuthType.FB.name -> LoginManager.getInstance()
                .logInWithReadPermissions(this, arrayListOf("public_profile"))
            AuthType.GOOGLE.name -> signIn()
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("294611782255-a8rfjhnm0l2naa4b9qia69ig31okf0lv.apps.googleusercontent.com")
            .requestEmail()
            .requestScopes(com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/youtube.readonly"))
            .build()
        mGoogleSignInClient = GoogleApiClient.Builder(context!!)
            .enableAutoManage(activity!!, this)
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
                viewModel.getUserData(AuthType.GOOGLE, null, null, account, authCode)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

/*    fun getAccessToken(authCode: String) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", "294611782255-a8rfjhnm0l2naa4b9qia69ig31okf0lv.apps.googleusercontent.com")
            .add("client_secret", "8JEQZaXXYiH2It069sr4JQsz")
            .add("code", authCode)
            .build()
        val request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v4/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Timber.e("onFailure")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val jsonObject = JSONObject(response.body()?.string())
                    val token = jsonObject.get("access_token").toString()
                    Timber.d("ACCESS TOKEN = $token")

                } catch (e: JSONException) {
                    Timber.e(e.toString())
                }
            }

        })
    }*/

    inner class VKAuthCallback : VKCallback<VKAccessToken> {

        override fun onResult(res: VKAccessToken) {
            Timber.d("onResult VK: ${res.accessToken}")
            viewModel.getUserData(AuthType.VK, res.accessToken, res.userId)
        }

        override fun onError(error: VKError?) {
            Timber.e("onError VK:")
        }
    }

    inner class FBAuthCallback : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            Timber.d("onResult FB: ${loginResult.accessToken.token}")
            viewModel.getUserData(AuthType.FB, loginResult.accessToken.token)
        }

        override fun onCancel() {
            Timber.d("onCancel FB")
        }

        override fun onError(exception: FacebookException) {
            Timber.e("onError FB")
        }
    }

    private fun logout(authType: String) {
        when (authType) {
            AuthType.VK.name -> VKSdk.logout()
            AuthType.FB.name -> LoginManager.getInstance().logOut()
            AuthType.GOOGLE.name -> {
                GoogleSignIn.getClient(context!!, GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .signOut()
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}
