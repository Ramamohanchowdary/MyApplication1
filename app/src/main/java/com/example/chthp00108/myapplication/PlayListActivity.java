package com.example.chthp00108.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayListActivity extends AppCompatActivity {
    //public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    ArrayList<SongObject> listOfContents  =new ArrayList<>();

    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    ListView listView;
    String absolutePath,songName;
    MediaPlayer mediaPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
       /* MyPersonalApp mypersonalapp=new MyPersonalApp();
          mypersonalapp.onCreate();*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Log.d("created","Activity is creadted");
       // ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

        //SongsManager plm = new SongsManager();
        // get all songs from sdcard
        //this.songsList = plm.initList();

        // looping through playlist
      /*  for (int i = 0; i < listOfContents.size(); i++) {
            // creating new HashMap
            HashMap<String, String> song = listOfContents.get(i);

            // adding HashList to ArrayList
            songsListData.add(song);
        }*/
        initList(path);
        listView = (ListView) findViewById(R.id.listview);
        CustomList adapter=new CustomList(this,R.layout.row_item,listOfContents);
        listView.setAdapter(adapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               SongObject sdOb = listOfContents.get(position);
               absolutePath = sdOb.getAbsolutePath();
               Intent start = new Intent(PlayListActivity.this, MainActivity.class);
               songName = listOfContents.get(position).getFileName();
               start.putExtra("path",absolutePath);
               start.putExtra("songName",songName);
               startActivity(start);
           }
       });
    }
    void initList(String path){
        try {
            File file = new File(path);
            File[] filesArray=file.listFiles();
            String fileName;
            for(File file1:filesArray) {
                if (file1.isDirectory()) {
                    initList(file1.getAbsolutePath());
                } else {
                    fileName=file1.getName();
                    if ((fileName.endsWith(".mp3")) || (fileName.endsWith(".MP3"))){


                        listOfContents.add(new SongObject(file1.getName(), file1.getAbsolutePath()));
                        Log.i("url",file1.getAbsolutePath());
                    }

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("error",e.toString());
        }
    }



    }

