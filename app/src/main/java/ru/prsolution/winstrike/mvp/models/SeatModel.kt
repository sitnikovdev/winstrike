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

    init {



/*        if (type.contains("VIP HP")) {
            img = R.drawable.vip
            imgCarousel = imageUrl
            cpu = usualDescription
            title = "Вы выбрали:"
//            imgCarousel = R.drawable.vip_room
*//*            imgCarousel = imageUrl
//          cpu = "CPU: Intel Core i7-8700K"
            hhd = "HHD: 512 GB SSD NVMe 2TB 7200"
            ram = "RAM: 16GB DDR4"
            gpu = "GPU: Nvidia GTX 1070 8Gb"
            monitor = "Монитор: LG 34\" 34UC79G 144hz \\ LG 32\" 32GK850G 144hz \\ LG 24\" 24GM79G 144hz"
            garnitura = "Гарнитура: Logitech G PRO \\ Logitech G231 \\ Logitech Headset G433"
            keyboard = "Клавиатура: Logitech G512 Carbon Mechanical GX Blue"
            mouse = "Мышь: Logitech G102 Prodigy \\ Logitech G403 Prodigy"
            carpet = "Ковер: Logitech G440 \\ Logitech G240"*//*
        } else if (type.contains("VIP LG")) {
            img = R.drawable.vip
            imgCarousel = imageUrl
            cpu = usualDescription
            title = "Вы выбрали:"

*//*            title = "Вы выбрали: VIP LG"
            hhd = "HHD: 512 GB SSD NVMe 2TB 7200"
            ram = "RAM: 16GB DDR4"
            gpu = "GPU: Nvidia GTX 1080 8Gb"
            monitor = "Монитор: LG 34\" 34UC79G 144hz \\ LG 27\" 27GK750F 240hz"
            garnitura = "Гарнитура: Sennheiser GSP 600 + GSX 1200 PRO \\ Sennheiser GSP 300"
            keyboard = "Клавиатура: Logitech G513 Carbon Mechanical Romer-G"
            mouse = "Мышь: Logitech Mouse G PRO + Charging Pad"
            carpet = "Ковер: Logitech Charging Pad"*//*
        } else if (type.contains("Общий")) {
            img = R.drawable.vip
            imgCarousel = imageUrl
            cpu = usualDescription
            title = "Вы выбрали:"
            img = R.drawable.event
            imgCarousel = imageUrl
            title = "Вы выбрали: Общий зал"
//            cpu = "CPU: Intel Core i5-8600K"
            cpu = usualDescription
            hhd = "HHD: 512 GB SSD NVMe 2TB 7200"
            ram = "RAM: 16GB DDR4"
            gpu = "GPU: nVidia GTX 1070 8GB"
            monitor = "Монитор: LG34'' 32GK850G 144hz \\  LG 24 24GM79G 144hz"
            garnitura = "Гарнитура: Logitech G PRO \\ Logitech G231 \\ Logitech Headset G433"
            keyboard = "Клавиатура: Logitech G512 Carbon Mechanical GX Blue"
            mouse = "Мышь: Logitech G102 Prodigy \\ Logitech G403 Prodigy"
            carpet = "Ковер: Logitech G440 \\ Logitech G240"
        } else {
            img = R.drawable.event
//            imgCarousel = R.drawable.event_room
            imgCarousel = imageUrl
            title = "Вы выбрали: Общий зал"
//            cpu = "CPU: Intel Core i5-8600K"
            cpu = usualDescription
            hhd = "HHD: 512 GB SSD NVMe 2TB 7200"
            ram = "RAM: 16GB DDR4"
            gpu = "GPU: nVidia GTX 1070 8GB"
            monitor = "Монитор: LG34'' 32GK850G 144hz \\  LG 24 24GM79G 144hz"
            garnitura = "Гарнитура: Logitech G PRO \\ Logitech G231 \\ Logitech Headset G433"
            keyboard = "Клавиатура: Logitech G512 Carbon Mechanical GX Blue"
            mouse = "Мышь: Logitech G102 Prodigy \\ Logitech G403 Prodigy"
            carpet = "Ковер: Logitech G440 \\ Logitech G240"
        }*/

    }

}


