package ru.prsolution.winstrike.presentation.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fmt_city_list.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.presentation.main.MainViewModel
import ru.prsolution.winstrike.presentation.utils.resouces.Resource
import ru.prsolution.winstrike.presentation.utils.resouces.ResourceState

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class CityListFragment : Fragment() {

    var mVm: MainViewModel? = null

    private val itemClick: (City) -> Unit =
            {
                val action = CityListFragmentDirections.nextAction()
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
            }
    private val adapter = CityListAdapter(itemClick)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fmt_city_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mVm = activity?.let { ViewModelProviders.of(it)[MainViewModel::class.java] }

        city_rv.adapter = adapter

        if (savedInstanceState == null) {
            mVm?.getCity()
        }

        mVm?.cityList?.observe(this@CityListFragment, Observer { cities ->

            cities?.let {
                updateCities(it)
            }

        })


    }

    private fun updateCities(resource: Resource<List<City>>?) {
        resource?.let {
            when (it.state) {
                ResourceState.LOADING -> {
                }
                ResourceState.SUCCESS -> {
                }
                ResourceState.ERROR -> {
                }
            }

            it.data?.let {
                 adapter.submitList(it)
            }
        }
    }
}