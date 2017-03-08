package com.fatmaasik.filmgecidi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AboutFragment extends Fragment {

    Activity titleChange;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        titleChange.setTitle("Hakkında");
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        TextView sitem=(TextView) v.findViewById(R.id.about);
        sitem.setMovementMethod(LinkMovementMethod.getInstance());
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



}
