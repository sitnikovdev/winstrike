package ru.prsolution.winstrike.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_profile_prof.et_password
import kotlinx.android.synthetic.main.fmt_profile_prof.fio
import kotlinx.android.synthetic.main.fmt_profile_prof.next_button
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.UserEntity
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

/*
 * Created by oleg on 03.02.2018.
 */

class ProfileFragment : Fragment() {
	private var user: UserEntity? = null

	lateinit var listener: OnProfileButtonsClickListener

	interface OnProfileButtonsClickListener {
		// Update user profile data (name and password)
		fun onProfileUpdate(name: String, passw: String)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is OnProfileButtonsClickListener) {
			listener = context
		} else {
			throw ClassCastException(context!!.toString() + " must implements OnProfileButtonsClickListener")
		}
	}


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val v = inflater.inflate(R.layout.fmt_profile_prof, container, false)
		this.user = UserEntity()
		next_button!!.setOnClickListener { it ->
			val name = fio!!.text.toString()
			val passw = et_password!!.text.toString()
			//                    setBtnEnable(next_button, false);
			listener.onProfileUpdate(name, passw)
		}

		if (PrefUtils.name != null) {
/*			this.user?.name = (AuthUtils.name)
			if (!TextUtils.isEmpty(user!!.getName())) {
				fio!!.setText(user!!.getName())
			}*/
		}

		return v
	}


	private fun setBtnEnable(v: View, isEnable: Boolean) {
		if (isEnable) {
			v.alpha = 1f
			v.isClickable = true
		} else {
			v.alpha = .5f
			v.isClickable = false
		}
	}

	companion object {

		private val TITLE = "Профиль"

		fun newInstance(): ProfileFragment {
			return ProfileFragment()
		}
	}
}
