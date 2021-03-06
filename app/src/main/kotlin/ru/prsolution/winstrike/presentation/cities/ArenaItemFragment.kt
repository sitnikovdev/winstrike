package ru.prsolution.winstrike.presentation.cities

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_arena.*
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.model.arena.ArenaItem
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils.selectedArena
import ru.prsolution.winstrike.viewmodel.CityItemViewModel

/**
 * Created by Oleg Sitnikov on 2019-02-11
 */

class ArenaItemFragment : Fragment() {

    private val mVm: CityItemViewModel by viewModel()

    private var mCityPid = ""
    private var mCityName = ""
    private var mArenaList: List<ArenaItem>? = null

    private val itemClick: (ArenaItem) -> Unit =
        { arena ->
            PrefUtils.arenaPid = arena.publicId

            //TODO: Navigate to Main Screen
            val action =
                ArenaItemFragmentDirections.actionToHome()
            arena.name?.let {
                action.title = it
                PrefUtils.arenaName = it
            }
            arena.publicId?.let {
                action.arenaPID = it
            }
            (activity as NavigationListener).navigate(action)
        }

    private val adapter = ArenaListAdapter(itemClick)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fmt_arena, container, false)
    }

    private var mArenaActivePid: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            val safeArgs = ArenaItemFragmentArgs.fromBundle(it)
            this.mCityPid = safeArgs.cityPid
            this.mCityName = safeArgs.cityName
            PrefUtils.cityPid = this.mCityPid
        }

        val spannable = SpannableString("Ваш регион: $mCityName")

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.colorAccent)),
            12, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        view.findViewById<TextView>(R.id.title_tv).text = spannable

        if (savedInstanceState == null) {
            mVm.fetchArenaList()
        }

        mVm.arenaList.observe(this@ArenaItemFragment, Observer { arenas ->

            arenas?.let {
                mArenaList = arenas.data?.filter { it.cityPid == mCityPid }
                mArenaActivePid = arenas.data!![selectedArena].activeLayoutPid
                PrefUtils.acitveArenaPid = mArenaActivePid
                updateArenaList(mArenaList)
            }

        })

        arena_rv.adapter = adapter
    }

    private fun updateArenaList(arenaList: List<ArenaItem>?) {
        adapter.submitList(arenaList)
    }
}
