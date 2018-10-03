package com.example.chthp00108.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by chthp00108 on 28/9/18.
 */

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] songname;
    private final String[] urls;

    CustomList(Activity context, String[] songname, String[] urls) {
        super(context,R.layout.row_item, urls);
        this.context = context;
        this.songname = songname;
        this.urls = urls;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.row_item, null, true);
        TextView txtSong = (TextView) rowView.findViewById(R.id.textsongName);
        //TextView txturl = (TextView) rowView.findViewById(R.id.texturl);
        txtSong.setText(songname[position]);
        //txturl.setText(urls[position]);
        return rowView;
    }

}
