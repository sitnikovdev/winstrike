package ru.prsolution.winstrike.presentation.cities

import android.content.res.Resources
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_city_list.*
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.model.ArenaItem
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils.selectedArena
import ru.prsolution.winstrike.viewmodel.ArenaViewModel

/**
 * Created by Oleg Sitnikov on 2019-02-11
 */

class CityFragment : Fragment() {

    private val mVm: ArenaViewModel by viewModel()

    private var mCityPid = ""
    private var mCityName = ""
    private var mArenaList: List<ArenaItem>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fmt_city_detail, container, false)
    }

    private var mArenaActivePid: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            val safeArgs = CityFragmentArgs.fromBundle(it)
            this.mCityPid = safeArgs.cityPid
            this.mCityName = safeArgs.cityName
        }

        val spannable = SpannableString("Ваш регион: ${mCityName}")

        spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorAccent)),
                12, spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        title_tv.text = spannable

        if (savedInstanceState == null) {
            mVm.fetchArenaList()
        }

        mVm.arenaList.observe(this@CityFragment, Observer { arenas ->

            arenas?.let {
                mArenaList = arenas.filter { it.cityPid == mCityPid }
                mArenaActivePid = arenas[selectedArena].activeLayoutPid
                PrefUtils.arenaPid = mArenaActivePid
            }

        })



/*        view.findViewById<TextView>(R.id.city_tv).setOnClickListener {

            val action = CityFragmentDirections.nextAction()
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
        }*/
    }
}