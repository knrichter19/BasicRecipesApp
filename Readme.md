This android app project uses the Spoonacular API to retrieve recipes based on a list of ingredients supplied. You will need a Spoonacular API key to use this app (see https://spoonacular.com/food-api/docs#Authentication).

The app will look like this when first launched:

![app1](https://user-images.githubusercontent.com/70418659/145733954-58d7f39f-a5f8-49a9-b5c5-89e99e17f64d.png)

You will need to input your api key in the upper right box - click "save key" after it is entered to have the key autofill the next time you open this app

![app2](https://user-images.githubusercontent.com/70418659/145733994-25b335f9-0d92-4f52-a97a-ca0970fae8d4.png)

Type in the ingredients you want in your recipe in the first box, with one ingredient per line:

![app3](https://user-images.githubusercontent.com/70418659/145734042-c1e9a71a-0ac6-4276-afa6-7670b45ce566.png)

Click "Search Recipes" to generate recipes using the given ingredients (search and ranking is performed by the Spoonacular API - ranking currently set to maximize ingredients used)

![app4](https://user-images.githubusercontent.com/70418659/145734082-5a5dff3f-b24d-4373-a657-ba0bb603281a.png)

Click "Expand" on a recipe you want more information about to view the recipe image, ingredient amounts, and instructions interpreted by the API

![app5](https://user-images.githubusercontent.com/70418659/145734109-8bb31bd6-ac02-49f0-8e94-bc2375a5cf2f.png)

To get more detailed and accurate recipe information, click "Go To Site" to visit the original url of the recipe.

![app6](https://user-images.githubusercontent.com/70418659/145734129-a1aecdc7-c582-471a-b4f3-6857c0593419.png)



Todo list:
- Fix visual issues
  - Stop text cutting off in recyclerview
  - Fix recyclerview falling off screen

Possible todo list:
- Better api key saving/autofilling
- Toggle prioritizing missing or owned ingredients
- General refactoring
- List of ingredients to avoid
- Save recipes option
- Move recyclerview initialization somewhere better
- More results in recipe search - adjustable? Pages?
