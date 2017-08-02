package humazed.github.com.smartbaking.model

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Ingredient(val quantity: String,
                      val measure: String,
                      val ingredient: String) : AutoParcelable