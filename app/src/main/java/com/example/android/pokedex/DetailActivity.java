package com.example.android.pokedex;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getName();
    private TextView mIdTextView;
    private TextView mNameTextView;
    private TextView mWeightTextView;
    private TextView mHeightTextView;
    private ImageView mSpriteImageView;
    Pokemon pokemon;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //display up action button
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //get the object pass in extra
        pokemon = getIntent().getExtras().getParcelable("pokemon");

        //init views controls
        mIdTextView = findViewById(R.id.detail_id_text_view);
        mNameTextView = findViewById(R.id.detail_name_text_view);
        mWeightTextView = findViewById(R.id.detail_weight_text_view);
        mHeightTextView = findViewById(R.id.detail_heigh_text_view);
        mSpriteImageView = findViewById(R.id.detail_sprite_image_view);

        //init text values
        mIdTextView.setText("#" + pokemon.getId());
        mNameTextView.setText(pokemon.getName());
        mWeightTextView.setText("Weight : " + pokemon.getWeight());
        mHeightTextView.setText("Height : " + pokemon.getHeight());

        //load sprite image
        Picasso.with(this)
                .load(pokemon.getSprite())
                .error(R.drawable.pokeball)
                .into(mSpriteImageView);

        //create images views for each type of the pokemon
        LinearLayout detailTypeLinearLayout = findViewById(R.id.detail_type_linear_layout);
        for (String s : pokemon.getTypes()) {
            int id = findType(s);
            if (id != 0) {
                ImageView image = new ImageView(DetailActivity.this);
                image.setImageResource(id);
                LayoutParams layoutParams = new LayoutParams(250,100);
                image.setLayoutParams(layoutParams);
                detailTypeLinearLayout.addView(image);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_share:
                sharePokemon();
                return true;
            default:
                finish();
                return true;
        }
    }

    //User can select in which app he wants to share the information
    private void sharePokemon() {
        // populate the share intent with data
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.base_text_share) + pokemon.getName());
        startActivity(Intent.createChooser(intent, getString(R.string.chooser_text_share)));
    }

    private int findType(String pType){
        switch (pType){
            case "flying" :
                return R.drawable.ftype_flying;
            case "bug" :
                return R.drawable.type_bug;
            case "dark" :
                return R.drawable.type_dark;
            case "dragon" :
                return R.drawable.type_dragon;
            case "electrik" :
                return R.drawable.type_electrik;
            case "fairy" :
                return R.drawable.type_fairy;
            case "fight" :
                return R.drawable.type_fight;
            case "fire" :
                return R.drawable.type_fire;
            case "ghost" :
                return R.drawable.type_ghost;
            case "grass" :
                return R.drawable.type_grass;
            case "ground" :
                return R.drawable.type_ground;
            case "ice" :
                return R.drawable.type_ice;
            case "poison" :
                return R.drawable.type_poison;
            case "psychic" :
                return R.drawable.type_psychic;
            case "rock" :
                return R.drawable.type_rock;
            case "steel" :
                return R.drawable.type_steel;
            case "water" :
                return R.drawable.type_water;
            case "normal" :
                return R.drawable.type_normal;
            default:
                return 0;
        }
    }
}
