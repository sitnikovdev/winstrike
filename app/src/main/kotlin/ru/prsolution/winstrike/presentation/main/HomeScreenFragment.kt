package ru.prsolution.winstrike.presentation.main

import android.net.Uri
import android.os.Bundle
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
import com.ositnikov.preference.LiveSharedPreferences
import kotlinx.android.synthetic.main.fmt_home.arena_description
import kotlinx.android.synthetic.main.fmt_home.arrowArena_Down
import kotlinx.android.synthetic.main.fmt_home.arrowArena_Up
import kotlinx.android.synthetic.main.fmt_home.head_image
import kotlinx.android.synthetic.main.fmt_home.root
import kotlinx.android.synthetic.main.fmt_home.rv_arena
import kotlinx.android.synthetic.main.fmt_home.tvArenaTitle
import kotlinx.android.synthetic.main.fmt_home.view_pager_seat
import org.jetbrains.anko.imageURI
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.datasource.model.Room
import ru.prsolution.winstrike.datasource.model.RoomType
import ru.prsolution.winstrike.domain.models.RoomSeatType
import ru.prsolution.winstrike.domain.models.SeatModel
import ru.prsolution.winstrike.domain.models.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_350
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_450
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_600
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1080
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1440
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_720
import ru.prsolution.winstrike.presentation.utils.custom.CarouselAdapter
import ru.prsolution.winstrike.presentation.utils.custom.RecyclerViewMargin
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.pref.SharedPrefFactory
import timber.log.Timber


/**
 * A main screen of Winstrike app.
 */
class HomeScreenFragment : Fragment() {

	var selectedArena = 0
	val arenaUpConstraintSet = ConstraintSet()
	val arenaDownConstraintSet = ConstraintSet()
	var isArenaShow: Boolean = false
	lateinit var vm: MainScreenViewModel
	lateinit var arenaListAdapter: ArenaListAdapter
	lateinit var carouselAdapter: CarouselAdapter
	lateinit var liveSharedPreferences: LiveSharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		vm = ViewModelProviders.of(this)[MainScreenViewModel::class.java]
		selectedArena = PrefUtils.selectedArena
		ArenaListAdapter.SELECTED_ITEM = selectedArena
		carouselAdapter = CarouselAdapter(activity)

		if (savedInstanceState == null) {
			vm.getRooms()
		}

		liveSharedPreferences = LiveSharedPreferences(SharedPrefFactory.prefs)

	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fmt_home, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arenaListAdapter = ArenaListAdapter(onArenaClickItem)

		vm.rooms.observe(this, Observer { resource ->
			resource.let {
				it?.data?.let { arenaListAdapter.submitList(it) }
				val room = resource?.data?.get(selectedArena)
				updateCarouselView(room)
				updateArenaInfo(room)
			}
		})

		liveSharedPreferences.getInt("selectedArena", 0).observe(this, Observer<Int> { value ->
			Timber.tag("###").d(value.toString())
		})


		initArenaRV(arenaListAdapter) // Arena select
		initAnimArenaRV() // Arena select transitions animations
	}

	private fun updateArenaInfo(room: Room?) {
		tvArenaTitle.text = room?.name
		arena_description.text = room?.description
		head_image.imageURI = Uri.parse(room?.imageUrl)
	}

	/** Arena selected recycler view */
	private fun initArenaRV(arenaListAdapter: ArenaListAdapter) {
		with(rv_arena) {
			bringToFront()
			addItemDecoration(RecyclerViewMargin(24, 1))
			layoutManager = LinearLayoutManager(activity)
			adapter = arenaListAdapter
			adapter!!.notifyDataSetChanged()
		}
	}

	private val onArenaClickItem: (Room, Int) -> Unit = { room, position ->
		TransitionManager.beginDelayedTransition(root)
		arenaUpConstraintSet.applyTo(root)
		PrefUtils.selectedArena = position
		ArenaListAdapter.SELECTED_ITEM = position
		this.selectedArena = position
		this.isArenaShow = false

		tvArenaTitle.text = room.name

		rv_arena.adapter!!.notifyDataSetChanged()
		updateCarouselView(room)
		updateArenaInfo(room)
	}

	private fun initAnimArenaRV() {
		arenaDownConstraintSet.clone(activity, R.layout.part_arena_down)
		arenaUpConstraintSet.clone(activity, R.layout.part_arena_up)
		arrowArena_Down.setOnClickListener {
			TransitionManager.beginDelayedTransition(root)
			if (!isArenaShow) {
				isArenaShow = true
				arenaDownConstraintSet.applyTo(root)
			} else {
				arenaUpConstraintSet.applyTo(root)
				isArenaShow = false
			}
		}

		arrowArena_Up.setOnClickListener {
			TransitionManager.beginDelayedTransition(root)
			arenaUpConstraintSet.applyTo(root)
		}
	}

	/** Seat type carousel */
	private fun updateCarouselView(room: Room?) {
		var roomType: RoomType = RoomType.COMMON

		val widthPx = PrefUtils.displayWidhtPx
		val seatMap: MutableMap<RoomSeatType, SeatModel> = mutableMapOf()

		if (
				(!TextUtils.isEmpty(room?.commonDescription) && (!TextUtils.isEmpty(room?.vipDescription)))
				||
				(!TextUtils.isEmpty(room?.commonImageUrl) && (!TextUtils.isEmpty(room?.vipImageUrl)))

		) {
			roomType = RoomType.TWOROOMS
		} else if (!TextUtils.isEmpty(room?.commonDescription)) {
			roomType = RoomType.COMMON
		} else if (!TextUtils.isEmpty(room?.vipDescription)) {
			roomType = RoomType.VIP
		}

		when (roomType) {
			RoomType.TWOROOMS -> {

				seatMap[RoomSeatType.COMMON] = SeatModel(
						type = RoomSeatType.COMMON,
						imageUrl = room?.commonImageUrl,
						description = room?.commonDescription

				)

				seatMap[RoomSeatType.VIP] = SeatModel(
						type = RoomSeatType.VIP,
						imageUrl = room?.vipImageUrl,
						description = room?.vipDescription
				)


				with(carouselAdapter) {
					setPagesCount(2)
					addFragment(CarouselSeatFragment.newInstance(activity, seatMap[RoomSeatType.COMMON]!!), 0)
					addFragment(CarouselSeatFragment.newInstance(activity, seatMap[RoomSeatType.VIP]!!), 1)
				}
			}
			RoomType.COMMON -> {
				seatMap[RoomSeatType.COMMON] = SeatModel(
						type = RoomSeatType.COMMON,
						imageUrl = room?.commonImageUrl,
						description = room?.commonDescription

				)

				with(carouselAdapter) {
					setPagesCount(1)
					addFragment(CarouselSeatFragment.newInstance(activity, seatMap[RoomSeatType.COMMON]!!), 0)
				}
			}
			RoomType.VIP -> {

				seatMap[RoomSeatType.VIP] = SeatModel(
						type = RoomSeatType.VIP,
						imageUrl = room?.vipImageUrl,
						description = room?.vipDescription
				)

				with(carouselAdapter) {
					setPagesCount(1)
					addFragment(CarouselSeatFragment.newInstance(activity, seatMap[RoomSeatType.VIP]!!), 0)
				}
			}
		}


		with(view_pager_seat) {
			adapter = carouselAdapter
			currentItem = 0
			offscreenPageLimit = 2
			setPageTransformer(false, carouselAdapter)
		}

		with(view_pager_seat) {
			pageMargin = when {
				widthPx <= SCREEN_WIDTH_PX_720 -> SCREEN_MARGIN_350
				widthPx <= SCREEN_WIDTH_PX_1080 -> SCREEN_MARGIN_450
				widthPx <= SCREEN_WIDTH_PX_1440 -> SCREEN_MARGIN_600
				else -> SCREEN_MARGIN_450
			}
		}
		carouselAdapter.notifyDataSetChanged()
	}

	/** click on seat in carousel view */
	fun onSeatClick(seat: SeatModel) {
		TimeDataModel.clearPids()
//		showFragmentHolderContainer(true)
		// TODO: remove this!!! Use ViewModel
		WinstrikeApp.instance.seat = seat
//		presenter.onChooseScreenClick()
	}


}
