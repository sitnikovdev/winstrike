package ru.prsolution.winstrike.presentation.main

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
import kotlinx.android.synthetic.main.fmt_home.arrowArena_Down
import kotlinx.android.synthetic.main.fmt_home.arrowArena_Up
import kotlinx.android.synthetic.main.fmt_home.root
import kotlinx.android.synthetic.main.fmt_home.rv_arena
import kotlinx.android.synthetic.main.fmt_home.tvArenaTitle
import kotlinx.android.synthetic.main.fmt_home.view_pager_seat
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.datasource.model.Room
import ru.prsolution.winstrike.domain.models.SeatModel
import ru.prsolution.winstrike.domain.models.TimeDataModel
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_350
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_450
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_600
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1080
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1440
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_720
import ru.prsolution.winstrike.presentation.utils.custom.CarouselAdapter
import ru.prsolution.winstrike.presentation.utils.custom.RecyclerViewMargin
import ru.prsolution.winstrike.presentation.utils.pref.AuthUtils


/**
 * A main screen of Winstrike app.
 */
class HomeScreenFragment : Fragment() {

	var carouselAdapter: CarouselAdapter? = null
	var selectedArena = 0
	lateinit var presenter: HomePresenter
	private val arenaUpConstraintSet = ConstraintSet()
	private val arenaDownConstraintSet = ConstraintSet()
	private var isArenaShow: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// TODO init view model here
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fmt_home, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// TODO: Use AndroidX Preferences
		selectedArena = AuthUtils.selectedArena
		ArenaListAdapter.SELECTED_ITEM = selectedArena

		carouselAdapter = CarouselAdapter(activity)
		val vm: ArenaListViewModel = ViewModelProviders.of(this)[ArenaListViewModel::class.java]

		if (savedInstanceState == null) {
			vm.get()
		}

		val arenaListAdapter = ArenaListAdapter(onArenaClickItem)
		rv_arena.adapter = arenaListAdapter

		vm.rooms.observe(this, Observer { resource ->
			resource.let {
				it?.data?.let { arenaListAdapter.submitList(it) }
				updateCarouselSeatAdapter(resource?.data?.get(selectedArena))
			}
		})

		initArenaRV() // Arena select
		arenaSelectTransitionAnim() // Arena select transitions animations
	}

	private fun initArenaRV() {
		rv_arena.layoutManager = LinearLayoutManager(activity)
		rv_arena.addItemDecoration(RecyclerViewMargin(24, 1))
		rv_arena.bringToFront()
		rv_arena.adapter!!.notifyDataSetChanged()
	}

	private val onArenaClickItem: (Room, Int) -> Unit = { room, position ->
		TransitionManager.beginDelayedTransition(root)
		this.selectedArena = position
		this.isArenaShow = false
		arenaUpConstraintSet.applyTo(root)
		AuthUtils.selectedArena = position

		ArenaListAdapter.SELECTED_ITEM = position
		tvArenaTitle.text = room.name

		rv_arena.adapter!!.notifyDataSetChanged()
		updateCarouselSeatAdapter(room)
	}

	//TODO Replace with something else (slow animations, bad users view).
	private fun arenaSelectTransitionAnim() {
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

	/** seat type carousel view */
	// TODO: remove this!!!
	private fun updateCarouselSeatAdapter(room: Room?) {

		val widthPx =  AuthUtils.displayWidhtPx

		if (!TextUtils.isEmpty(room?.usualDescription) &&
				!TextUtils.isEmpty(room?.vipDescription)) {
			carouselAdapter!!.setPagesCount(2)
			carouselAdapter!!.addFragment(CarouselSeatFragment.newInstance(activity, room), 0, "Общий зал")
			carouselAdapter!!.addFragment(CarouselSeatFragment.newInstance(activity, room), 1, "Vip зал")
		} else {
			carouselAdapter!!.setPagesCount(1)
			carouselAdapter!!.addFragment(CarouselSeatFragment.newInstance(activity, room), 0, "Общий зал")
		}
		carouselAdapter!!.notifyDataSetChanged()
		view_pager_seat!!.adapter = carouselAdapter
		view_pager_seat!!.setPageTransformer(false, carouselAdapter)
		view_pager_seat!!.currentItem = 0
		view_pager_seat!!.offscreenPageLimit = 2

		when {
			widthPx <= SCREEN_WIDTH_PX_720 -> view_pager_seat!!.pageMargin = SCREEN_MARGIN_350
			widthPx <= SCREEN_WIDTH_PX_1080 -> view_pager_seat!!.pageMargin = SCREEN_MARGIN_450
			widthPx <= SCREEN_WIDTH_PX_1440 -> view_pager_seat!!.pageMargin = SCREEN_MARGIN_600
			else -> view_pager_seat!!.pageMargin = SCREEN_MARGIN_450
		}
	}

	/** click on seat in carousel view */
	fun onSeatClick(seat: SeatModel) {
		TimeDataModel.clearPids()
//		showFragmentHolderContainer(true)
		// TODO: remove this!!!
		WinstrikeApp.instance.seat = seat
//		presenter.onChooseScreenClick()
	}


}
