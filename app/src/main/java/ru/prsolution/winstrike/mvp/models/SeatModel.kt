package ru.prsolution.winstrike.mvp.models

import org.jetbrains.annotations.NotNull

/*
 * Created by oleg on 01.02.2018.
 */


class SeatModel(var type: @NotNull String, imageUrl: @NotNull String, usualDescription: String) {
    var imgCarousel: String = imageUrl
    var title: String = "Вы выбрали: $type"
    var cpu: String = usualDescription
    var hhd: String = ""
    var ram: String = ""
    var gpu: String = ""
    var monitor: String = ""
    var garnitura: String = ""
    var keyboard: String = ""
    var mouse: String = ""
    var carpet: String = ""

}


