package ru.prsolution.winstrike.domain.models

class Room(
		val name: String? = null,
		val roomPid: String? = null,
		val createAt: String? = null,
		val publicId: String? = null,
		val seats: List<Seat>? = null,
		val walls: List<Wall>? = null,
		val labels: List<Label>? = null
)
