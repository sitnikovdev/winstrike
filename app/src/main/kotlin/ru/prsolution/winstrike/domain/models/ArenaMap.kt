package ru.prsolution.winstrike.domain.models

import android.graphics.Point


class ArenaMap(room: ArenaSchema?) {

	var name: String? = room?.name
	var walls: MutableList<WallModel> = mutableListOf()
	var labels: MutableList<Label> = mutableListOf()
	var seats: MutableList<SeatMap> = mutableListOf()
	var arenaSchema: ArenaSchemaName = ArenaSchemaName.WINSTRIKE

	init {
		when {
			this.name?.contains("Winstrike Arena 1")!! -> arenaSchema = ArenaSchemaName.WINSTRIKE
			this.name?.contains("Schema 2")!! -> arenaSchema = ArenaSchemaName.CORNER
			this.name?.contains("Серпухов")!! -> arenaSchema = ArenaSchemaName.SERPUCHOV
		}
		val seats = room?.seats
		if (seats != null) {
			for (seat in seats) {

				val computer = seat.computer
				val coors = seat.coors
				var type = if ((coors?.type) == 1 && seat.status != "hidden") "vip" else seat.status
				if (type == "vip") {
					if (seat.status == "booking" || seat.status == "self_booking") {
						type = seat.status
					}
				}

				this.seats.add(SeatMap(coors, seat.publicId, type, computer?.name,
				                       computer?.active))
			}
		}

		val labels = room?.labels
		if (labels != null) {
			for (label in labels) {
				val text = label.text
				val dx = label.x
				val dy = label.y

				this.labels.add(Label(text, dx, dy))
			}
		}

		for (wallData in room?.walls!!) {
			this.walls.add(WallModel(wallData))
		}

	}
}


class Label(
		val text: String? = null,
		val x: Int? = null,
		val y: Int? = null
)


class WallModel(wall: Wall) {
	var start: Point
	var end: Point

	init {
		val startP = wall.start
		val endP = wall.end
		val sx = startP?.x
		val sy = startP?.y
		val ex = endP?.x
		val ey = endP?.y
		this.start = Point(sx!!, sy!!)
		this.end = Point(ex!!, ey!!)
	}
}

enum class ArenaSchemaName {
	WINSTRIKE, CORNER, SERPUCHOV
}
