package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
                            instructions.add("Step " + number + ":\n" + stepString.replace(".",".\n"));
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
        // sends API request through volley to get the original recipe url and image url
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String url;
                String image;
                try {
                    image = response.getString("image");
                    ImageView iv = findViewById(R.id.recipeImage);
                    ImageAccess ia = new ImageAccess(iv);
                    ia.execute(image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        // button goes to original recipe cite
        Log.d("redirect:", originalUrl);
        // todo: surround in try/catch
        Uri uri = Uri.parse(originalUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // makes back button close current activity
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private class ImageAccess extends AsyncTask<String, Void, Drawable> {
        ImageView iv;

        public ImageAccess (ImageView iv){
            this.iv = iv;
        }

        @Override
        protected Drawable doInBackground(String... strings) {
            String imageUrl = strings[0];
            Drawable d = null;
            try {
                InputStream is = (InputStream) new URL(imageUrl).getContent();
                d = Drawable.createFromStream(is, "srcName");

            } catch (IOException e) { // todo: handle errors
                e.printStackTrace();
            }
            return d;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (!(drawable == null)) {
                iv.setImageDrawable(drawable);
            }
        }
    }
}