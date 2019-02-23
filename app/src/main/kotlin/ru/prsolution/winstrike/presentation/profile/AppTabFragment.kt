package ru.prsolution.winstrike.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_profile_app.*
import ru.prsolution.winstrike.presentation.utils.inflate
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import android.content.ActivityNotFoundException


/*
 * Created by oleg on 03.02.2018.
 */

class AppTabFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(ru.prsolution.winstrike.R.layout.fmt_profile_app)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cv_recomend.setOnClickListener {
            shareImg()
        }

        cv_estimate.setOnClickListener {
            onGooglePlayButtonClick()
        }


        v_vk.setOnClickListener {
            onVkClick()
        }

        v_instagram.setOnClickListener {
            onInstagramClick()
        }

        v_facebook.setOnClickListener {
            onFacebookClick()
        }

        v_tweeter.setOnClickListener {
            onTweeterClick()
        }

        v_twitch.setOnClickListener {
            onTwitchClick()
        }

    }

    private fun shareImg() {
        val attachedUri = Uri.parse(
            "android.resource://" + requireContext().packageName
                    + "/drawable/" + "winstrike_share"
        )
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setType("image/jpg")
            .setStream(attachedUri)
            .intent
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            ("Winstrike Arena - киберспорт в центре Москвы.\n" + "Качай приложение и играй за 50 рублей/час")
        )
        startActivity(Intent.createChooser(shareIntent, "Send"))
    }

    private fun onGooglePlayButtonClick() {
        val uri = Uri.parse("market://details?id=" + context?.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context?.packageName)
                )
            )
        }

    }

    fun onVkClick() {
        val url = "https://vk.com/winstrikearena"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    fun onInstagramClick() {
        val url = "https://www.instagram.com/winstrikearena/"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    fun onTweeterClick() {
        val url = "https://twitter.com/winstrikearena"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    fun onFacebookClick() {
        val url = "https://www.facebook.com/winstrikegg/"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    fun onTwitchClick() {
        val url = "https://www.twitch.tv/winstrikearena"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}
