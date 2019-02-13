package ru.prsolution.winstrike.presentation.cities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fmt_city_list.*
import kotlinx.android.synthetic.main.item_home.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.datasource.cache.CityCacheDataSourceImpl
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.presentation.utils.cache.Cache
import ru.prsolution.winstrike.viewmodel.CityViewModel
import ru.prsolution.winstrike.viewmodel.MainViewModel
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

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val mVm = activity?.let { ViewModelProviders.of(this@CityFragment)[CityViewModel::class.java] }


        var city:List<City>? = null

//        mVm?.cityCache?.get()?.subscribe { it ->
//                city = it
//        }

//        Timber.d("cities: ${city?.size}")


        if (savedInstanceState == null) {
//            mVm?.getArenaList()
        }

/*        mVm?.arenaList?.observe(this@CityFragment, Observer { arenas ->

            arenas?.let {
//                updateCities(it)
                Timber.tag("$$$").d("arena list size: ${arenas.data?.size}")
                mArenaList = arenas.data
                Timber.tag("$$$").d("arena contains city: ${arenas.data?.find { it.cityPid == mCityPid }}")
//                Timber.d("city name: ${arenas.data?.find { it.cityPid == mCityPid }}")
            }

        })*/


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
