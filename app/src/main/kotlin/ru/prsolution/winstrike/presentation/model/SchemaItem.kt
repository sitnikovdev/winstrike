package ru.prsolution.winstrike.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import ru.prsolution.winstrike.domain.models.*

// TODO: Replace with no parcebale
/*Parcel: unable to marshal value ru.prsolution.winstrike.domain.models.Seat@3189b34
at android.os.Parcel.writeValue(Parcel.java:1736)
at ru.prsolution.winstrike.presentation.model.SchemaItem.writeToParcel(Unknown Source:57)*/
@Parcelize
data class SchemaItem(
    val name: String? = null,
    val roomPid: String? = null,
    val createAt: String? = null,
    val publicId: String? = null,
    val seats: @RawValue List<Seat>? = null,
    val walls: @RawValue List<Wall>? = null,
    val labels: @RawValue List<Label>? = null
) : Parcelable

/**
 * Presentation layer should be responsible of mapping the domain model to an
 * appropriate presentation model and the presentation model to a domain model if needed.
 *
 * This is because domain should contain only business logic
 * and shouldn't know at all about presentation or data layers.
 */


fun ArenaSchema.mapToPresentation(): SchemaItem =
    SchemaItem(
        name = name,
        roomPid = roomPid,
        createAt = createAt,
        publicId = publicId,
        seats = seats,
        walls = walls,
        labels = labels
    )

fun SchemaItem.mapToDomain(): ArenaSchema =
    ArenaSchema(
        name = name,
        roomPid = roomPid,
        createAt = createAt,
        publicId = publicId,
        seats = seats,
        walls = walls,
        labels = labels
    )
