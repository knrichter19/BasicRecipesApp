package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private String apiKey;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView t = findViewById(R.id.recipeResults);

        queue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/recipes/findByIngredients";

        this.apiKey = "7f9718e13529466691df6206e109c755";
//        String testIngredients = "chicken,bacon,lettuce";
//        url = url + String.format("?apiKey=%s&ingredients=%s", apiKey, testIngredients);
//
//        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                t.setText(response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                t.setText(error.getMessage());
//            }
//        });
//
//        queue.add(request);
    }

    public void searchByIngredients(View v){
        String endPoint = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + this.apiKey;
        TextView t = (TextView) findViewById(R.id.ingredientInput);
        String ingredients = t.getText().toString();
        CharSequence joined = TextUtils.replace(ingredients,new String[]{"\n"},new CharSequence[]{","});
        endPoint = endPoint + "&ingredients=" + joined;

        TextView results = (TextView) findViewById(R.id.recipeResults);

        JsonArrayRequest request = new JsonArrayRequest(endPoint, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                StringBuilder shittyResults = new StringBuilder(); //todo: distinct objects instead of big string
                //results.setText(response.toString());
                HashMap<String, ArrayList<String>> recipes = null;
                try {
                    recipes = parseRecipeResults(response);
                } catch (JSONException e) {
                    results.setText(e.getMessage());
                }
                if (recipes != null && !recipes.isEmpty()){
                    for (String key : recipes.keySet()){
                        shittyResults.append(key).append(":\n");
                        for (String ingredient : recipes.get(key)){
                            shittyResults.append("\t").append(ingredient).append("\n");
                        }
                    }
                }
                results.setText(shittyResults.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                results.setText(error.getMessage());
            }
        });

        queue.add(request);
    }

    private HashMap<String, ArrayList<String>> parseRecipeResults(JSONArray response) throws JSONException {
        HashMap<String, ArrayList<String>> recipes = new HashMap<>();
        // parse response and print out nicely
        for (int i = 0; i < response.length(); i++){
            ArrayList<String> recipeInfo = new ArrayList<>();
            JSONObject recipe = response.getJSONObject(i);
            String title = recipe.getString("title");
            JSONArray usedIngredients = recipe.getJSONArray("usedIngredients");
            JSONArray missingIngredients = recipe.getJSONArray("missedIngredients");

            for (int j = 0; j < usedIngredients.length(); j++){
                JSONObject ingredient = usedIngredients.getJSONObject(j);
                String name = ingredient.getString("name");
                String amount = ingredient.getString("amount");
                String unit = ingredient.getString("unit");
                recipeInfo.add(amount + " " + unit + " " + name);
            }
            for (int j = 0; j < missingIngredients.length(); j++){
                JSONObject ingredient = missingIngredients.getJSONObject(j);
                String name = ingredient.getString("name");
                String amount = ingredient.getString("amount"); //todo: can just getString?
                String unit = ingredient.getString("unit");
                recipeInfo.add(amount + " " + unit + " " + name);
            }
            recipes.put(title,recipeInfo);
            Log.d("title", title);
            Log.d("used_ingredients", usedIngredients.toString());
            Log.d("missing_ingredients", missingIngredients.toString());
        }
        Log.d("full hashmap", recipes.toString());
        return recipes;
    }
}