package com.funnycat.popularmovies.connectivity;

import android.net.Uri;
import android.util.Log;

import com.funnycat.popularmovies.BuildConfig;
import com.funnycat.popularmovies.domain.commands.Command;
import com.funnycat.popularmovies.domain.models.Trailer;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 18/01/2017.
 */

public class Trailers_Request implements Command<List<Trailer>> {

    private static String TAG = "Trailers_Request";

    private static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static String END_URL = "videos";

    private String mUrl;

    public Trailers_Request(int movieId){
        mUrl = buildUrl(movieId);
    }

    private String buildUrl(int movieId){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendPath(END_URL)
                .appendQueryParameter("api_key", BuildConfig.MOVIES_API_KEY)
                .build();
        return uri.toString();
    }

    @Override
    public List<Trailer> execute() {
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
                List<Trailer> resultList = new ArrayList<>();
                for(int i=0; i<resultArray.length(); i++){
                    resultList.add(gson.fromJson(resultArray.getString(i), Trailer.class));
                }
                loadCover(resultList);
                return resultList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private void loadCover(List<Trailer> trailers){
        OkHttpClient client = new OkHttpClient();
        for(Trailer trailer : trailers){
            Request request = new Request.Builder()
                    .url("https://www.googleapis.com/youtube/v3/videos?id="+ trailer.getKey()
                    + "&key=" + BuildConfig.YOUTUBE_API_KEY + "%20&part=snippet")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    JSONObject json = new JSONObject(response.body().string());
                    if(json.has("items")){
                        JSONArray jsonItems = new JSONArray(json.getString("items"));
                        JSONObject jsonSnippet = new JSONObject(new JSONObject(jsonItems.get(0).toString()).getString("snippet"));
                        JSONObject jsonThumbnails = new JSONObject(jsonSnippet.getString("thumbnails"));
                        JSONObject jsonDefaultI = new JSONObject(jsonThumbnails.getString("medium"));
                        trailer.setCover(jsonDefaultI.getString("url"));
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

