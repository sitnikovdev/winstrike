package ru.prsolution.winstrike.presentation.model.arena

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.city.City

data class ArenaItem(
        val activeLayoutPid: String?,
        val cityPid: String?,
        val description: String?,
        val imageUrl: String?,
        val metro: String?,
        val name: String?,
        val locale: String?,
        val publicId: String?,
        val roomLayoutPid: String?,
        val commonDescription: String?,
        val commonImageUrl: String?,
        val vipDescription: String?,
        val vipImageUrl: String?
)

/**
 * Presentation layer should be responsible of mapping the domain model to an
 * appropriate presentation model and the presentation model to a domain model if needed.
 *
 * This is because domain should contain only business logic
 * and shouldn't know at all about presentation or data layers.
 */

fun List<Arena>.mapToPresentation(): List<ArenaItem> =
        map {
                ArenaItem(
                        activeLayoutPid = it.activeLayoutPid,
                        cityPid = it.cityPid,
                        description = it.description,
                        imageUrl = it.imageUrl,
                        name = it.name,
                        metro = it.metro,
                        locale = it.locale,
                        publicId = it.publicId,
                        roomLayoutPid = it.roomLayoutPid,
                        commonDescription = it.commonDescription,
                        commonImageUrl = it.commonImageUrl,
                        vipDescription = it.vipDescription,
                        vipImageUrl = it.vipImageUrl
                )
        }



fun Resource<List<Arena>>.mapToPresentation(): Resource<List<ArenaItem>> = Resource<List<ArenaItem>>(
        state = state,
        data = data?.mapToPresentation(),
        message = message
)
