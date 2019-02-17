package ru.prsolution.winstrike.presentation.utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.prsolution.winstrike.presentation.utils.Constants.ENTER_DURATION
import ru.prsolution.winstrike.presentation.utils.Constants.EXIT_DURATION

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun SwipeRefreshLayout.startRefreshing() {
    isRefreshing = true
}

fun SwipeRefreshLayout.stopRefreshing() {
    isRefreshing = false
}

//Inflate a Layout
/**
 * context.inflate(R.layout.my_layout)
 */

fun Context.inflate(res: Int, parent: ViewGroup? = null) : View {
    return LayoutInflater.from(this).inflate(res, parent, false)
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)


// fun ImageView.loadImage(url: String) = Glide.with(this).load(url).into(this)

fun TextView.setColor(color: Int) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    this.setTextColor(context.getColor(color))
} else {
    TODO("VERSION.SDK_INT < M")
}

fun BottomNavigationView.hide() {
    with(this) {
        if (visibility == View.VISIBLE && alpha == 1f) {
            animate()
                .alpha(0f)
                .withEndAction { visibility = View.GONE }
                .duration = EXIT_DURATION
        }
    }
}

fun BottomNavigationView.show() {
    with(this) {
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .duration = ENTER_DURATION
    }
}

// Retrieving a Color resource across API levels
/**
 * use context.color(R.color.my_color)
 */

fun Context.color(@ColorRes id: Int) = when {
    isAtLeastMarshmallow() -> {
        resources.getColor(id, null)
    }
    else -> resources.getColor(id)
}

fun isAtLeastMarshmallow(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

// View with layout
/**
 * view.setHeight(newHeight)
 */
fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
}
