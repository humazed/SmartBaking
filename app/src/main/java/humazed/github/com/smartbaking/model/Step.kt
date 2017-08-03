package humazed.github.com.smartbaking.model

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable


@SuppressLint("ParcelCreator")
data class Step(
        val id: Int,
        val shortDescription: String,
        val description: String,
        val videoURL: String,
        val thumbnailURL: String
) : AutoParcelable