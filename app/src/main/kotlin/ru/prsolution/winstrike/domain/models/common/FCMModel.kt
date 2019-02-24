package ru.prsolution.winstrike.domain.models.common

import ru.prsolution.winstrike.datasource.model.fcm.FCMEntity

/**
 * Created by oleg on 07/03/2018.
 */

class FCMModel(val token: String)

fun FCMModel.mapToDataSource(): FCMEntity = FCMEntity(
    token = this.token
)
