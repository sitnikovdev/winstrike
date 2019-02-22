package ru.prsolution.winstrike.domain.models.arena

import ru.prsolution.winstrike.datasource.model.arena.ArenaEntity

class Arena(
    val publicId: String?,
    val activeLayoutPid: String?,
    val cityPid: String?,
    val name: String?,
    val metro: String?,
    val roomLayoutPid: String?,
    val description: String?,
    val imageUrl: String?,
    val commonDescription: String?,
    val vipDescription: String?,
    val commonImageUrl: String?,
    val vipImageUrl: String?,
    val locale: String?,
    val trs: String?,
    val trsMetro: String?,
    val exactAddress: String?
)

enum class ArenaHallType {
    DOUBLE, COMMON, VIP
}


fun Arena.mapToDatasource(): ArenaEntity = ArenaEntity(
    publicId = this.publicId,
    activeLayoutPid = this.activeLayoutPid,
    cityPid = this.cityPid,
    name = this.name,
    metro = this.metro,
    roomLayoutPid = this.roomLayoutPid,
    description = this.description,
    imageUrl = this.imageUrl,
    commonImageUrl = this.commonImageUrl,
    commonDescription = this.commonDescription,
    vipDescription = this.vipDescription,
    vipImageUrl = this.vipImageUrl,
    locale = this.locale,
    trs = this.trs,
    trsMetro = this.trsMetro,
    exactAddress = this.exactAddress
)
