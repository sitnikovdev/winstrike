package ru.prsolution.winstrike.presentation.utils.custom

import android.view.View

import java.util.ArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp

/**
 * Created by oleg on 02/04/2018.
 */

class CarouselAdapter(val context: FragmentActivity?) : FragmentPagerAdapter(
		context?.supportFragmentManager), ViewPager.PageTransformer {

	private val mFragmentList = ArrayList<Fragment>()
	private var PAGES = 1
	var dpHeight: Float = 0.toFloat()
	var dpWidth: Float = 0.toFloat()

	var BIG_SCALE: Float? = null
	var SMALL_SCALE: Float? = null
	var DIFF_SCALE: Float? = null
	private var hall_name: String? = null

	fun setPagesCount(pages: Int) {
		this.PAGES = pages
	}

	init {

		dpHeight = WinstrikeApp.instance.displayHeightDp
		dpWidth = WinstrikeApp.instance.displayWidhtDp

		if (dpWidth <= 360.0) {
			BIG_SCALE = 0.9f
			SMALL_SCALE = 0.5f
		} else {
			BIG_SCALE = 1.0f
			SMALL_SCALE = 0.6f
		}
		DIFF_SCALE = BIG_SCALE!! - SMALL_SCALE!!
	}


	override fun getItem(position: Int): Fragment {
		return mFragmentList[position]
	}


	fun addFragment(fragment: Fragment, position: Int) {
		this.hall_name = hall_name
		mFragmentList.add(position, fragment)
	}

	override fun getCount(): Int {
		return PAGES
	}

	override fun transformPage(page: View, position: Float) {
		val root = page.findViewById<ChooseSeatLinearLayout>(R.id.root)
		val myLinearLayout = root
		var scale = BIG_SCALE
		if (scale != null) {
			if (position > 0) {
				scale -= position * DIFF_SCALE!!
			} else {
				scale += position * DIFF_SCALE!!
			}
		}
		if (scale != null) {
			if (scale < 0) {
				scale = 0f
			}
		}
		myLinearLayout.setScaleBoth(scale)
	}

}


