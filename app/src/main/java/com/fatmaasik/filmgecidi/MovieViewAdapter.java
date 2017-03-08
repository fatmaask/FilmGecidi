package com.fatmaasik.filmgecidi;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieViewAdapter extends ArrayAdapter<Movie> {
    ArrayList<Movie> movieList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder=null;

    public MovieViewAdapter(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Resource = resource;
        movieList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);

            holder.title = (TextView) v.findViewById(R.id.title);
            holder.image = (ImageView) v.findViewById(R.id.item_image);
            holder.id = (TextView) v.findViewById(R.id.listId);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.title.setText(movieList.get(position).getTitle());
        holder.image.setImageResource(R.drawable.background_mondorf);
        Picasso.with(getContext()).load(movieList.get(position).getImage()).into(holder.image);

        if (holder.id != null){
            int pos = position+1;
            holder.id.setText(String.valueOf(pos) + ".");
        }

        return v;

    }



    static class ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView id;


    }

}