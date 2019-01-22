package ru.prsolution.winstrike.presentation.utils.vpadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber
import java.util.ArrayList


/**
 * BottomNav
 * Created by Suleiman19 on 6/12/17.
 * Copyright (c) 2017. Suleiman Ali Shakir. All rights reserved.
 */

class BaseViewPagerAdapter(fragmentManager: FragmentManager) : SmartFragmentStatePagerAdapter(fragmentManager) {

	private val fragments = ArrayList<Fragment>()

	override fun getCount(): Int {
		return fragments.size
	}

	fun addFragments(fragment: Fragment) {
		fragments.add(fragment)
	}

	fun addFragments(fragment: Fragment, title: String) {
		fragments.add(fragment)
		mFragmentTitleList.add(title)
	}

	override fun getItem(position: Int): Fragment {
		Timber.e("Fragment: " + fragments[position])
		Timber.e("This position is: $position")
		return fragments[position]
	}

	override fun getPageTitle(position: Int): CharSequence {
		return mFragmentTitleList[position]
	}

	companion object {
		private val mFragmentTitleList = ArrayList<String>()
	}
}
