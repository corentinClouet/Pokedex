package com.example.android.pokedex;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class PokemonAdapter extends ArrayAdapter<Pokemon> {

    public PokemonAdapter(Context context, List<Pokemon> pokemons) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, pokemons);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the object located at this position in the list
        Pokemon currentPokemon = getItem(position);

        // Find the TextView in the list_item.xml layout with the name of the Pokemon
        ImageView thumbnailImageView = (ImageView) listItemView.findViewById(R.id.thumbnail_image_view);
        Picasso.with(getContext())
                .load(currentPokemon.getSprite())
                .error(R.drawable.pokeball)
                .into(thumbnailImageView);

        // Find the TextView in the list_item.xml layout with the name of the Pokemon
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentPokemon.getName());

        // Find the TextView in the list_item.xml layout with the id of the Pokemon
        TextView idTextView = (TextView) listItemView.findViewById(R.id.id_text_view);
        idTextView.setText("#" + currentPokemon.getId());

        // Return the whole list item layout (containing 3 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
