package com.example.chthp00108.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chthp00108 on 28/9/18.
 */

public class CustomList extends ArrayAdapter<SongObject> {
    Context cxt;
    int res;
    ArrayList<SongObject> list;

    public CustomList(Context context, int resource, ArrayList<SongObject> objects) {
        super(context, resource, objects);
        cxt=context;
        res=resource;
        list=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;

        //Initializing view which will point to layout file list_item
        view= LayoutInflater.from(cxt).inflate(res,parent,false);

        //Initializing TextView
        TextView fileName=(TextView)view.findViewById(R.id.textsongName);

        SongObject sdOb=list.get(position);
        //Setting the Icon and FileName
        fileName.setText(sdOb.getFileName());

        return view;
    }
}
