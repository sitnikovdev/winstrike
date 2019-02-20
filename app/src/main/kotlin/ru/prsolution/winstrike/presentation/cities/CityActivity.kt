package ru.prsolution.winstrike.presentation.cities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import ru.prsolution.winstrike.R

/**
 * Created by Oleg Sitnikov on 2019-02-20
 */

class CityActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_city)
    }

    // Fragment navigation
    fun navigate(action: NavDirections) {
        Navigation.findNavController(this, ru.prsolution.winstrike.R.id.city_host_fragment).navigate(action)
    }

}