package ru.prsolution.winstrike.datasource.model.arena

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.arena.Arena

class ArenaListEntity(var rooms: List<ArenaEntity>)

class ArenaEntity(
    @field:Json(name = "public_id") val publicId: String? = null,
    @field:Json(name = "active_layout_pid") val activeLayoutPid: String? = null,
    @field:Json(name = "city_pid") val cityPid: String? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "metro") val metro: String? = null,
    @field:Json(name = "room_layout_pid") val roomLayoutPid: String? = null,
    @field:Json(name = "description") val description: String? = null,

    @field:Json(name = "image_url") val imageUrl: String? = null,
    @field:Json(name = "usual_description") val commonDescription: String? = null,
    @field:Json(name = "vip_description") val vipDescription: String? = null,
    @field:Json(name = "usual_image_url") val commonImageUrl: String? = null,
    @field:Json(name = "vip_image_url") val vipImageUrl: String? = null,

    @field:Json(name = "locale") val locale: String? = null
)

fun ArenaEntity.mapToDomain(): Arena =
    Arena(
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

fun List<ArenaEntity>.mapToDomain(): List<Arena> = map { it.mapToDomain() }
