package com.example.basicrecipes;

import android.content.Context;
import android.util.Log;
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

public class VolleySingleton {
    private static VolleySingleton instance = null;
    public RequestQueue requestQueue;
    private String apiKey;

    private VolleySingleton(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    public static synchronized VolleySingleton getInstance(Context context){
        if (instance == null)
            instance = new VolleySingleton(context);
        return instance;
    }
    public static synchronized VolleySingleton getInstance(){
        if (instance==null){
            throw new IllegalStateException("Network manager not initialized, " +
                    "please provide context as a parameter");
        }
        return instance;
    }

    public void setApiKey(String key) {
        apiKey = key;
    }

    public String getApiKey(){
        return this.apiKey;
    }

    public void requestRecipes(String ingredients, int ranking,
                                    Response.Listener<JSONArray> listener,
                                    Response.ErrorListener errorListener){
        // ingredients: String separated by commas (for now)

        String url = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + this.apiKey
                + "&ranking=" + ranking
                + "&ingredients=" + ingredients;

        JsonArrayRequest request = new JsonArrayRequest(url,listener, errorListener);
        requestQueue.add(request);
    }

    public void requestRecipeInfo(String recipeId, boolean includeNutrition,
                                  Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errorListener){
        String url = "https://api.spoonacular.com/recipes/"+recipeId+"/information?apiKey=" + this.apiKey
                + "&includeNutrition=" + String.valueOf(includeNutrition);
        JsonObjectRequest request = new JsonObjectRequest(url, listener, errorListener);
        requestQueue.add(request);
    }

    public void requestRecipeInstructions(String recipeId, Response.Listener<JSONArray> listener,
                                          Response.ErrorListener errorListener){
        String url = "https://api.spoonacular.com/recipes/"+recipeId+"/analyzedInstructions?apiKey=" + apiKey;
        JsonArrayRequest request = new JsonArrayRequest(url,listener,errorListener);
        requestQueue.add(request);
    }

    public String getRecipeUrl(String recipeId){
        final String[] returnUrl = new String[1]; //find better way to do this
        returnUrl[0] = "";
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    returnUrl[0] = response.getString("sourceUrl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
        requestRecipeInfo(recipeId,false,listener,errorListener);
        return returnUrl[0];
    }
}
