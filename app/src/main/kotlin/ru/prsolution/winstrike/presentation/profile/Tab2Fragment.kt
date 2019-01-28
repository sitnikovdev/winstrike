package ru.prsolution.winstrike.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.ac_mainscreen.container
import ru.prsolution.winstrike.R

/**
 * Created by Oleg Sitnikov on 2019-01-28
 */

class Tab2Fragment: Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return  inflater.inflate(R.layout.fragment_two, container, false)
	}
}