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
import kotlinx.android.synthetic.main.inc_password.*
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.presentation.model.arena.CityItem
import ru.prsolution.winstrike.viewmodel.CityListViewModel


/*
 * Created by oleg on 03.02.2018.
 */

class ProfileTabFragment : Fragment() {
    private val mVm: CityListViewModel by viewModel()

    private var CITIES = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(ru.prsolution.winstrike.R.layout.fmt_profile_prof)
    }

    private lateinit var adapter: ArrayAdapter<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            mVm.fetchCities()
        }

        mVm.cityList.observe(this@ProfileTabFragment, Observer { cities ->

            cities?.let {
                updateCities(it.data!!)
            }

        })

        et_name.setText(PrefUtils.name)
        et_password.setText(PrefUtils.password)

        et_city.setText(PrefUtils.cityName)

        et_city.onRightDrawableClicked {
            it.text.clear()
        }

        CITIES = mutableListOf()

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line, CITIES
        )

        et_city.threshold = 1

        et_city.setAdapter(adapter)
    }


    private fun updateCities(data: List<CityItem>) {
        data.forEach {
            CITIES.add(it.name)
        }
        adapter.notifyDataSetChanged()
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