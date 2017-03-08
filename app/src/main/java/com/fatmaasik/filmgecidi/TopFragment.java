package com.fatmaasik.filmgecidi;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class TopFragment extends Fragment {

    Activity titleChange;

    ArrayList<Movie> moviesList;
    MovieViewAdapter adapter;
    View v;


    public TopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_top, container, false);
        // Inflate the layout for this fragment
        moviesList = new ArrayList<Movie>();
        new JSONAsyncTask().execute("http://fatmaasik.hol.es/api/v1/top_movies");
        titleChange.setTitle("En İyi Filmler");
        return v;
    }

    // Activity metodunu fragment içinde kullanmak içi attach işlemi yapıyoruz
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        titleChange = (MainActivity) context;
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
            try {
                JSONParser jsonParser = new JSONParser();
                moviesList = jsonParser.makeHttpRequest(urls[0]);

                return true;
                //------------------>>
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.cancel();
            ListView listView = (ListView) v.findViewById(R.id.topList);
            adapter = new MovieViewAdapter(getActivity(), R.layout.listview_top_movie, moviesList);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long id) {
                    // TODO Auto-generated method stub
                    ((MainActivity) getActivity()).displaySelectedScreen(4, moviesList.get(position).getId());

                }
            });
        }
    }


}
