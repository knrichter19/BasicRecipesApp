package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = findViewById(R.id.realSampleText);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/recipes/findByIngredients";

        String apiKey = "7f9718e13529466691df6206e109c755";
        String testIngredients = "chicken,bacon,lettuce";
        url = url + String.format("?apiKey=%s&ingredients=%s", apiKey, testIngredients);

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                t.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                t.setText(error.getMessage());
            }
        });

        queue.add(request);




    }
}