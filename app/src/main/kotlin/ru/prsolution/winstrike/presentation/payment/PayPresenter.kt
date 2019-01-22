package ru.prsolution.winstrike.presentation.payment

import ru.prsolution.winstrike.networking.Service
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
(private val service: Service, internal var url: String)  {
	private val subscriptions: CompositeSubscription

	init {
		this.subscriptions = CompositeSubscription()
	}

	fun onStop() {
		subscriptions.unsubscribe()
	}

	fun loadUrl() {
//		viewState.loadUrl(url)
	}

	fun showProgress() {
//		viewState.showWait()
	}

	fun hideProgress() {
//		viewState.removeWait()
	}

	fun onBackPressed() {
//		router.replaceScreen(Screens.MAP_SCREEN, 0)
	}
}
