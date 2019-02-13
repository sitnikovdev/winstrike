package ru.prsolution.winstrike.presentation.main

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.fmt_home.arena_description
import kotlinx.android.synthetic.main.fmt_home.arrowArena_Down
import kotlinx.android.synthetic.main.fmt_home.arrowArena_Up
import kotlinx.android.synthetic.main.fmt_home.head_image
import kotlinx.android.synthetic.main.fmt_home.root
import kotlinx.android.synthetic.main.fmt_home.rv_arena
import kotlinx.android.synthetic.main.fmt_home.tvArenaTitle
import kotlinx.android.synthetic.main.fmt_home.view_pager_seat
import org.jetbrains.anko.imageURI
import ru.prsolution.winstrike.App
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.ArenaHallType
import ru.prsolution.winstrike.domain.models.Type
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_350
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_450
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_600
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1080
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1440
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_720
import ru.prsolution.winstrike.presentation.main.carousel.CarouselAdapter
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.utils.custom.RecyclerViewMargin
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils.selectedArena
import ru.prsolution.winstrike.viewmodel.MainViewModel

/**
 * A main screen of Winstrike app.
 */

class HomeFragment : Fragment() {

    private var mArena: Arena? = null
    var mCarouselAdapter: CarouselAdapter? = null

    private val seatMap: MutableMap<Type, SeatCarousel> = mutableMapOf()
    var hallType: ArenaHallType = ArenaHallType.COMMON

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCarouselAdapter = CarouselAdapter(activity?.supportFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(ru.prsolution.winstrike.R.layout.fmt_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        updateCarouselView(mArena)

        val mVm = ViewModelProviders.of(this)[MainViewModel::class.java]

        if (savedInstanceState == null) {
            mVm.getArenaList()
        }

        // TODO: Process error response and show some info message!!!
        mVm.arenaList.observe(this@HomeFragment, Observer { resource ->
            resource.let {
                // TODO: use arena pid!!!
                mArena = resource?.data?.get(selectedArena)
                updateCarouselView(mArena)
                updateArenaInfo(mArena)
            }
        })

        setupViewPager()

    }

    override fun onResume() {
        super.onResume()
        val handler = Handler()
        handler.post {
            mCarouselAdapter?.notifyDataSetChanged()
        }
    }

    private fun updateArenaInfo(room: Arena?) { // TODO: Don't use datasource, use domain!!!
        tvArenaTitle.text = room?.name
        arena_description.text = room?.description
        head_image.imageURI = Uri.parse(room?.imageUrl)
    }

// Carousel view initiated

    private fun updateCarouselView(room: Arena?) {
        mCarouselAdapter?.isClear()?.let {
            require(it) {
                "++++ Adapter must be empty! ++++"
            }
        }
        // Define mArena type ( double(common & vip), common, vip)
        if (
                (!TextUtils.isEmpty(room?.commonDescription) && (!TextUtils.isEmpty(room?.vipDescription))) ||
                (!TextUtils.isEmpty(room?.commonImageUrl) && (!TextUtils.isEmpty(room?.vipImageUrl)))

        ) {
            hallType = ArenaHallType.DOUBLE
        } else if (!TextUtils.isEmpty(room?.commonDescription)) {
            hallType = ArenaHallType.COMMON
        } else if (!TextUtils.isEmpty(room?.vipDescription)) {
            hallType = ArenaHallType.VIP
        }

        when (hallType) {
            ArenaHallType.DOUBLE -> { // create two mArena: COMMON and VIP

                seatMap[Type.COMMON] = SeatCarousel(
                        type = Type.COMMON,
                        imageUrl = room?.commonImageUrl,
                        description = room?.commonDescription
                )

                seatMap[Type.VIP] = SeatCarousel(
                        type = Type.VIP,
                        imageUrl = room?.vipImageUrl,
                        description = room?.vipDescription
                )
            }
            ArenaHallType.COMMON -> {
                seatMap[Type.COMMON] = SeatCarousel(
                        type = Type.COMMON,
                        imageUrl = room?.commonImageUrl,
                        description = room?.commonDescription
                )
            }
            ArenaHallType.VIP -> { // Create VIP mArena

                seatMap[Type.VIP] = SeatCarousel(
                        type = Type.VIP,
                        imageUrl = room?.vipImageUrl,
                        description = room?.vipDescription
                )
            }
        }

        with(mCarouselAdapter) {
            seatMap.forEach {
                CarouselFragment.newInstance(
                        activity?.supportFragmentManager,
                        it.value
                )?.let { this?.addFragment(fragment = it) }
            }
        }

        view_pager_seat.adapter = mCarouselAdapter
        mCarouselAdapter?.notifyDataSetChanged()
    }

    private fun setupViewPager() {
        val widthPx = PrefUtils.displayWidhtPx
        with(view_pager_seat) {
            adapter = mCarouselAdapter
            currentItem = 0
            offscreenPageLimit = 2
            setPageTransformer(false, mCarouselAdapter)
            pageMargin = when {
                widthPx <= SCREEN_WIDTH_PX_720 -> SCREEN_MARGIN_350
                widthPx <= SCREEN_WIDTH_PX_1080 -> SCREEN_MARGIN_450
                widthPx <= SCREEN_WIDTH_PX_1440 -> SCREEN_MARGIN_600
                else -> SCREEN_MARGIN_450
            }
        }
    }

}
