package vn.geekup.app.pages.root

import androidx.lifecycle.MutableLiveData
import vn.geekup.app.base.BaseViewModel
import vn.geekup.app.network.NetworkChange

class RootViewModel(
    networkChange: NetworkChange,
) : BaseViewModel(networkChange) {

    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
}
