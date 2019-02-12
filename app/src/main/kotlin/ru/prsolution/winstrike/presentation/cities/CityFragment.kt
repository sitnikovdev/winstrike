package ru.prsolution.winstrike.presentation.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fmt_city_list.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.presentation.main.MainViewModel
import timber.log.Timber

/**
 * Created by Oleg Sitnikov on 2019-02-11
 */

class CityFragment : Fragment() {

    private var mCityPid = ""
    private var mArenaList: List<Arena>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fmt_city_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

       val mVm = activity?.let { ViewModelProviders.of(this@CityFragment)[MainViewModel::class.java] }

        if (savedInstanceState == null) {
            mVm?.getArenaList()
        }

        mVm?.arenaList?.observe(this@CityFragment, Observer { arenas ->

            arenas?.let {
//                updateCities(it)
                Timber.tag("$$$").d("arena list size: ${arenas.data?.size}")
                mArenaList = arenas.data
                Timber.tag("$$$").d("arena contains city: ${arenas.data?.find { it.cityPid == mCityPid }}")
//                Timber.d("city name: ${arenas.data?.find { it.cityPid == mCityPid }}")
            }

        })


        arguments?.let {
            val safeArgs = CityFragmentArgs.fromBundle(it)
            this.mCityPid = safeArgs.cityPid
        }

        title_tv.text = mCityPid

/*        view.findViewById<TextView>(R.id.city_tv).setOnClickListener {

            val action = CityFragmentDirections.nextAction()
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
        }*/
    }
}
