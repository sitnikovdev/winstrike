package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.Computer
import ru.prsolution.winstrike.domain.models.Coors
import ru.prsolution.winstrike.domain.models.End
import ru.prsolution.winstrike.domain.models.Label
import ru.prsolution.winstrike.domain.models.Offer
import ru.prsolution.winstrike.domain.models.ArenaSchema
import ru.prsolution.winstrike.domain.models.Seat
import ru.prsolution.winstrike.domain.models.Start
import ru.prsolution.winstrike.domain.models.Wall

class SchemaEntity(
		@field:Json(name = "room_layout")
		val roomLayout: ArenaSchemaEntity? = null
)

class ArenaSchemaEntity(
		@field:Json(name = "name")
		val name: String? = null,

		@field:Json(name = "create_at")
		val createAt: String? = null,

		@field:Json(name = "labels")
		val labels: List<LabelEntity>? = null,

		@field:Json(name = "places")
		val places: List<SeatEntity>? = null,

		@field:Json(name = "room_pid")
		val roomPid: String? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,


		@field:Json(name = "walls")
		val walls: List<WallEntity>? = null

)

class SeatEntity(

		@field:Json(name = "offer_pid")
		val offerPid: String? = null,

		@field:Json(name = "is_hidden")
		val isHidden: Boolean? = null,

		@field:Json(name = "computer")
		val computer: ComputerEntity? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,

		@field:Json(name = "offer")
		val offer: OfferEntity? = null,

		@field:Json(name = "computer_pid")
		val computerPid: String? = null,

		@field:Json(name = "room_layout_pid")
		val roomLayoutPid: String? = null,

		@field:Json(name = "name")
		val name: String? = null,

		@field:Json(name = "create_at")
		val createAt: String? = null,

		@field:Json(name = "coors")
		val coors: CoorsEntity? = null,

		@field:Json(name = "status")
		val status: String? = null
)

class ComputerEntity(

		@field:Json(name = "active")
		val active: Boolean? = null,

		@field:Json(name = "name")
		val name: String? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,

		@field:Json(name = "create_at")
		val createAt: String? = null
)

class OfferEntity(

		@field:Json(name = "name")
		val name: String? = null,

		@field:Json(name = "cost")
		val cost: Int? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,

		@field:Json(name = "create_at")
		val createAt: String? = null
)


class CoorsEntity(

		@field:Json(name = "id")
		val id: String? = null,

		@field:Json(name = "angle")
		val angle: Double? = null,

		@field:Json(name = "type")
		val type: Int? = null,

		@field:Json(name = "x")
		val x: Int? = null,

		@field:Json(name = "y")
		val y: Int? = null,

		@field:Json(name = "xn")
		val xn: Int? = null,

		@field:Json(name = "yn")
		val yn: Int? = null
)

class LabelEntity(

		@field:Json(name = "text")
		val text: String? = null,

		@field:Json(name = "x")
		val x: Int? = null,

		@field:Json(name = "y")
		val y: Int? = null
)

class WallEntity(

		@field:Json(name = "start")
		val start: StartEntity? = null,

		@field:Json(name = "end")
		val end: EndEntity? = null
)

class StartEntity(

		@field:Json(name = "x")
		val x: Int? = null,

		@field:Json(name = "y")
		val y: Int? = null
)

class EndEntity(

		@field:Json(name = "x")
		val x: Int? = null,

		@field:Json(name = "y")
		val y: Int? = null
)

fun ComputerEntity.mapToDomain(): Computer = Computer(active, name, publicId, createAt)

fun OfferEntity.mapToDomain(): Offer = Offer(name, cost, publicId, createAt)


fun List<SeatEntity>.mapToDomain(): List<Seat> = map { it.mapToDomain() }

fun StartEntity.mapToDomain(): Start = Start(x, y)

fun EndEntity.mapToDomain(): End = End(x, y)

fun WallEntity.mapToDomain(): Wall = Wall(start?.mapToDomain(), end?.mapToDomain())

fun List<WallEntity>.mapWallsToDomain(): List<Wall> = map { it.mapToDomain() }

fun CoorsEntity.mapToDomain(): Coors = Coors(id, angle, type, x, y, xn, yn)


fun SeatEntity.mapToDomain(): Seat = Seat(offerPid, isHidden, computer?.mapToDomain(),
                                          publicId,
                                          offer?.mapToDomain(),
                                          computerPid,
                                          roomLayoutPid,
                                          name,
                                          createAt,
                                          coors?.mapToDomain(),
                                          status
)

fun LabelEntity.mapToDomain(): Label = Label(text, x, y)

fun List<LabelEntity>.mapLabelsToDomain(): List<Label> = map { it.mapToDomain() }

fun ArenaSchemaEntity.mapRoomToDomain(): ArenaSchema =
		ArenaSchema(
				name = name,
				roomPid = roomPid,
				createAt = createAt,
				publicId = publicId,
				seats = places?.mapToDomain(),
				walls = walls?.mapWallsToDomain(),
				labels = labels?.mapLabelsToDomain()
		)

