package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        requests = VolleySingleton.getInstance();
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
                            instructions.add("Step " + number + ":\n\t" + stepString);
                            Log.d("after adding step:", instructions.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
                recipeDisplay.setText(instructions.toString());
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
        // figure out how to go to original site
    }
}