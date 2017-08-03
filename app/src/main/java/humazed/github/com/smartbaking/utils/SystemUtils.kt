package humazed.github.com.smartbaking.utils

import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * User: YourPc
 * Date: 8/3/2017
 */
fun hideSystemUI(activity: AppCompatActivity) {
    activity.supportActionBar?.hide()
    //Use Google's "LeanBack" mode to get fullscreen in landscape
    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_IMMERSIVE
}
