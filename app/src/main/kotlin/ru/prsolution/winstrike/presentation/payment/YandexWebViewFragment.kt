package ru.prsolution.winstrike.presentation.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.prsolution.winstrike.R

/**
 * Created by Oleg Sitnikov on 2019-01-27
 */


class YandexWebViewFragment : Fragment() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_yandex_web_view, container, false)
	}
}
