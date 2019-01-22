package ru.prsolution.winstrike.domain.models

import android.view.View
import ru.prsolution.winstrike.datasource.model.Coors

//
//  SeatApi.swift
//  winstrike
//
//  Created by PRS on 28/02/2018.
//  Copyright © 2018 PR_Solution. All rights reserved.
//


data class Seat(val json: Coors?, var pid: String?, var seatType: String?, var name: String?, var status: Boolean?) {
	var id: String
	var dx: Double = 0.0
	var dy: Double = 0.0
	var angle: Double = 0.0
	var type: SeatType
	var publicPid: String
	var pcname: String
	var pcstatus: Boolean = false
	var mOnClickListener: View.OnClickListener? = null

	init {

		var x = json?.x
		var y = json?.y
		var angle = json?.angle
		var type = SeatType.get(seatType)
		var id = json?.id

		this.publicPid = pid.toString()
		this.id = id.toString()
		this.dx = x!!.toDouble()
		this.dy = y!!.toDouble()
		this.angle = angle!!
		this.type = type!!
		this.pcname = name.toString()
		this.pcstatus = this.status!!
	}


	fun setOnClickListener(listener: View.OnClickListener?) {
		this.mOnClickListener = listener
	}

	fun getOnClickListener(): View.OnClickListener? {
		return this.mOnClickListener
	}

}
