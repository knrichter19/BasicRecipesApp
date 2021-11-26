package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

public class RecipeViewActivity extends AppCompatActivity {

    private Button linkButton;
    private TextView recipeHeader;
    private TextView recipeDisplay;
    private String recipeId;
    private VolleySingleton requests;
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

        // unpack intent
        // display things well
    }

    private void getInstructions(){
    }
}