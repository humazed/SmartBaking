package humazed.github.com.smartbaking.ui.step_details

import android.os.Bundle
import android.support.v4.app.NavUtils.navigateUpFromSameTask
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import humazed.github.com.smartbaking.R
import humazed.github.com.smartbaking.adapters.StepsAdapter
import humazed.github.com.smartbaking.model.Recipe
import humazed.github.com.smartbaking.model.Step
import humazed.github.com.smartbaking.ui.MainActivity
import kotlinx.android.synthetic.main.steps_list_layout.*
import org.jetbrains.anko.startActivity

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [StepDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class StepsListActivity : AppCompatActivity() {
    companion object {
        val KEY_STEPS = "StepsListActivity:mStep"
        val KEY_POSITION = "StepsListActivity:mPosition"
    }

    private var mTwoPane: Boolean = false
    lateinit var recipe: Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title

        mTwoPane = resources.getBoolean(R.bool.isTablet)

        recipe = intent.getParcelableExtra<Recipe>(MainActivity.KEY_RECIPE).also {
            ingredientsTextView.text = it.ingredients
                    .map { it.toString() }
                    .reduce { acc, s -> acc + s } //to concat the ingredients in one string

            setupRecyclerView(ArrayList(it.steps))
        }
    }

    private fun setupRecyclerView(steps: ArrayList<Step>) {
        val adapter = StepsAdapter(steps)
        adapter.setOnItemClickListener { _, _, position ->
            if (mTwoPane) {
                StepDetailFragment.start(this, steps, position)
            } else {
                startActivity<StepDetailActivity>(KEY_STEPS to steps, KEY_POSITION to position)
            }
        }
        stepsRecyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpFromSameTask(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
