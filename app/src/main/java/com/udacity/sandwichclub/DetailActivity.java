package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mTitleTextView;
    private TextView mAlsoKnownAsTextView;
    private TextView mOriginTextView;
    private TextView mDescriptionTextView;
    private TextView mIngredientsTextView;

    private TextView mAlsoKnownAsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mTitleTextView = findViewById(R.id.title_tv);
        mAlsoKnownAsTextView = findViewById(R.id.also_known_tv);
        mOriginTextView = findViewById(R.id.origin_tv);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);

        mAlsoKnownAsLabel = findViewById(R.id.also_known_as_label);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        mTitleTextView.setText(sandwich.getMainName());
        mDescriptionTextView.setText(sandwich.getDescription());

        // Display place of origin. If not found, display an error message.
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            mOriginTextView.setText(R.string.place_of_origin_error);
            mOriginTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            mOriginTextView.setText(sandwich.getPlaceOfOrigin());
        }

        List<String> alsoKnownAs = sandwich.getAlsoKnownAs();
        if (alsoKnownAs.isEmpty()) {
            // No other names found. Make the views invisible.
            mAlsoKnownAsLabel.setVisibility(View.INVISIBLE);
            mAlsoKnownAsTextView.setVisibility(View.INVISIBLE);
        } else {
            // Bring back the "also known as" fields in case they were set to GONE before
            mAlsoKnownAsLabel.setVisibility(View.VISIBLE);
            mAlsoKnownAsTextView.setVisibility(View.VISIBLE);

            for (int index = 0; index < alsoKnownAs.size(); index++) {
                mAlsoKnownAsTextView.append(alsoKnownAs.get(index));

                // Add a comma for the "also known as" data, except for the last one
                if (index != (alsoKnownAs.size() - 1)) {
                    mAlsoKnownAsTextView.append(", ");
                }
            }
        }

        List<String> ingredients = sandwich.getIngredients();
        if (ingredients.isEmpty()) {
            // Let the user know that no ingredients were found for this sandwich
            mIngredientsTextView.setText(R.string.ingredients_error);
            mIngredientsTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            for (int index = 0; index < ingredients.size(); index++) {
                mIngredientsTextView.append(ingredients.get(index) + "\n");
            }
        }
    }
}
