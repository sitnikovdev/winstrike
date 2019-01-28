package ru.prsolution.winstrike.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_profile_app.cv_estimate
import kotlinx.android.synthetic.main.fmt_profile_app.cv_recomend
import kotlinx.android.synthetic.main.fmt_profile_app.sw_note
import kotlinx.android.synthetic.main.fmt_profile_app.v_facebook
import kotlinx.android.synthetic.main.fmt_profile_app.v_instagram
import kotlinx.android.synthetic.main.fmt_profile_app.v_tweeter
import kotlinx.android.synthetic.main.fmt_profile_app.v_twitch
import kotlinx.android.synthetic.main.fmt_profile_app.v_vk
import ru.prsolution.winstrike.R

/*
 * Created by oleg on 03.02.2018.
 */

class AppTabFragment : Fragment() {


	private var listener: OnAppButtonsClickListener? = null

	interface OnAppButtonsClickListener {
		fun onPushClick(isOn: String)

		fun onRecommendButtonClick()

		fun onGooglePlayButtonClick()

		fun onVkClick()

		fun onInstagramClick()

		fun onTweeterClick()

		fun onFacebookClick()

		fun onTwitchClick()
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is OnAppButtonsClickListener) {
			listener = context
		} else {
			throw ClassCastException(context!!.toString() + " must implements OnAppButtonsClickListener")
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val v = inflater.inflate(R.layout.fmt_profile_app, container, false)
		sw_note.setTextOn("On") // displayed text of the Switch whenever it is in checked or on state
		sw_note.setTextOff("Off") // displayed text of the Switch whenever it is in unchecked i.e. off state


		sw_note.setOnClickListener(View.OnClickListener {
			if (sw_note.isChecked()) {
				listener!!.onPushClick(sw_note.getTextOn().toString())
			} else {
				listener!!.onPushClick(sw_note.getTextOff().toString())
			}
		})



		cv_recomend.setOnClickListener(
				{ it -> listener!!.onRecommendButtonClick() }
		)

		cv_estimate.setOnClickListener(
				{ it -> listener!!.onGooglePlayButtonClick() }
		)

		v_vk.setOnClickListener(
				{ it -> listener!!.onVkClick() }
		)

		v_instagram.setOnClickListener(
				{ it -> listener!!.onInstagramClick() }
		)

		v_tweeter.setOnClickListener(
				{ it -> listener!!.onTweeterClick() }
		)

		v_facebook.setOnClickListener(
				{ it -> listener!!.onFacebookClick() }
		)

		v_twitch.setOnClickListener(
				{ it -> listener!!.onTwitchClick() }
		)

		return v
	}

	companion object {

		fun newInstance(): AppTabFragment {
			return AppTabFragment()
		}
	}


}
