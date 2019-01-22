package ru.prsolution.winstrike.domain.models

import android.graphics.Point
import ru.prsolution.winstrike.datasource.model.Wall


class Wall(wall: Wall) {
    var start: Point
    var end: Point

    init {
        var startP = wall.start
        var endP = wall.end
        var sx = startP?.x
        var sy = startP?.y
        var ex = endP?.x
        var ey = endP?.y
        this.start = Point(sx!!, sy!!)
        this.end = Point(ex!!, ey!!)
    }
}
