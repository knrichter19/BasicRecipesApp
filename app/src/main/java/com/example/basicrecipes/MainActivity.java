package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private String apiKey;
    private RequestQueue queue;
    private VolleySingleton requests;
    private RecyclerView recipeView;
    private TextView apiKeyBox;
    private SharedPreferences sharedPreferences;
    private int numRecipes = 20;
    private int ranking = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView t = findViewById(R.id.recipeResults);
        recipeView = findViewById(R.id.rvRecipes);
        apiKeyBox = findViewById(R.id.apiKeyBox);

        // set up API connection
        requests = VolleySingleton.getInstance(this);

        queue = Volley.newRequestQueue(this);

        // check for stored apiKey
        this.sharedPreferences = getPreferences(MODE_PRIVATE);
        this.apiKey = sharedPreferences.getString("apiKey", null);
        if (!(apiKey == null)){
            apiKeyBox.setText(apiKey);
        }
    }

    public void searchByIngredients(View v){
        this.apiKey = apiKeyBox.getText().toString();
        // todo: check for valid api key
        if (apiKey.length() > 0) {
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
                        Log.d("error", e.getMessage());
                        Toast.makeText(MainActivity.this, "Error interpreting results", Toast.LENGTH_LONG).show();
                        // how to interpret api key error vs internet error?
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
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    String message = "Error retrieving results";
                    if (statusCode.equals("401")){
                        // not authorized
                        message = "You are not authorized. Please enter a valid Spoonacular key";
                    }
                    else if (statusCode.equals("402")){
                        // reached quota
                        message = "You have reached your daily quota for the Spoonacular API";
                    }
                    // convert to switch statement + add more checks for other error codes?

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                }
            };

            requests.setApiKey(this.apiKey);
            requests.requestRecipes(joined, ranking, numRecipes, listener, errorListener);
        }
        else{
            Toast.makeText(MainActivity.this, "Please enter an API key to retrieve your recipes!", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Recipe> parseRecipeResults(JSONArray response) throws JSONException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(3);
        // parse response and print out nicely
        for (int i = 0; i < response.length(); i++){
            Recipe newRecipe = new Recipe();
            ArrayList<String[]> ingredientArray = new ArrayList<>();
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
                amount = df.format(Float.valueOf(amount));
                String unit = ingredient.getString("unit");
                ingredientArray.add(new String[]{amount, unit, name});
            }
            for (int j = 0; j < missingIngredients.length(); j++){
                JSONObject ingredient = missingIngredients.getJSONObject(j);
                String name = ingredient.getString("name");
                String amount = ingredient.getString("amount");
                amount = df.format(Float.valueOf(amount));
                String unit = ingredient.getString("unit");
                ingredientArray.add(new String[]{amount, unit, name});
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

    public void storeKey(View v){
        // get key from textbox
        // store
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("apiKey", apiKeyBox.getText().toString());
        editor.apply();
    }
}