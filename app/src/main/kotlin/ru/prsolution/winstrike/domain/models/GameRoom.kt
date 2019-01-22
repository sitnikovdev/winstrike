package ru.prsolution.winstrike.domain.models

import ru.prsolution.winstrike.datasource.model.RoomLayout


class GameRoom(room: RoomLayout?) {

	var walls: MutableList<Wall> = mutableListOf()
	var labels: MutableList<LabelRoom> = mutableListOf()
	var seats: MutableList<Seat> = mutableListOf()

	init {
		var seats = room?.places;
		var labels = room?.labels;

		for (wallData in room?.walls!!) {
			this.walls.add(Wall(wallData))
		}

		if (seats != null) {
			for (seatData in seats) {
				var seat: Seat

				var pcinfo = seatData.computer
				var coors = seatData.coors
				var realStatus = seatData.status

				var type = if ((coors?.type) == 1 && realStatus != "hidden") "vip" else seatData.status

				if (type == "vip") {
					if (seatData.status == "booking" || seatData.status == "self_booking") {
						type = seatData.status
					}
				}

				seat = Seat(coors, seatData.publicId, type, pcinfo?.name,
				            pcinfo?.active)
				this.seats.add(seat)
			}
		}

		if (labels != null) {
			for (labelData in labels) {

				var text = labelData.text
				var dx = labelData.x
				var dy = labelData.y

				var label = LabelRoom(text, dx, dy)
				this.labels.add(label)
			}
		}
	}
}
