package ru.prsolution.winstrike.presentation.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fmt_splash.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.StartActivity
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

/**
 * Created by Oleg Sitnikov on 2019-02-18
 */

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_splash)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animation_view.imageAssetsFolder = "images"
        animation_view.setAnimation("data.json")
        animation_view.repeatCount = 0
        animation_view.scale = 1f
        animation_view.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                start()
            }
        })
    }



    private fun start() {
        // If user is sign out -> go to login screen. else -> check if user is exist on server and if Ok -- go to Main screen.
        when {
            PrefUtils.isLogout -> {
    //            startActivity(loginIntent)
            }

            !PrefUtils.token?.isEmpty()!! && !PrefUtils.cityPid?.isEmpty()!! -> {
                val action = SplashFragmentDirections.actionToMainActivity()
                (activity as StartActivity).navigate(action)
            }

            !PrefUtils.token?.isEmpty()!! -> {
                val action = SplashFragmentDirections.actionToCityActivity()
                (activity as StartActivity).navigate(action)
            }

            else -> {
                val action = SplashFragmentDirections.actionToLoginActivity()
                (activity as StartActivity).navigate(action)
            }
        }
    }

}