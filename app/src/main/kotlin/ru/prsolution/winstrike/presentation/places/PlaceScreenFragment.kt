package ru.prsolution.winstrike.presentation.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.orders.OrderModel
import ru.prsolution.winstrike.presentation.utils.vpadapter.SeatAdapter
import java.util.ArrayList


class PlaceScreenFragment : Fragment() {
	private val mPayList = ArrayList<OrderModel>()


	private var mSeatAdapter: SeatAdapter? = null

	var rv_pay: RecyclerView? = null


	var presenter: PlacesPresenter? = null


	fun onGetOrdersSuccess(orders: ArrayList<OrderModel>) {
//		this.orders = orders
//		mViewModel.onTabPlaceClick(this.orders)
//		Timber.d("UserEntity order list size: %s", this.orders.size)
	}

	fun onGetOrdersFailure(appErrorMessage: String) {
//		orders = ArrayList()
//		mViewModel.onTabPlaceClick(orders)
//		Timber.d("Failure get layout from server: %s", appErrorMessage)
	}



	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		var view: View

		view = inflater.inflate(R.layout.fmt_nopaid, container, false)
		mSeatAdapter = SeatAdapter(mPayList)
		if (mPayList == null) {
			view = inflater.inflate(R.layout.fmt_nopaid, container, false)
		}
		if (!mPayList.isEmpty()) {
/*			binding = DataBindingUtil.inflate(inflater, R.layout.fmt_paid, container, false)
			view = binding.root
			binding.adapter = mSeatAdapter*/
			initRView()
		} else {
			view = inflater.inflate(R.layout.fmt_nopaid, container, false)
		}

		return view
	}

	private fun initRView() {
		/*    binding.rvPay.addItemDecoration(new BottomDecoratorHelper(350));
    binding.rvPay.setLayoutManager(new LinearLayoutManager(getActivity()));
    binding.rvPay.setAdapter(mSeatAdapter);*/
	}




	override fun onStop() {
		super.onStop()
		presenter!!.onStop()
	}

	 fun showWait() {}

	 fun removeWait() {}

}
