package com.example.android.pokedex;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Pokemon>>{

    private static final String POKEAPI_REQUEST_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final String LOG_TAG = MainActivity.class.getName();
    private PokemonAdapter mAdapter; //Adapter for the list of pokemons
    private static final int POKEAPI_LOADER_ID = 1; //constant loader id
    private ImageView mEmptyImageView;
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView pokemonListView = findViewById(R.id.list);

        //init the empty view for the list view
        LinearLayout mEmptyStateTextView = findViewById(R.id.empty_view);
        pokemonListView.setEmptyView(mEmptyStateTextView);

        //init the child views of emptyView
        mEmptyImageView = findViewById(R.id.empty_image_view);
        mEmptyTextView = findViewById(R.id.empty_text_view);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new PokemonAdapter(this, new ArrayList<Pokemon>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        pokemonListView.setAdapter(mAdapter);

        //open the detail activity for the selected pokemon
        pokemonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pokemon currentPokemon = mAdapter.getItem(i);
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("pokemon", currentPokemon);
                startActivity(intent);
            }
        });

        //verify internet connection
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for the bundle.
            loaderManager.initLoader(POKEAPI_LOADER_ID, null, this);
        }
        else
        {
            // Hide loading indicator because there is not internet connection
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Set empty state view to display "No internet connection."
            mEmptyImageView.setImageResource(R.drawable.no_internet_connection);
            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Pokemon>> onCreateLoader(int id, Bundle args) {
        //return a new loader with our pokeapi URL request
        return new PokemonLoader(this, POKEAPI_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Pokemon>> loader, List<Pokemon> pokemons) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state view to display "No Pokemons found." in case of we have no data
        mEmptyImageView.setImageResource(R.drawable.no_data);
        mEmptyTextView.setText(R.string.no_data);

        // Clear the adapter of previous pokemons data
        mAdapter.clear();

        // If there is a valid list of {@link Pokemon}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (pokemons != null && !pokemons.isEmpty()) {
            mAdapter.addAll(pokemons);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Pokemon>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
