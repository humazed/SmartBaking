package humazed.github.com.smartbaking.utils

import android.content.Context

/**
 * User: YourPc
 * Date: 8/2/2017
 */

fun Context.calculateSpanCount(): Int {
    val displayMetrics = resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / 130).toInt()
}
