package ru.prsolution.winstrike.presentation.payment


import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import ru.prsolution.winstrike.ui.Screens
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.mvp.views.PayView
import ru.terrakok.cicerone.Router
import rx.subscriptions.CompositeSubscription

/**
 * Created by ennur on 6/25/16.
 */
class PayPresenter
/*
        url = getIntent().getStringExtra("url");

        if (TextUtils.isEmpty(url)) {
//            finish();
        }

        if (url.contains("politika.html")) {
            toolbar.setTitle(R.string.politica);
        } else {
            toolbar.setTitle(R.string.arena_name);
        }
*/
(private val service: Service, private val router: Router, internal var url: String) : MvpPresenter<PayView>() {
	private val subscriptions: CompositeSubscription

	init {
		this.subscriptions = CompositeSubscription()
	}

	fun onStop() {
		subscriptions.unsubscribe()
	}

	fun loadUrl() {
		viewState.loadUrl(url)
	}

	fun showProgress() {
		viewState.showWait()
	}

	fun hideProgress() {
		viewState.removeWait()
	}

	fun onBackPressed() {
		router.replaceScreen(Screens.MAP_SCREEN, 0)
	}
}
