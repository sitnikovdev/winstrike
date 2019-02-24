package ru.prsolution.winstrike.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_name.*
import kotlinx.android.synthetic.main.inc_prof_city.*
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.onRightDrawableClicked
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import com.google.android.material.textfield.TextInputLayout
import android.view.inputmethod.InputConnection
import android.view.inputmethod.EditorInfo
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_profile_prof.*
import kotlinx.android.synthetic.main.inc_password.*
import kotlinx.android.synthetic.main.inc_prof_name.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.presentation.main.ToolbarTitleListener
import ru.prsolution.winstrike.presentation.model.arena.CityItem
import ru.prsolution.winstrike.presentation.model.login.ProfileInfo
import ru.prsolution.winstrike.presentation.utils.isNameValid
import ru.prsolution.winstrike.presentation.utils.validate
import ru.prsolution.winstrike.viewmodel.CityListViewModel
import ru.prsolution.winstrike.viewmodel.ProfileViewModel
import timber.log.Timber


/*
 * Created by oleg on 03.02.2018.
 */

class ProfileTabFragment : Fragment() {
    private val mVmCity: CityListViewModel by viewModel()
    private val mVmProfile: ProfileViewModel by viewModel()

    private var cities = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(ru.prsolution.winstrike.R.layout.fmt_profile_prof)
    }

    private lateinit var adapter: ArrayAdapter<String>
    lateinit var mUserInfo: ProfileInfo


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            mVmCity.fetchCities()
        }


        save_btn.setOnClickListener {

            et_name_prof.validate({ et_name_prof.text?.isNameValid()!! }, getString(ru.prsolution.winstrike.R.string.fmt_name_error_lengh))

            when {
                et_name_prof.text!!.isNameValid() -> {

                    mUserInfo = ProfileInfo(PrefUtils.phone!!, et_name_prof.text.toString())
                    mVmProfile.updateUserProfile(PrefUtils.publicid!!, mUserInfo)

                }

            }
        }
        mVmProfile.messageResponse.observe(this@ProfileTabFragment, Observer {
            it?.let {
                // TODO: process error!
                when (it.state) {
//                    ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
//                    ResourceState.SUCCESS -> swipeRefreshLayout.stopRefreshing()
//                    ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
                }
                it.data?.let {
                    onUpdateSuccess(it)
                }
                it.message?.let {
                    onUpdateFailure(it)
                }
            }
        })


        mVmCity.cityList.observe(this@ProfileTabFragment, Observer { cities ->

            cities?.let {
                updateCities(it.data!!)
            }
        })

        et_name_prof.setText(PrefUtils.name)
        et_password.setText(PrefUtils.password)

        et_city.setText(PrefUtils.cityName)

        et_city.onRightDrawableClicked {
            it.text.clear()
        }

        cities = mutableListOf()

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line, cities
        )

        et_city.threshold = 1

        et_city.setAdapter(adapter)
    }


    private fun updateCities(data: List<CityItem>) {
        data.forEach {
            cities.add(it.name)
        }
        adapter.notifyDataSetChanged()
    }


    private fun onUpdateSuccess(message: MessageResponse) {
        longToast("Информация обновлена")
        (activity as ToolbarTitleListener).updateTitle(mUserInfo.name)
        PrefUtils.name = mUserInfo.name
        PrefUtils.cityName = et_city.text.toString()
//        save_btn.isEnabled = false
    }


    private fun onUpdateFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("403") ||
                    appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            (appErrorMessage.contains("409")) -> longToast("Не верный код.")
            appErrorMessage.contains("502") -> longToast("Ошибка сервера")
            appErrorMessage.contains("401") -> longToast("Ошибка авторизации")
            (appErrorMessage.contains("413")) -> longToast("Не верный формат данных")
            appErrorMessage.contains("No Internet Connection!") ->
                longToast("Интернет подключение не доступно!")
        }

    }

}


class TextInputAutoCompleteTextView : AppCompatAutoCompleteTextView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        val ic = super.onCreateInputConnection(outAttrs)
        if (ic != null && outAttrs.hintText == null) {
            // If we don't have a hint and our parent is a TextInputLayout, use it hint for the
            // EditorInfo. This allows us to display a hint in 'extract mode'.
            val parent = parent
            if (parent is TextInputLayout) {
                outAttrs.hintText = parent.hint
            }
        }
        return ic
    }
}