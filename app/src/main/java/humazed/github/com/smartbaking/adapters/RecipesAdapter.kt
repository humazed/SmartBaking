package humazed.github.com.smartbaking.adapters

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import humazed.github.com.smartbaking.R
import humazed.github.com.smartbaking.model.Recipe


/**
 * User: YourPc
 * Date: 7/22/2017
 */
class RecipesAdapter(result: List<Recipe>) :
        BaseQuickAdapter<Recipe, BaseViewHolder>(R.layout.row_recipe, result) {

    override fun convert(helper: BaseViewHolder, result: Recipe) {

        result.apply {
            helper.apply {
                setText(R.id.recipeNameTextView, name)

                Glide.with(mContext)
                        .load(image)
                        .apply(RequestOptions().placeholder(R.drawable.ic_cake_white_24dp))
                        .into(getView<ImageView>(R.id.recipeImageView))
            }
        }

    }
}

