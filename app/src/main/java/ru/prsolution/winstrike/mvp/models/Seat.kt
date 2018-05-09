package ru.prsolution.winstrike.mvp.models

import ru.prsolution.winstrike.mvp.apimodels.Coors

//
//  SeatApi.swift
//  winstrike
//
//  Created by PRS on 28/02/2018.
//  Copyright © 2018 PR_Solution. All rights reserved.
//


class Seat(json: Coors, publicPid: String, seatType: String, pcname: String, pcstatus: Boolean) {
    var id: String
    var dx: Double = 0.0
    var dy: Double = 0.0
    var angle: Double = 0.0
    var type: SeatType
    var publicPid: String
    var pcname: String
    var pcstatus: Boolean = false


    init  {

            var x = json.x
            var y = json.y
            var angle = json.angle
            var type = SeatType.get(seatType)
            var id = json.id

        this.publicPid = publicPid
        this.id = id
        this.dx = x.toDouble()
        this.dy = y.toDouble()
        this.angle = angle
        this.type = type!!
        this.pcname = pcname
        this.pcstatus = pcstatus
    }
}
