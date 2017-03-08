package com.fatmaasik.filmgecidi;


import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MovieDetail extends Fragment {

    Activity titleChange;
    ArrayList<Movie> moviesList;
    VideoView videoView;
    MovieViewAdapter adapter;
    MediaController video;

    public MovieDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        // Inflate the layout for this fragment

        String value = getArguments().getString("movie_id");

        moviesList = new ArrayList<Movie>();
        new JSONAsyncTask().execute("http://fatmaasik.hol.es/api/v1/movies/" + value);

        return v;
    }

    // Activity metodunu fragment içinde kullanmak içi attach işlemi yapıyoruz
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        titleChange = (MainActivity) activity;
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Yüklenirken lütfen bekleyin");
            dialog.setTitle("Sunucuya bağlanıyor");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            InputStream stream;
            StringBuffer buffer;
            try {
                URL url = new URL(urls[0]); //bağlantı
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream)); //okuma yapıyor
                buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String data = buffer.toString();
                JSONObject jsono = new JSONObject(data);
                JSONArray jarray = jsono.getJSONArray("movies");
                for (int i = 0; i < jarray.length(); i++){
                    JSONObject object = jarray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setTitle(object.getString("title_1"));
                    movie.setId(object.getString("movie_id"));
                    movie.setImage(object.getString("image_url"));
                    movie.setVideo(object.getString("video"));
                    movie.setRating(object.getString("rating"));
                    movie.setRuntime(object.getString("runtime"));
                    movie.setYear(object.getString("year"));
                    movie.setCountry(object.getString("country"));
                    movie.setGenre(object.getString("genre"));
                    movie.setActor(object.getString("actor"));
                    movie.setDirector(object.getString("director"));
                    movie.setSynopsis(object.getString("synopsis"));

                    moviesList.add(movie);
                }

                return true;
                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.cancel();

            titleChange.setTitle(moviesList.get(0).getTitle());

            TextView movieName = (TextView) getView().findViewById(R.id.movie_title);
            movieName.setText(moviesList.get(0).getTitle());

            TextView movieProductionYear = (TextView) getView().findViewById(R.id.production_year);
            movieProductionYear.append(moviesList.get(0).getYear());

            TextView movieRating = (TextView) getView().findViewById(R.id.rating);
            movieRating.setText(moviesList.get(0).getRating()+"/10");

            TextView movieCountry = (TextView) getView().findViewById(R.id.country);
            movieCountry.append(moviesList.get(0).getCountry());

            TextView movieRuntime = (TextView) getView().findViewById(R.id.runtime);
            movieRuntime.append(moviesList.get(0).getRuntime() + " dk");


            TextView movieGenre = (TextView) getView().findViewById(R.id.genre);
            movieGenre.append(moviesList.get(0).getGenre());

            TextView movieActor = (TextView) getView().findViewById(R.id.actor);
            movieActor.append(moviesList.get(0).getActor());

            TextView movieDirector = (TextView) getView().findViewById(R.id.director);
            movieDirector.append(moviesList.get(0).getDirector());

            TextView movieSynopsis = (TextView) getView().findViewById(R.id.synopsis);
            movieSynopsis.append(moviesList.get(0).getSynopsis());

            ImageView movieImage = (ImageView) getView().findViewById(R.id.movie_image);
            movieImage.setImageResource(R.drawable.background_mondorf);
            Picasso.with(getContext()).load(moviesList.get(0).getImage()).into(movieImage);

            videoView =(VideoView)getView().findViewById(R.id.movie_video);
            MediaController mediaController= new MediaController(getActivity());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoPath(moviesList.get(0).getVideo());
            videoView.requestFocus();
            //videoView.start();
        }
    }



}
