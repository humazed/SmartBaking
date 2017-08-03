package humazed.github.com.smartbaking.ui.step_details

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import humazed.github.com.smartbaking.R
import kotlinx.android.synthetic.main.activity_step_detail.*

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
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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
            navigateUpTo(Intent(this, StepsListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
