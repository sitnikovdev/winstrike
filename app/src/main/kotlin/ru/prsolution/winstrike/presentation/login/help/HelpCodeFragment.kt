package ru.prsolution.winstrike.presentation.login.help

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_help_code.*
import kotlinx.android.synthetic.main.fmt_help_code.view.*
import kotlinx.android.synthetic.main.inc_help_phone.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.setPhoneMask

/**
 * Created by Oleg Sitnikov on 2019-02-24
 */

class HelpCodeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_help_code)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set phone mask
//        et_phone.setPhoneMask()

        et_phone.setText(PrefUtils.phone)

        et_phone.isEnabled = false

        send_code_btn.isEnabled = false

        // Confirm code button
        confirm_code_btn.setOnClickListener {
            val action = HelpCodeFragmentDirections.actionToHelpPassword()
            (activity as NavigationListener).navigate(action)
        }


    }


}
