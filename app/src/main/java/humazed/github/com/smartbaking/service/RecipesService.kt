package humazed.github.com.smartbaking.service

import humazed.github.com.smartbaking.model.Recipe
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface RecipesService {
    @get:GET("baking.json")
    val recipes: Call<List<Recipe>>

    /**
     * Companion object to create the RecipeService
     */
    companion object {
        fun create(): RecipesService {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking")
                    .build()

            return retrofit.create(RecipesService::class.java)
        }
    }
}

