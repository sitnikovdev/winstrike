package ru.prsolution.winstrike.presentation.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.login.ProfileModel
import ru.prsolution.winstrike.presentation.splash.SplashActivity
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils


class ProfileScreenFragment : Fragment() {

	var presenter: ProfilePresenter? = null

	private var mDlgSingOut: Dialog? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		dlgProfileSingOut()
	}


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.frm_profile, container, false)
	}

	// TODO Move in View Model
	fun onProfileUpdate(name: String, password: String) {
		if (password.isEmpty()) {
//			Toast.makeText(this, R.string.message_password, Toast.LENGTH_LONG).show()
		}
		if (name.isEmpty()) {
//			Toast.makeText(this, R.string.message_username, Toast.LENGTH_LONG).show()
		}
		if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {
			// call api for update profile here ...
			val profile = ProfileModel(
					name = name,
					password = password
			)
			val publicId = PrefUtils.publicid
			val token = Constants.TOKEN_TYPE_BEARER + PrefUtils.token
//			publicId?.let { mViewModel.updateProfile(token, profile, it) }
			PrefUtils.name = name
			var title: String? = getString(R.string.title_settings)
			if (PrefUtils.name != null) {
				if (!TextUtils.isEmpty(PrefUtils.name)) {
					title = PrefUtils.name
				}
			}
		}
	}


	// SignOut Dialog
	private fun dlgProfileSingOut() {
		mDlgSingOut = Dialog(activity, android.R.style.Theme_Dialog)
		mDlgSingOut!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
		mDlgSingOut!!.setContentView(R.layout.dlg_logout)

		val cvBtnOk = mDlgSingOut!!.findViewById<TextView>(R.id.btn_ok)
		val cvCancel = mDlgSingOut!!.findViewById<TextView>(R.id.btn_cancel)

		cvCancel.setOnClickListener { it -> mDlgSingOut!!.dismiss() }

		cvBtnOk.setOnClickListener { it ->
			PrefUtils.isLogout = true
			PrefUtils.token = ""
			startActivity(Intent(activity, SplashActivity::class.java))
		}

		mDlgSingOut!!.setCanceledOnTouchOutside(true)
		mDlgSingOut!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		mDlgSingOut!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

		val window = mDlgSingOut!!.window
		val wlp = window!!.attributes

		wlp.gravity = Gravity.CENTER
		wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
		window.attributes = wlp

		mDlgSingOut!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
		mDlgSingOut!!.window!!.setDimAmount(0.5f)

		mDlgSingOut!!.setCanceledOnTouchOutside(false)
		mDlgSingOut!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
		mDlgSingOut!!.dismiss()

	}

	// TODO: remove it !!!
	// Social networks links block:
	fun onGooglePlayButtonClick() {
		startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_GOOGLE_PLAY)))
	}

	fun onVkClick() {
		startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_VK)))
	}

	fun onInstagramClick() {
		startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_INSTAGRAM)))
	}

	fun onTweeterClick() {
		startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_TWEETER)))
	}

	fun onFacebookClick() {
		startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_FACEBOOK)))
	}

	fun onTwitchClick() {
		startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_TWITCH)))
	}

	fun onRecommendButtonClick() {
		shareImgOnRecommendClick()
	}

	// TODO replace packageName str with value
	private fun shareImgOnRecommendClick() {
		val attachedUri = Uri.parse(Constants.ANDROID_RESOURCES_PATH + "packageName"
				                            + Constants.SHARE_DRAWABLE + Constants.SHARE_IMG)
		val shareIntent = ShareCompat.IntentBuilder.from(activity)
				.setType(Constants.IMAGE_TYPE)
				.setStream(attachedUri)
				.intent
		shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.message_share_images)
		startActivity(Intent.createChooser(shareIntent, Constants.SHARE_IMG_TITLE))
	}


}
