package ru.prsolution.winstrike.presentation.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.common.BackButtonListener
import ru.prsolution.winstrike.datasource.model.OrderModel
import ru.prsolution.winstrike.presentation.utils.vpadapter.SeatAdapter
import ru.prsolution.winstrike.networking.Service
import java.util.ArrayList


class PlaceScreenFragment : Fragment(),  BackButtonListener {
	private val mPayList = ArrayList<OrderModel>()


	private var mSeatAdapter: SeatAdapter? = null

	var rv_pay: RecyclerView? = null

	var service: Service? = null

	var presenter: PlacesPresenter? = null


	/*  PlacesPresenter provideMainScreenPresenter() {
    return new PlacesPresenter(service,
        ((RouterProvider) getParentFragment()).getRouter()
        , getArguments().getParcelableArrayList(ORDERS)
    );
  }*/


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


	override fun onBackPressed(): Boolean {
		//    startActivity(new Intent(getActivity(), MainScreenActivity.class));
		return true
	}


	override fun onStop() {
		super.onStop()
		presenter!!.onStop()
	}

	 fun showWait() {}

	 fun removeWait() {}


	protected fun toast(message: String) {

		//    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	companion object {

		private val ARENA_NAME = "extra_name"
		private val ORDERS = "extra_number"


		fun getNewInstance(name: String, orders: ArrayList<OrderModel>): PlaceScreenFragment {
			val fragment = PlaceScreenFragment()
			val arguments = Bundle()
			arguments.putString(ARENA_NAME, name)
/*			arguments.putParcelableArrayList(
					ORDERS, orders)*/
			//    fragment.setArguments(arguments);
			return fragment
		}
	}
}
