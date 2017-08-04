package humazed.github.com.smartbaking.ui.step_details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import humazed.github.com.smartbaking.R

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [StepsListActivity].
 */
class StepDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_detail)


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, StepDetailFragment
                            .newInstance(intent.getParcelableExtra(StepsListActivity.KEY_STEP)))
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
