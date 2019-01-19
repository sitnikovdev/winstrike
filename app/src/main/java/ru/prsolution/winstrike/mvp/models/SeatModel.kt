package ru.prsolution.winstrike.mvp.models

/*
 * Created by oleg on 01.02.2018.
 */


class SeatModel(var type: String = "VIP", imageUrl: String, usualDescription: String) {
    var imgCarousel: String = imageUrl
    var title: String = "Вы выбрали: $type"
    var cpu: String = usualDescription
}


