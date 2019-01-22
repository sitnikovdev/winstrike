package ru.prsolution.winstrike.datasource.model

class SeatApi(var public_id: String, var seatXLeft: Int?, var seatYTop: Int?, var seatAngle: Double?,
              var seatType: Int?, var seatStatus: String) {
	var isSelected: Boolean = false
}
