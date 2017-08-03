package humazed.github.com.smartbaking.ui.step_details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import humazed.github.com.smartbaking.R
import humazed.github.com.smartbaking.model.Step
import kotlinx.android.synthetic.main.activity_step_detail.*
import kotlinx.android.synthetic.main.step_detail.view.*

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a [RecipeListActivity]
 * in two-pane mode (on tablets) or a [RecipeDetailActivity]
 * on handsets.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class StepDetailFragment : Fragment() {

    lateinit var step: Step


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments.containsKey(ARG_STEP)) {
            step = arguments.getParcelable(ARG_STEP)

            activity.toolbar_layout?.title = step.shortDescription
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.step_detail, container, false)

        rootView.recipe_detail.text = step.description

        return rootView
    }

    companion object {
        val ARG_STEP = "StepDetailFragment:step"

        fun newInstance(step: Step): StepDetailFragment {
            val fragment = StepDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_STEP, step)
            fragment.arguments = args
            return fragment
        }
    }

}