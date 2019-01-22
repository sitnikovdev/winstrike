package ru.prsolution.winstrike.presentation.profile

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import ru.prsolution.winstrike.ui.Screens
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.mvp.views.ProfileView
import ru.terrakok.cicerone.Router
import rx.subscriptions.CompositeSubscription

/**
 * Created by terrakok 26.11.16
 */

@InjectViewState
class ProfilePresenter(private val service: Service, private val router: Router,
                       private val number: Int) : MvpPresenter<ProfileView>() {
	private val subscriptions: CompositeSubscription? = null


	fun onForwardPressed() {
		router.navigateTo(Screens.FORWARD_SCREEN, number + 1)
	}

	fun onGithubPressed() {
		router.navigateTo(Screens.GITHUB_SCREEN)
	}

	fun onBackPressed() {
		//router.exit();
		router.replaceScreen(Screens.START_SCREEN, 0)
	}

	fun onStop() {
		subscriptions!!.unsubscribe()
	}

}
