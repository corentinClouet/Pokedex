package com.example.android.pokedex;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class PokemonLoader extends AsyncTaskLoader<List<Pokemon>> {

    private static final String LOG_TAG = PokemonLoader.class.getName(); //tag for log messages
    private String mUrl; //query base url
    private final int NB_POKEMON = 9;

    /**
     * Constructs a new {@link PokemonLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public PokemonLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Pokemon> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        //Create a list to store all pokemons
        ArrayList<Pokemon> pokemons = new ArrayList<>();

        // Perform the network request, parse the response, and extract a list of pokemons.
        for (int i = 1; i <= NB_POKEMON; i++){
            Pokemon pokemon = QueryUtils.fetchPokemonData(mUrl + i); //adapt base URL to loop on all pokemons
            pokemons.add(pokemon);
        }

        return pokemons;
    }
}
