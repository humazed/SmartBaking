package humazed.github.com.smartbaking.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import humazed.github.com.smartbaking.R
import humazed.github.com.smartbaking.adapters.RecipesAdapter
import humazed.github.com.smartbaking.model.Recipe
import humazed.github.com.smartbaking.service.RecipesService
import humazed.github.com.smartbaking.ui.step_details.StepsListActivity
import humazed.github.com.smartbaking.utils.calculateSpanCount
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        val KEY_RECIPE = "MainActivity:recipe"
    }

    var isTablet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isTablet = resources.getBoolean(R.bool.isTablet)


        RecipesService.create().recipes.enqueue(object : Callback<List<Recipe>?> {
            override fun onResponse(call: Call<List<Recipe>?>?, response: Response<List<Recipe>?>?) {
                recyclerView.adapter = response?.body()?.let { setupAdapter(it) }
                recyclerView.layoutManager = GridLayoutManager(this@MainActivity, calculateSpanCount())
            }

            override fun onFailure(call: Call<List<Recipe>?>?, t: Throwable?) {
                error { t?.message }
            }
        })

    }

    private fun setupAdapter(recipes: List<Recipe>): RecipesAdapter {
        val adapter = RecipesAdapter(recipes)
        adapter.setOnItemClickListener { _, _, position ->
            startActivity<StepsListActivity>(KEY_RECIPE to recipes[position])
        }
        return adapter
    }
}