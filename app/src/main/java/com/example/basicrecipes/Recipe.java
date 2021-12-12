package com.example.basicrecipes;

import android.text.TextUtils;

import java.util.ArrayList;

public class Recipe {
    public String id;
    public String name;
    public ArrayList<String[]> ingredients;
    public String url;

    public Recipe(){

    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String[]> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getFormattedIngredients(){
        // ingredients are returned formatted as "{amount} {units} {name}"
        ArrayList<String> formatted = new ArrayList<>();
        for (String[] ingredient : ingredients){
            formatted.add(TextUtils.join(" ", ingredient));
        }
        return formatted;
    }

    public ArrayList<String> getIngredientNames(){
        // only the name of each ingredient is returned, no quantities
        ArrayList<String> names = new ArrayList<>();
        for (String[] ingredient : ingredients){
            names.add(ingredient[2]);
        }
        return names;
    }

    public void setIngredients(ArrayList<String[]> ingredients) {
        this.ingredients = ingredients;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
