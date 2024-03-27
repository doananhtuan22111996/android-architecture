package vn.geekup.app.pages.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vn.geekup.app.R
import vn.geekup.app.base.BaseFragment
import vn.geekup.app.databinding.FragmentLoginBinding
import vn.geekup.domain.model.general.ResultModel
import vn.geekup.app.pages.root.RootActivity
import vn.geekup.app.utils.*
import vn.geekup.data.BuildConfig

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModel()

    override fun provideViewBinding(parent: ViewGroup): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater, parent, true)

    override fun onInitLayout(view: View, savedInstanceState: Bundle?) {
        baseActivity.setAppColorStatusBar()
        fragmentBinding.fragment = this
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.login.observe(this) {
            Timber.d("Start Main Fragment")
            if (it) {
                (baseActivity as? RootActivity)?.redirectMain()
            }
        }

        viewModel.isLoading.observe(this) {
            fragmentBinding.vLoading.root.visible(it)
        }

    }

    override fun handleServerErrorState(serverErrorException: ResultModel.ServerErrorException?) {
        super.handleServerErrorState(serverErrorException)
        fragmentBinding.btnLoginOTable.visible(true)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webView = fragmentBinding.wvLogin
        webView.settings.javaScriptEnabled = true // enable javascript
        webView.webViewClient = object : WebViewOTableClient() {
            override fun setIsLoading(isLoading: Boolean) {
                viewModel.setIsLoading(isLoading)
            }

            override fun loginSuccess(otableToken: String) {
                requestLoginOTable(otableToken)
                redirectLoginOTable(false)
            }
        }
        webView.loadUrl("${BuildConfig.MAIN_DOMAIN}${BuildConfig.OTABLE_ENDPOINT}")
    }

    fun onClickLoginOTable() {
        redirectLoginOTable(true)
        initWebView()
//        viewModel.loginWithTravel()
    }

    private fun requestLoginOTable(otableToken: String) {
        viewModel.loginOTable(otableToken)
    }

    private fun redirectLoginOTable(isOTable: Boolean) {
        if (isOTable) {
            baseActivity.setAppColorStatusBar(R.color.color_white)
            fragmentBinding.btnLoginOTable.visible(false)
            fragmentBinding.wvLogin.visible(true)
        } else {
            baseActivity.setAppColorStatusBar()
            fragmentBinding.btnLoginOTable.visible(true)
            fragmentBinding.wvLogin.visible(false)
        }

    }

}