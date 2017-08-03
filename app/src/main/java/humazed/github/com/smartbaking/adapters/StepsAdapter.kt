package humazed.github.com.smartbaking.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import humazed.github.com.smartbaking.R
import humazed.github.com.smartbaking.model.Step


/**
 * User: YourPc
 * Date: 7/22/2017
 */
class StepsAdapter(steps: List<Step>) :
        BaseQuickAdapter<Step, BaseViewHolder>(R.layout.row_step, steps) {

    override fun convert(helper: BaseViewHolder, result: Step) {

        result.apply {
            helper.apply {
                setText(R.id.nameTextView, shortDescription)

//                Glide.with(mContext)
//                        .load(image)
//                        .apply(RequestOptions().placeholder(R.drawable.ic_cake_white_24dp))
//                        .into(getView<ImageView>(R.id.recipeImageView))
            }
        }

    }
}

