package ru.prsolution.winstrike.presentation.main

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_home.*
import org.jetbrains.anko.imageURI
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.ArenaHallType
import ru.prsolution.winstrike.domain.models.Type
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_350
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_450
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_600
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1080
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1440
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_720
import ru.prsolution.winstrike.presentation.main.carousel.CarouselAdapter
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.model.ArenaItem
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.ArenaViewModel
import timber.log.Timber

/**
 * Created by Oleg Sitnikov on 2019-02-13
 */


class HomeFragment : Fragment() {

    private val mVm: ArenaViewModel by viewModel()

    private var mArenaPid: String = ""
    private var mArena: ArenaItem? = null

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
        arguments?.let {
            val safeArgs = HomeFragmentArgs.fromBundle(it)
            this.mArenaPid = safeArgs.arenaPID
        }

        if (savedInstanceState == null) {
            mVm.fetchArenaList()
        }

        // TODO: Process error response and show some info message!!!
        mVm.arenaList.observe(this@HomeFragment, Observer {
            it.let {
                // TODO: use arena pid!!!
                mArena = it.find { it.publicId!!.contains(mArenaPid) }
                updateArenaInfo(mArena)
                updateCarouselView(mArena)
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

    private fun updateArenaInfo(arena: ArenaItem?) { // TODO: Don't use domain (datasource), use (domain) Presentation Layer!!!
        metro_tv.text = arena?.metro
        arena_description.text = arena?.description
        head_image.imageURI = Uri.parse(arena?.imageUrl)
    }

// Carousel view initiated

    private fun updateCarouselView(arenaItem: ArenaItem?) {
        mCarouselAdapter?.isClear()?.let {
            require(it) {
                "++++ Adapter must be empty! ++++"
            }
        }
        // Define mArena type ( double(common & vip), common, vip)
        if (
                (!TextUtils.isEmpty(arenaItem?.commonDescription) && (!TextUtils.isEmpty(arenaItem?.vipDescription))) ||
                (!TextUtils.isEmpty(arenaItem?.commonImageUrl) && (!TextUtils.isEmpty(arenaItem?.vipImageUrl)))

        ) {
            hallType = ArenaHallType.DOUBLE
        } else if (!TextUtils.isEmpty(arenaItem?.commonDescription)) {
            hallType = ArenaHallType.COMMON
        } else if (!TextUtils.isEmpty(arenaItem?.vipDescription)) {
            hallType = ArenaHallType.VIP
        }

        when (hallType) {
            ArenaHallType.DOUBLE -> { // create two mArena: COMMON and VIP

                seatMap[Type.COMMON] = SeatCarousel(
                        type = Type.COMMON,
                        imageUrl = arenaItem?.commonImageUrl,
                        description = arenaItem?.commonDescription
                )

                seatMap[Type.VIP] = SeatCarousel(
                        type = Type.VIP,
                        imageUrl = arenaItem?.vipImageUrl,
                        description = arenaItem?.vipDescription
                )
            }
            ArenaHallType.COMMON -> {
                seatMap[Type.COMMON] = SeatCarousel(
                        type = Type.COMMON,
                        imageUrl = arenaItem?.commonImageUrl,
                        description = arenaItem?.commonDescription
                )
            }
            ArenaHallType.VIP -> { // Create VIP mArena

                seatMap[Type.VIP] = SeatCarousel(
                        type = Type.VIP,
                        imageUrl = arenaItem?.vipImageUrl,
                        description = arenaItem?.vipDescription
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
