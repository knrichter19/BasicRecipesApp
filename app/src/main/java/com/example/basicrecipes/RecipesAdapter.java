package com.example.basicrecipes;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>{

    private HashMap<String, ArrayList<String>> recipes;

    public RecipesAdapter(HashMap<String, ArrayList<String>> recipes){
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // context = activity this is running in?
        LayoutInflater inflater = LayoutInflater.from(context);
        // instantiate inflater with reference to context
        View recipeView = inflater.inflate(R.layout.item_recipe,parent,false);
        // create and inflate a view, giving it the context and the layout to use
        return new ViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView ingredients;
        public Button expandButton;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recipe_name);
            ingredients = (TextView) itemView.findViewById(R.id.ingredients_needed);
            expandButton = (Button) itemView.findViewById(R.id.expand_button);
        }
    }
}
