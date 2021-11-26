package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private VolleySingleton requests;
    private RecyclerView recipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView t = findViewById(R.id.recipeResults);
        recipeView = findViewById(R.id.rvRecipes);

        // set up API connection
        requests = VolleySingleton.getInstance(this);

        queue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/recipes/findByIngredients";

        this.apiKey = "7f9718e13529466691df6206e109c755";
    }

    public void searchByIngredients(View v){
        TextView t = (TextView) findViewById(R.id.ingredientInput);
        String ingredients = t.getText().toString();
        String joined = ingredients.replaceAll("\n", ",");
        Log.d("ingredients", joined);
        // todo: option to toggle ranking

        // todo: move to new class
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Recipe> recipes = null;
                try {
                    recipes = parseRecipeResults(response);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this,"Error interpreting results", Toast.LENGTH_LONG).show();
                }

                // todo: error check here + move elsewhere
                RecipesAdapter adapter = new RecipesAdapter(recipes);
                recipeView.setAdapter(adapter);
                recipeView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error retrieving results", Toast.LENGTH_LONG).show();
            }
        };

        requests.setApiKey(this.apiKey);
        requests.requestRecipes(joined,1,listener,errorListener);
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
            // getrecipeinformation with id to grab sourceurl
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
        Log.d("full list", recipes.toString());
        return recipes;
    }
}