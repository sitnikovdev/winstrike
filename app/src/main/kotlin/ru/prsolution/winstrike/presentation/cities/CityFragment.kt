package ru.prsolution.winstrike.presentation.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.prsolution.winstrike.R

/**
 * Created by Oleg Sitnikov on 2019-02-11
 */

class CityFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fmt_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.findViewById<TextView>(R.id.city_tv).setOnClickListener {

            val action = CityFragmentDirections.nextAction()
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
        }
    }
}
