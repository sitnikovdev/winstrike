package ru.prsolution.winstrike.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.mvp.views.ProfileView
import ru.prsolution.winstrike.networking.Service


class ProfileScreenFragment : Fragment(), ProfileView {

	internal var service: Service? = null

	@InjectPresenter
	internal var presenter: ProfilePresenter? = null

	/*  ProfilePresenter provideMainScreenPresenter() {
    return new ProfilePresenter(service,
        ((RouterProvider) getParentFragment()).getRouter()
        , getArguments().getInt(EXTRA_NUMBER)
    );
  }*/


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.frm_profile, container, false)
		return view
	}

	companion object {

		private val EXTRA_NAME = "extra_name"
		private val EXTRA_NUMBER = "extra_number"

		fun getNewInstance(name: String, number: Int): ProfileScreenFragment {
			val fragment = ProfileScreenFragment()
			val arguments = Bundle()
			arguments.putString(EXTRA_NAME, name)
			arguments.putInt(EXTRA_NUMBER, number)
			//    fragment.setArguments(arguments);
			return fragment
		}
	}

}
