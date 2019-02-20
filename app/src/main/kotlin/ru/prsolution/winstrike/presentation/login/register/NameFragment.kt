package ru.prsolution.winstrike.presentation.login.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fmt_code.*
import kotlinx.android.synthetic.main.fmt_name.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.login.LoginActivity
import ru.prsolution.winstrike.presentation.utils.inflate

/**
 * Created by Oleg Sitnikov on 2019-02-19
 */

class NameFragment: Fragment() {
    var mPhone = ""
    var mCode = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_name)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val safeArgs = NameFragmentArgs.fromBundle(it)
            mPhone = safeArgs.phone
            mCode = safeArgs.code
        }

        et_sms_code.text = mCode

        start_button.setOnClickListener {
            val action = NameFragmentDirections.actionToMainActivity()
            Navigation.findNavController(requireActivity(), R.id.login_host_fragment).navigate(action)
        }

        (activity as LoginActivity).setPhoneHint(phone_hint_tv, mPhone)
        (activity as LoginActivity).setNamePolicyFooter(tv_name_policy)
    }


}