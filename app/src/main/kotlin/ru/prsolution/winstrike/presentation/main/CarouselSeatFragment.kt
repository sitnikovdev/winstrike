package ru.prsolution.winstrike.presentation.main

/*
 * Created by oleg on 01.02.2018.
 */

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.facebook.drawee.view.SimpleDraweeView
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.datasource.model.Room
import ru.prsolution.winstrike.presentation.utils.custom.ChooseSeatLinearLayout
import ru.prsolution.winstrike.domain.models.SeatModel
import timber.log.Timber

class CarouselSeatFragment : Fragment() {


	lateinit var listener: OnChoosePlaceButtonsClickListener
	private var itemSeat: View? = null
	private var mSelectedArena: Int = 0
	private var mRoom: Room? = null
	private var mainScreenActivity: MainScreenActivity? = null


	interface OnChoosePlaceButtonsClickListener {

		fun onSeatClick(seat: SeatModel)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is OnChoosePlaceButtonsClickListener) {
			listener = context
		} else {
			throw ClassCastException(
					context!!.toString() + " must implements OnChoosePlaceButtonsClickListener ") as Throwable
		}
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		try {
			mRoom = arguments?.getSerializable("room") as Room
		} catch (e: Exception) {
			Timber.e(e)
		}

	}


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		this.mainScreenActivity = activity as MainScreenActivity?
		if (container == null) {
			return null
		}
		itemSeat = inflater.inflate(R.layout.item_seats, container, false)

//        this.mSelectedArena = mainScreenActivity!!.selectedArena

		val seat = setUpFragmentData(mRoom)

		val seat_title = itemSeat!!.findViewById<TextView>(R.id.seat_title)
		seat_title.text = seat.type

		val thumbnail = itemSeat!!.findViewById<SimpleDraweeView>(R.id.content)
		val uri = Uri.parse(seat.imgCarousel)
		thumbnail.setImageURI(uri)

		val root = itemSeat!!.findViewById<ChooseSeatLinearLayout>(R.id.root)
		val scale = this.arguments!!.getFloat("scale")
		root.setScaleBoth(scale)
		thumbnail.setOnClickListener { it -> listener.onSeatClick(seat) }
		return itemSeat
	}

	private fun setUpFragmentData(room: Room?): SeatModel {

		return if (!TextUtils.isEmpty(room?.usualDescription) && !TextUtils.isEmpty(room?.vipDescription)) {
			if (mRoom == null) {
				SeatModel(getString(R.string.common_hall),
				          room?.usualImageUrl ?: "", room?.usualDescription ?: ""
				)
			} else {
				SeatModel(getString(R.string.vip_hp),
				          room?.usualImageUrl ?: "", room?.usualDescription ?: ""
				)
			}
		} else if (!TextUtils.isEmpty(room?.usualDescription)) {
			SeatModel(getString(R.string.common_hall),
			          room?.usualImageUrl ?: "", room?.usualDescription ?: ""
			)
		} else if (!TextUtils.isEmpty(room?.vipDescription)) {
			SeatModel(getString(R.string.vip_hp),
			          room?.usualImageUrl ?: "", room?.usualDescription ?: ""
			)
		} else {
			SeatModel(getString(R.string.common_hall),
			          room?.usualImageUrl ?: "", room?.usualDescription ?: ""
			)

		}
	}

	companion object {

		fun newInstance(c: FragmentActivity?, room: Room?): Fragment {
			val bundle = Bundle()
			bundle.putSerializable("room", room)
			return Fragment.instantiate(c, CarouselSeatFragment::class.java.name, bundle)
		}
	}

}

