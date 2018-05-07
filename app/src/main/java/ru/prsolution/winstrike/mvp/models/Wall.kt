package ru.prsolution.winstrike.mvp.models

import android.graphics.Point
import ru.prsolution.winstrike.mvp.apimodels.Wall


class Wall(wallData: Wall) {
    lateinit var start: Point
    lateinit var end: Point

    init  {
            var startP = wallData.start
            var endP = wallData.end
            var sx = startP.x
            var sy = startP.y
            var ex = endP.x
            var ey = endP.y

        this.start = Point(sx,  sy)
        this.end = Point(ex, ey)
    }
}