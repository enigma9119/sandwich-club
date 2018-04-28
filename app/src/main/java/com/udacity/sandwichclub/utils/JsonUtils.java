package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();

        try {
            JSONObject sandwichData = new JSONObject(json);
            JSONObject name = sandwichData.getJSONObject("name");

            sandwich.setMainName(name.getString("mainName"));
            sandwich.setPlaceOfOrigin(sandwichData.getString("placeOfOrigin"));
            sandwich.setDescription(sandwichData.getString("description"));
            sandwich.setImage(sandwichData.getString("image"));

            JSONArray alsoKnownAs = name.getJSONArray("alsoKnownAs");
            sandwich.setAlsoKnownAs(parseJsonArray(alsoKnownAs));

            JSONArray ingredients = sandwichData.getJSONArray("ingredients");
            sandwich.setIngredients(parseJsonArray(ingredients));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }

    private static List<String> parseJsonArray(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }
}
