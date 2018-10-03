package com.example.chthp00108.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {
     ListView listview;
    TextView url,songName;
    String[] sName ={"song1","song2","song3","song4","song5"};
   // String[] urls={"https://www.ssaurel.com/tmp/mymusic.mp3","https://www.android-examples.com/wp-content/uploads/2016/04/Thunder-rumble.mp3","http://www.robtowns.com/music/blind_willie.mp3","http://www.songblasts.com/songs/hindi/t/three-idiots/01-Aal_Izz_Well-(SongsBlasts.Com).mp3","/Music/song.mp3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listview = (ListView) findViewById(R.id.listview);

       /* url= (TextView) findViewById(R.id.texturl);
        songName= (TextView) findViewById(R.id.textsongName);*/

       // CustomList listAdapter = new CustomList(this, sName, urls);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.row_item,R.id.textsongName,sName);
        listview.setAdapter(itemsAdapter);
       listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


              Intent intent =new Intent(ListActivity.this,MainActivity.class);
               intent.putExtra("position",position);
               intent.putExtra("songname",sName[position]);

               startActivity(intent);

           }
       });

    }
}
