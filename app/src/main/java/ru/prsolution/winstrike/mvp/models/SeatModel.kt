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
    var garnitura: String
    var keyboard: String
    var mouse: String
    var carpet: String

    init {
        if (type.contains("VIP HP")) {
            img = R.drawable.vip
            imgCarousel = R.drawable.vip_room
            title = "Вы выбрали: VIP HP"
            cpu = "CPU: Intel Core i5-8400"
            ram = "RAM: 8Gb; SSD 120Gb+HDD 1Tb"
            gpu = "GPU: Nvidia GTX 1060 3Gb"
            monitor = "Монитор: LG 32GK850G"
            garnitura = ""
            keyboard = ""
            mouse = ""
            carpet = ""
        } else if (type.contains("VIP LG")) {
            img = R.drawable.vip
            imgCarousel = R.drawable.vip_room
            title = "Вы выбрали: VIP LG"
            cpu = "CPU: Intel Core i5-8400"
            ram = "RAM: 8Gb; SSD 120Gb+HDD 1Tb"
            gpu = "GPU: Nvidia GTX 1060 3Gb"
            monitor = "Монитор: LG 32GK850G"
            garnitura = ""
            keyboard = ""
            mouse = ""
            carpet = ""
        } else if (type.contains("Общий")) {
            img = R.drawable.event
            imgCarousel = R.drawable.event_room
            title = "Вы выбрали: Общий зал"
            cpu = "CPU: Intel Core i5-8600K"
            ram = "RAM: 16GB DDR4; 512 GB SSD NVMe  2TB 7200"
            gpu = "GPU: nVidia GTX 1070 8GB"
            monitor = "Монитор: LG34'' 32GK850G 144hz \\  LG 24 24GM79G 144hz"
            garnitura = "Гарнитура: Logitech G PRO \\ Logitech G231 \\ Logitech Headset G433"
            keyboard = "Клавиатура: Logitech G512 Carbon Mechanical GX Blue"
            mouse = "Мышь: Logitech G102 Prodigy \\ Logitech G403 Prodigy"
            carpet = "Ковер: Logitech G440 \\ Logitech G240"
        } else {
            img = R.drawable.event
            imgCarousel = R.drawable.event_room
            title = "Вы выбрали: Общий зал"
            cpu = "CPU: Intel Core i5-8600K"
            ram = "RAM: 16GB DDR4; 512 GB SSD NVMe  2TB 7200"
            gpu = "GPU: nVidia GTX 1070 8GB"
            monitor = "Монитор: LG34'' 32GK850G 144hz \\  LG 24 24GM79G 144hz"
            garnitura = "Гарнитура: Logitech G PRO \\ Logitech G231 \\ Logitech Headset G433"
            keyboard = "Клавиатура: Logitech G512 Carbon Mechanical GX Blue"
            mouse = "Мышь: Logitech G102 Prodigy \\ Logitech G403 Prodigy"
            carpet = "Ковер: Logitech G440 \\ Logitech G240"

        }

    }

}


