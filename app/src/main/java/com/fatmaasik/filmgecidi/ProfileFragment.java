package com.fatmaasik.filmgecidi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class ProfileFragment extends Fragment {

    Activity titleChange;
    SessionManager session;
    TabLayout tabLayout;
    ViewPager viewPager;
    String userId;
    ArrayList<Movie> moviesList;
    MovieViewAdapter adapter;
    View v;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        TextView lblName = (TextView) v.findViewById(R.id.profileName);
        TextView lblEmail = (TextView) v.findViewById(R.id.profileEmail);
        ImageView profileImage = (ImageView) v.findViewById(R.id.profileImage);
        lblName.setText(user.get(SessionManager.KEY_NAME));
        lblEmail.setText(user.get(SessionManager.KEY_EMAIL));
        profileImage.setImageResource(R.mipmap.ic_profile);
        System.out.println("http://fatmaasik.hol.es/public/static/photos/"+user.get(SessionManager.KEY_IMAGE));
        Picasso.with(getContext()).load("http://fatmaasik.hol.es/public/static/photos/"+user.get(SessionManager.KEY_IMAGE)).into(profileImage);
        userId = user.get(SessionManager.KEY_ID);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0);
        tabLayout.getTabAt(1);
        titleChange.setTitle("Profil");
        moviesList = new ArrayList<Movie>();
        new JSONAsyncTask().execute("http://fatmaasik.hol.es/api/v1/user/"+userId+"/watched");
        return v;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        titleChange = (MainActivity) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new WatchedFragment(),"İzlediklerim");
        adapter.addFragment(new CommentsFragment(), "Yorumlarım");
        viewPager.setAdapter(adapter);
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

            GridView gridView = (GridView) v.findViewById(R.id.watched_movies);
            adapter = new MovieViewAdapter(getActivity(), R.layout.gridview_movie, moviesList);

            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
