package humazed.github.com.smartbaking.ui.step_details

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils.navigateUpFromSameTask
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import humazed.github.com.smartbaking.R
import humazed.github.com.smartbaking.adapters.StepsAdapter
import humazed.github.com.smartbaking.model.Recipe
import humazed.github.com.smartbaking.model.Step
import humazed.github.com.smartbaking.ui.MainActivity
import kotlinx.android.synthetic.main.activity_steps_list.*
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
        val KEY_STEP = "StepsListActivity:step"
    }

    private var mTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = title

        mTwoPane = resources.getBoolean(R.bool.isTablet)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val recipe = intent.getParcelableExtra<Recipe>(MainActivity.KEY_RECIPE)

        ingredientsTextView.text = recipe.ingredients
                .map { it.toString() }
                .reduce { acc, s -> acc + s }

        setupRecyclerView(recipe.steps)
    }

    private fun setupRecyclerView(steps: List<Step>) {
        val adapter = StepsAdapter(steps)
        adapter.setOnItemClickListener { _, _, position ->
            val step = steps[position]
            if (mTwoPane) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.recipe_detail_container, StepDetailFragment.newInstance(step))
                        .commit()
            } else {
                startActivity<StepDetailActivity>(KEY_STEP to step)
            }

        }
        stepsRecyclerView.adapter = adapter
        stepsRecyclerView.layoutManager = LinearLayoutManager(this)
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
