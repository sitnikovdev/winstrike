package ru.prsolution.winstrike.presentation.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.login.LoginActivity
import ru.prsolution.winstrike.presentation.login.register.NameFragmentDirections
import ru.prsolution.winstrike.presentation.model.orders.Order
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.OrderViewModel
import ru.prsolution.winstrike.viewmodel.ProfileViewModel
import timber.log.Timber

class OrderFragment : Fragment() {

    private val mVm: OrderViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.inflate(R.layout.fmt_nopaid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            mVm.getOrders()
        }

        mVm.orders.observe(this@OrderFragment, Observer {
            it?.let {
                // TODO: process error!
                when (it.state) {
//                    ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
//                    ResourceState.SUCCESS -> swipeRefreshLayout.stopRefreshing()
//                    ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
                }
                it.data?.let {
                    onUpdateSuccess(it)
                }
                it.message?.let {
                    onUpdateFailure(it)
                }
            }
        })


    }


    private fun onUpdateSuccess(orders: List<Order>) {
//        PrefUtils.name = mUserInfo.name
//        val action = NameFragmentDirections.actionToCityActivity()
//        (activity as LoginActivity).navigate(action)
    }


    private fun onUpdateFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("403") ||
                    appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            (appErrorMessage.contains("409")) -> longToast("Не верный код.")
            appErrorMessage.contains("502") -> longToast("Ошибка сервера")
            appErrorMessage.contains("401") -> longToast("Ошибка авторизации")
            (appErrorMessage.contains("413")) -> longToast("Не верный формат данных")
            appErrorMessage.contains("No Internet Connection!") ->
                longToast("Интернет подключение не доступно!")
        }

    }


}
