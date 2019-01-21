package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.ButterKnife
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.mvp.presenters.HomePresenter
import ru.prsolution.winstrike.mvp.views.HomeView
import ru.prsolution.winstrike.common.BackButtonListener
import ru.prsolution.winstrike.networking.RetrofitFactory


/**
 * Created by terrakok 26.11.16
 */
class HomeScreenFragment : Fragment(),
        HomeView,
        BackButtonListener {

    lateinit var presenter: HomePresenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var service = RetrofitFactory.makeRetrofitService()
        presenter = HomePresenter(service)
        val view = inflater.inflate(R.layout.fmt_home, container, false)
        ButterKnife.bind(this, view)
        return view
    }


    override fun onBackPressed(): Boolean {
        return false
    }


    override fun showWait() {}

    override fun removeWait() {

    }

    companion object {
        private val EXTRA_NAME = "extra_name"
        private val EXTRA_NUMBER = "extra_number"

        fun getNewInstance(name: String, number: Int): Fragment {
            val fragment: Fragment
            fragment = HomeScreenFragment()
            val arguments = Bundle()
            arguments.putString(EXTRA_NAME, name)
            arguments.putInt(EXTRA_NUMBER, number)
            fragment.setArguments(arguments)

            return fragment
        }
    }

}
