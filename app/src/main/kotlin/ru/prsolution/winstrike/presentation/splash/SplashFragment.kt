package ru.prsolution.winstrike.presentation.splash

import android.animation.Animator
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fmt_splash.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

/**
 * Created by Oleg Sitnikov on 2019-02-18
 */

class SplashFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_splash)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animation_view.imageAssetsFolder = "images"
        animation_view.setAnimation("data.json")
        animation_view.repeatCount = 0
        animation_view.scale = 1f
        animation_view.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                start()
            }
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

//        mainIntent = Intent(this@StartActivity, MainActivity::class.java)
//        loginIntent = Intent(Intent(this@StartActivity, LoginActivity::class.java))


    private fun start() {
        // If user is sign out from App: go to login screen. Else: check if user is exist on server and if Ok -- go to Main screen.
//        startActivity(loginIntent)
        if (PrefUtils.isLogout) {
//            startActivity(loginIntent)
        } else if (PrefUtils.token?.isEmpty()!!) {
            val action = SplashFragmentDirections.actionToMainActivity()
            Navigation.findNavController(requireActivity(), R.id.splash_host_fragment).navigate(action)
        } else {
//            startActivity(loginIntent)
            val action = SplashFragmentDirections.actionToNavigationCityList()
            Navigation.findNavController(requireActivity(), R.id.splash_host_fragment).navigate(action)
        }
    }

}