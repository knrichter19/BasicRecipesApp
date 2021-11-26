package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private String apiKey;
    private RequestQueue queue;
    private RecyclerView recipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView t = findViewById(R.id.recipeResults);
        recipeView = findViewById(R.id.rvRecipes);
        // do this later
        queue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/recipes/findByIngredients";

        this.apiKey = "7f9718e13529466691df6206e109c755";
    }

    public void searchByIngredients(View v){
        String endPoint = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + this.apiKey;
        TextView t = (TextView) findViewById(R.id.ingredientInput);
        String ingredients = t.getText().toString();
        Log.d("ingredients", ingredients);
        String joined = ingredients.replaceAll("\n", ",");
        //CharSequence joined = TextUtils.replace(ingredients,new String[]{"\n"},new CharSequence[]{","});
        Log.d("ingredients", joined);
        endPoint = endPoint + "&ranking=1&ingredients=" + joined;
        // todo: option to toggle ranking

        TextView results = (TextView) findViewById(R.id.recipeResults);

        JsonArrayRequest request = new JsonArrayRequest(endPoint, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                StringBuilder shittyResults = new StringBuilder(); //todo: distinct objects instead of big string
                //results.setText(response.toString());
                ArrayList<Recipe> recipes = null;
                try {
                    recipes = parseRecipeResults(response);
                } catch (JSONException e) {
                    results.setText(e.getMessage());
                }
                if (recipes != null && !recipes.isEmpty()){
                    for (Recipe recipe : recipes){
//                        shittyResults.append(recipe.get(0)).append(":\n");
//                        for (String ingredient : recipe.subList(1,recipe.size()-1)){
//                            shittyResults.append("\t").append(ingredient).append("\n");
//                        }
                    }
                }
                // todo: error check here + move elsewhere
                results.setText(shittyResults.toString());
                RecipesAdapter adapter = new RecipesAdapter(recipes);
                recipeView.setAdapter(adapter);
                recipeView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                results.setText(error.getMessage());
            }
        });

        queue.add(request);
    }

    private ArrayList<Recipe> parseRecipeResults(JSONArray response) throws JSONException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        // parse response and print out nicely
        for (int i = 0; i < response.length(); i++){
            Recipe newRecipe = new Recipe();
            ArrayList<String> ingredientArray = new ArrayList<>();
            JSONObject recipe = response.getJSONObject(i);
            String title = recipe.getString("title");
            newRecipe.setName(title);
            newRecipe.setId(recipe.getString("id"));
            JSONArray usedIngredients = recipe.getJSONArray("usedIngredients");
            JSONArray missingIngredients = recipe.getJSONArray("missedIngredients");

            for (int j = 0; j < usedIngredients.length(); j++){
                JSONObject ingredient = usedIngredients.getJSONObject(j);
                String name = ingredient.getString("name");
                String amount = ingredient.getString("amount");
                String unit = ingredient.getString("unit");
                ingredientArray.add(amount + " " + unit + " " + name);
            }
            for (int j = 0; j < missingIngredients.length(); j++){
                JSONObject ingredient = missingIngredients.getJSONObject(j);
                String name = ingredient.getString("name");
                String amount = ingredient.getString("amount");
                String unit = ingredient.getString("unit");
                ingredientArray.add(amount + " " + unit + " " + name);
            }
            newRecipe.setIngredients(ingredientArray);
            recipes.add(newRecipe);
            Log.d("title", title);
            Log.d("used_ingredients", usedIngredients.toString());
            Log.d("missing_ingredients", missingIngredients.toString());
        }
        Log.d("full hashmap", recipes.toString());
        return recipes;
    }

    public void expandRecipe(View v){

    }
}