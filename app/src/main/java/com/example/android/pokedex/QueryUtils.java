package com.example.android.pokedex;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving pokemon data from POKEAPI.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName(); //tag for log messages

    /**
     * Query the POKEAPI dataset and return an {@link Pokemon} object to represent a single pokemon.
     */
    public static Pokemon fetchPokemonData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);
        Log.d(LOG_TAG, "Création url OK");

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        Pokemon pokemon = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return pokemon;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.d(LOG_TAG, "Requete envoyée");

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                Log.d(LOG_TAG, "Code reponse 200");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.d(LOG_TAG, "Json reponse OK");
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the pokemon JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Pokemon} object by parsing out information
     * about the first earthquake from the input pokemonJSON string.
     */
    private static Pokemon extractFeatureFromJson(String pokemonJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(pokemonJSON)) {
            return null;
        }

        Log.d(LOG_TAG, "debut parsing");

        // Create an empty Pokemon that we can start adding data to
        Pokemon pokemon = new Pokemon();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        try {
            // build up a Pokemon object with the corresponding data.
            JSONObject reader = new JSONObject(pokemonJSON);

            pokemon.setId(reader.getInt("id"));
            pokemon.setName(reader.getString("name"));
            pokemon.setWeight(reader.getInt("weight"));
            pokemon.setHeight(reader.getInt("height"));

            Log.d(LOG_TAG, "Id en cours : " + pokemon.getId());

            JSONObject sprites = reader.getJSONObject("sprites");
            pokemon.setSprite(sprites.getString("front_default"));

            JSONArray types = reader.getJSONArray("types");
            ArrayList<String> tmpTypes = new ArrayList<>();

            for (int i = 0; i < types.length(); i++){
                JSONObject globalType = types.getJSONObject(i);
                JSONObject detailType = globalType.getJSONObject("type");
                tmpTypes.add(detailType.getString("name"));
            }
            pokemon.setTypes(tmpTypes);
            Log.d(LOG_TAG, "Parsing ok");

            return pokemon;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }
}