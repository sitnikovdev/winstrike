package ru.prsolution.winstrike.mvp.models

import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.R.id.seat_title

/*
 * Created by oleg on 01.02.2018.
 */


class SeatModel(var type: String) {
    var img: Int
    var imgCarousel: Int
    var title: String
    var cpu: String
    var ram: String
    var gpu: String
    var monitor: String

    init {
        if (type.contains("VIP")) {
            img = R.drawable.vip
            imgCarousel = R.drawable.vip_room
            title = "Вы выбрали: VIP room"
            cpu = "CPU: Intel Core i5-8400"
            ram = "RAM: 8Gb; SSD 120Gb+HDD 1Tb"
            gpu = "GPU: Nvidia GTX 1060 3Gb"
            monitor = "Монитор: LG 32GK850G"
        } else {
            img = R.drawable.event
            imgCarousel = R.drawable.event_room
            title = "Вы выбрали: Основной зал"
            cpu = "CPU: Intel Core i5-8400"
            ram = "RAM: 8Gb; SSD 120Gb+HDD 1Tb"
            gpu = "GPU: Nvidia GTX 1060 3Gb"
            monitor = "Монитор: LG 24GM79G"

        }
    }

}


