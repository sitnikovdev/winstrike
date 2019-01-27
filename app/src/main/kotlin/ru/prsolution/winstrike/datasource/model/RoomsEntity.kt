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

class RoomsEntity(
		@field:Json(name = "room_layout")
		val roomLayout: RoomEntity? = null
)

fun ComputerEntity.mapToDomain(): Computer = Computer(active, name, publicId, createAt)

fun OfferEntity.mapToDomain(): Offer = Offer(name, cost, publicId, createAt)


fun List<PlaceEntity>.mapToDomain(): List<Seat> = map { it.mapToDomain() }

fun StartEntity.mapToDomain(): Start = Start(x, y)

fun EndEntity.mapToDomain(): End = End(x, y)

fun WallEntity.mapToDomain(): Wall = Wall(start?.mapToDomain(), end?.mapToDomain())

fun List<WallEntity>.mapWallsToDomain(): List<Wall> = map { it.mapToDomain() }

fun CoorsEntity.mapToDomain(): Coors = Coors(id, angle, type, x, y, xn, yn)


fun PlaceEntity.mapToDomain(): Seat = Seat(offerPid, isHidden, computer?.mapToDomain(),
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

fun RoomEntity.mapRoomToDomain(): ArenaSchema =
		ArenaSchema(
				name = name,
				roomPid = roomPid,
				createAt = createAt,
				publicId = publicId,
				seats = places?.mapToDomain(),
				walls = walls?.mapWallsToDomain(),
				labels = labels?.mapLabelsToDomain()
		)

