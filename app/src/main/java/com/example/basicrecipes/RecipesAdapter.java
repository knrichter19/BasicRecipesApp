package com.example.basicrecipes;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>{
    // Adapter for RecyclerView

    private ArrayList<Recipe> recipes; // List of recipes from search results
    private Context context; // MainActivity context

    public RecipesAdapter(ArrayList<Recipe> recipes){
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recipeView = inflater.inflate(R.layout.item_recipe,parent,false);
        return new ViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // fills viewholder variables for each recipe in list
        Recipe recipe = recipes.get(position);
        holder.setRecipe(recipe); // reference to recipe object
//        holder.nameBox.setText(recipe.getName()); // fill textBox
//        holder.ingredientsBox.setText(TextUtils.join(", ",recipe.getIngredients()));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameBox;
        public TextView ingredientsBox;
        public Button expandButton;
        public Recipe recipe;

        public ViewHolder(View itemView){
            super(itemView);

            // grab items in each row's layout
            nameBox = (TextView) itemView.findViewById(R.id.recipe_name);
            ingredientsBox = (TextView) itemView.findViewById(R.id.ingredients_needed);
            expandButton = (Button) itemView.findViewById(R.id.expand_button);

            // set button listener (better way to do this?)
            expandButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // opens new activity with more detailed recipe instructions
                    if (recipe != null) { // todo: better bug fixing
                        // gets context from adapter
                        Intent intent = new Intent(context, RecipeViewActivity.class);
                        // sends over recipe name + id
                        intent.putExtra("name", recipe.getName());
                        intent.putExtra("id", recipe.getId());
                        context.startActivity(intent);
                    }
                    Log.d("ButtonClick", "clicked expand button");
                }
            });
        }

        public void setRecipe(Recipe recipe) {
            // reference to recipe object
            this.recipe = recipe;
            // sets text for name + ingredients
            nameBox.setText(recipe.getName());
            ingredientsBox.setText(TextUtils.join(", ", recipe.getIngredients()));
        }
    }
}
