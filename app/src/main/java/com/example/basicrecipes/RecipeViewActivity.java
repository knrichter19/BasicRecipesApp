package com.example.basicrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

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
        ArrayList<String> instructions = requests.getInstructionList(recipeId);
        // how to wait for results of volley query?
        originalUrl = requests.getRecipeUrl(recipeId);
        // unpack intent
        // display things well
    }

    public void goToOriginal(View v){
        // figure out how to go to original site
    }
}