package ru.prsolution.winstrike.datasource.model

import ru.prsolution.winstrike.domain.models.Arena

class ArenaListEntity(var rooms: List<ArenaEntity>)

fun ArenaEntity.mapToDomain(): Arena = Arena(
		publicId,
		activeLayoutPid,
		cityPid,
		name,
		metro,
		roomLayoutPid,
		description,
		imageUrl,
		commonDescription,
		vipDescription,
		commonImageUrl,
		vipImageUrl,
		locale
)

fun List<ArenaEntity>.mapToDomain(): List<Arena> = map { it.mapToDomain()}

