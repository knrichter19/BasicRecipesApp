package com.example.basicrecipes;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
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

    private ArrayList<ArrayList<String>> recipes;

    public RecipesAdapter(ArrayList<ArrayList<String>> recipes){
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
        ArrayList<String> recipe = recipes.get(position);
        holder.name.setText(recipe.get(0));
        // todo: functional buttons
        holder.ingredients.setText(TextUtils.join(", ",recipe.subList(1,recipe.size()-1)));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
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
