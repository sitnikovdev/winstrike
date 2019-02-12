package ru.prsolution.winstrike.presentation.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.prsolution.winstrike.R

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class CityList : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fmt_city_list, container, false)
    }
}