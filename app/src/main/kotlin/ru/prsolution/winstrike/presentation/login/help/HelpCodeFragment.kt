package ru.prsolution.winstrike.presentation.login.help

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_code.*
import kotlinx.android.synthetic.main.fmt_help_code.*
import kotlinx.android.synthetic.main.fmt_help_code.view.*
import kotlinx.android.synthetic.main.fmt_help_phone.*
import kotlinx.android.synthetic.main.inc_help_code.*
import kotlinx.android.synthetic.main.inc_help_phone.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.login.FooterSetUp
import ru.prsolution.winstrike.presentation.model.login.SmsInfo
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.isCodeValid
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.setPhoneMask
import ru.prsolution.winstrike.presentation.utils.validate

/**
 * Created by Oleg Sitnikov on 2019-02-24
 */

class HelpCodeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_help_code)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set footer
        val action = HelpCodeFragmentDirections.actionToLogin()
        (activity as FooterSetUp).setRegisterLoginFooter(textView = login_footer_code , action = action)

        // Set phone mask
//        et_phone.setPhoneMask()

        et_phone_help.setText(PrefUtils.phone)

        et_phone_help.isEnabled = false

        send_code_btn.isEnabled = false

        // Confirm code button
        confirm_code_btn.setOnClickListener {

            et_code_help.validate({ et_code_help.text!!.isCodeValid() }, getString(R.string.fmt_code_error_lengh))

            when {
                et_code_help.text!!.isCodeValid() -> {
                    val mCode = et_code_help.text.toString()
                    val action = HelpCodeFragmentDirections.actionToHelpPassword()
                    action.code = mCode
                    (activity as NavigationListener).navigate(action)

                }
            }

        }


    }


}
