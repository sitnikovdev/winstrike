package ru.prsolution.winstrike.presentation.main.carousel

/*
 * Created by oleg on 01.02.2018.
 */

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.facebook.drawee.view.SimpleDraweeView
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.RoomSeatType
import ru.prsolution.winstrike.domain.models.SeatCarousel

class CarouselFragment : Fragment() {

    lateinit var mListener: OnSeatClickListener
    private var mSeat: SeatCarousel? = null

    interface OnSeatClickListener {

        fun onSeatClick(seat: SeatCarousel?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSeatClickListener) {
            mListener = context
        } else {
            throw ClassCastException(
                "$context must implements OnChoosePlaceButtonsClickListener "
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            mSeat = arguments?.getSerializable("room") as SeatCarousel
        } catch (e: Exception) {
            mSeat = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.item_carousel, container, false)

        val seatTitle = view!!.findViewById<TextView>(R.id.seat_name_tv)

        val seatImage: SimpleDraweeView = view.findViewById(R.id.seat_image_iv)

        seatImage.setOnClickListener {
            mListener.onSeatClick(mSeat)
        }

        val imageUri = Uri.parse(mSeat?.imageUrl)
        seatImage.setImageURI(imageUri)

        if (mSeat?.type == RoomSeatType.COMMON) {
            seatTitle.text = getString(R.string.common_hall)
        } else {
            seatTitle.text = getString(R.string.vip_hall)
        }

        return view
    }

    companion object {

        fun newInstance(fm: FragmentManager?, room: SeatCarousel?): Fragment? {
            val bundle = Bundle()
            bundle.putSerializable("room", room)

            return fm?.fragmentFactory?.instantiate(
                ClassLoader.getSystemClassLoader(),
                CarouselFragment::class.java.name,
                bundle
            )
        }
    }
}
