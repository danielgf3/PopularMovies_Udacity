package com.funnycat.popularmovies.connectivity;

import android.net.Uri;
import android.util.Log;

import com.funnycat.popularmovies.BuildConfig;
import com.funnycat.popularmovies.domain.commands.Command;
import com.funnycat.popularmovies.domain.models.Movie;
import com.funnycat.popularmovies.domain.models.MovieListType;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 18/01/2017.
 */

public class Movies_Request implements Command<List<Movie>> {

    private static String TAG = "Movies_Request";

    private static String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private String mUrl;

    public Movies_Request(MovieListType typeRequest){
        this(typeRequest, 0);
    }

    public Movies_Request(MovieListType typeRequest, int page){
        mUrl = buildUrl(typeRequest);
        Log.d(TAG, "URL: " + mUrl);
    }

    private String buildUrl(MovieListType typeRequest){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(typeRequest.path)
                .appendQueryParameter("api_key", BuildConfig.MOVIES_API_KEY)
                .build();
        return uri.toString();
    }

    @Override
    public List<Movie> execute() {
        Request request = new Request.Builder()
                .url(mUrl)
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String body = response.body().string();
                JSONObject jsonBody = new JSONObject(body);
                JSONArray resultArray = new JSONArray(jsonBody.getString("results"));
                Gson gson = new Gson();
                List<Movie> resultList = new ArrayList<>();
                for(int i=0; i<resultArray.length(); i++){
                    resultList.add(gson.fromJson(resultArray.getString(i), Movie.class));
                }
                return resultList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}

