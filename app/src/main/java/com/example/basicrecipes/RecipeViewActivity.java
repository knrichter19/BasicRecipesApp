package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeViewActivity extends AppCompatActivity {

    private Button linkButton;
    private TextView recipeHeader;
    private TextView recipeDisplay;
    private String recipeId;
    private VolleySingleton requests;
    private String originalUrl;
    private String apiKey;
    private String ingredientString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        linkButton = findViewById(R.id.linkButton);
        recipeHeader = findViewById(R.id.recipeHeader);
        recipeDisplay = findViewById(R.id.recipeInfo);

        Intent i = getIntent();
        recipeId = i.getStringExtra("id");
        recipeHeader.setText(i.getStringExtra("name"));
        ingredientString = i.getStringExtra("ingredients");
        // todo: figure out how to get apiKey

        requests = VolleySingleton.getInstance();
        originalUrl = null;
        getInstructionList();
        getRecipeUrl();
        // how to wait for results of volley query?
        //originalUrl = requests.getRecipeUrl(recipeId);
        // unpack intent
        // display things well
    }

    private void getInstructionList(){
        // sends api request to volley to display steps of the recipe
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<String> instructions = new ArrayList<>();
                try {
                    JSONArray steps = response.getJSONObject(0).getJSONArray("steps");
                    for (int i = 0; i < steps.length(); i++) {
                        try {
                            JSONObject step = steps.getJSONObject(i);
                            Log.d("response step", step.toString());
                            String number = step.getString("number");
                            String stepString = step.getString("step");
                            instructions.add("Step " + number + ":\n-" + stepString.replace(".",".\n-"));
                            // todo: fix extra - at end of each step
                            Log.d("after adding step:", instructions.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

                String displayString = getString(R.string.recipe_display_string, ingredientString, instructions.size() > 0? TextUtils.join("\n",instructions) : "Instructions could not be extracted from original site");
                recipeDisplay.setText(displayString);
                // display here
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // display here
            }
        };
        requests.requestRecipeInstructions(recipeId,listener,errorListener);
    }

    private void getRecipeUrl(){
        // sends API request through volley to get the original recipe url - maybe refactor to get additional information later?
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String url;
                try {
                    url = response.getString("sourceUrl");
                    originalUrl = url;
                    Log.d("url grabbed", url);
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
        requests.requestRecipeInfo(recipeId,false,listener,errorListener);
    }

    public void goToOriginal(View v){
        Log.d("redirect:", originalUrl);
        // todo: surround in try/catch
        Uri uri = Uri.parse(originalUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        // figure out how to go to original site
    }
}