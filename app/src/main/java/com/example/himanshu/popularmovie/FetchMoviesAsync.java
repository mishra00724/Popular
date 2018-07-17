package com.example.himanshu.popularmovie;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchMoviesAsync extends AsyncTask<String,Void,String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        if(strings.length==0){
            return null;
        }

        String sortingCriteria=strings[0];

        Uri builtUri=Uri.parse(Constants.ApiConstants.BASE_MOVIE_URL).buildUpon()
                        .appendPath(sortingCriteria)
                        .appendQueryParameter(Constants.ApiConstants.API_KEY_PARAM,Constants.ApiConstants.MOVIE_API_KEY)
                        .build();

        String response;

        try{
            response=getJson(builtUri);
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public static String getJson(Uri builtUri){
        String JsonStr=null;

        try{
            String movieStr=builtUri.toString();
            HttpHandler sh=new HttpHandler();

            JsonStr=sh.makeServiceCall(movieStr);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return JsonStr;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null){
            loadInfo(s);
        }
    }

    public static void loadInfo(String JsonStr){
        if(MainActivity.list!=null){
        MainActivity.list.clear();}
        if(MainActivity.images!=null){
        MainActivity.images.clear();}


        try {
            if(JsonStr!=null){
                JSONObject moviesObject=new JSONObject(JsonStr);
                JSONArray moviesResults=moviesObject.getJSONArray("results");

                for(int i=0;i<moviesResults.length();i++){
                    JSONObject movieJsonObject=moviesResults.getJSONObject(i);
                    Movies movies=new Movies();
                    movies.setId(movieJsonObject.getLong("id"));

                    if(movieJsonObject.getString("poster_path").equals("null")){
                        MainActivity.images.add("");
                        movies.setPoster_path("");
                    }
                    else{
                        MainActivity.images.add(Constants.ApiConstants.IMAGE_BASE_URL+ Constants.ApiConstants.IMAGE_SMALL_SIZE+movieJsonObject.getString("poster_path"));
                    }

                    movies.setOriginal_title(movieJsonObject.getString("original_title"));
                    movies.setOverview(movieJsonObject.getString("overview"));
                    movies.setRelease_date(movieJsonObject.getString("release_date"));
                    movies.setVote_average(movieJsonObject.getDouble("vote_average"));
                    MainActivity.list.add(movies);
                    MainActivity.moviesAdapter.notifyDataSetChanged();

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }




    }






}
