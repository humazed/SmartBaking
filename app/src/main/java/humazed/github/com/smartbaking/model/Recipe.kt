package humazed.github.com.smartbaking.model

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class Recipe(
        val id: Int,
        val name: String,
        val ingredients: List<Ingredient>,
        val steps: List<Step>,
        val servings: Int,
        val image: String
) : AutoParcelable