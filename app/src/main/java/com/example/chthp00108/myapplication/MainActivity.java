package com.example.chthp00108.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    ImageButton rewind, forward, playpause, stop;
    TextView musicplayer, textCurrentPosition, textMaxTime, songName;
    SeekBar seekbar;
    Button btback;
    LinearLayout layout2;
    MediaPlayer mediaPlayer;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();;
    PlayListActivity playlistactivity;
    //int[] songs = {R.raw.song, R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4};
    private int startTime = 0;
    private int finalTime = 0;
    private int forwardTime = 15000;
    ArrayList<SongObject> listOfContents  =new ArrayList<>();
  //  SongsManager songmanager;
    private int backwardTime = 15000;
    private Handler threadHandler = new Handler();
    //private AudioManager audioManager =new AudioManager();

    //String path= Environment.getExternalStorageDirectory().getAbsolutePath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  MyPersonalApp myPersonalApp=new MyPersonalApp();
        //myPersonalApp.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("play","we are in Manin Activity");
        if (Build.VERSION.SDK_INT >= 23)
            checkPermission();
        else
            initViews();
    }


        void checkPermission(){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                //if permission granted, initialize the views
                initViews();
            }else{
                //show the dialog requesting to grant permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            switch (requestCode){
                case 1:
                    if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        initViews();
                    }else{
                        //permission is denied (this is the first time, when "never ask again" is not checked)
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                            finish();
                        }
                        //permission is denied (and never ask again is  checked)
                        else {
                            //shows the dialog describing the importance of permission, so that user should grant
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("You have forcefully denied Read storage permission.\n\nThis is necessary for the working of app."+"\n\n"+"Click on 'Grant' to grant permission")
                                    //This will open app information where user can manually grant requested permission
                                    .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    //close the app
                                    .setNegativeButton("Don't", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });
                            builder.setCancelable(false);
                            builder.create().show();
                        }
                    }
            }
        }


    void initViews() {
        Log.d("locate","entering in initviews");
        rewind = (ImageButton) findViewById(R.id.btnPrevious);
        forward = (ImageButton) findViewById(R.id.btnForward);
        playpause = (ImageButton) findViewById(R.id.btnPlay);
        stop = (ImageButton) findViewById(R.id.btnStop);
        musicplayer = (TextView) findViewById(R.id.textview);
        textCurrentPosition = (TextView) findViewById(R.id.textView1);
        textMaxTime = (TextView) findViewById(R.id.textView3);
        songName = (TextView) findViewById(R.id.textView2);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        btback = (Button) findViewById(R.id.btback);
        seekbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekbar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        mediaPlayer = new MediaPlayer();
          playpause.setImageResource(R.mipmap.ic_launcher);

          Intent intent = getIntent();




            //final int position = intent.getIntExtra("position",0);

            String path = intent.getStringExtra("path");
            String song = intent.getStringExtra("songName");
            songName.setText(song);

        // String filePath = Environment.getExternalStorageDirectory()+"/storage/emulated/0/song.mp3";

        /// final String url="https://archive.org/details/Mp3Songs_175/khelein-hum-jee-jaan-sey01www.songs.pk.mp3";
        seekbar.setEnabled(false);
        stop.setEnabled(false);

        // If Android Marshmello or above, then check if permission is granted
        //songmanager.initList(path);
       // mediaPlayer = MediaPlayer.create(this, R.raw.song);
        //   mediaPlayer = MediaPlayer.create(this,Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/storage/emulated/0/song.mp3"));

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
     try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("show","Error: "+e.toString());
        }


        playpause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("show","in playPause");
                seekbar.setEnabled(true);
                //mediaPlayer=new MediaPlayer();
                stop.setEnabled(true);
                try {

                    if (mediaPlayer.isPlaying()) {
                        if (mediaPlayer != null) {
                            mediaPlayer.pause();
                            Log.i("pause", "song is pausing");
                            playpause.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            //Toast.makeText(MainActivity.this, "medaiplayer is not set", Toast.LENGTH_SHORT).show();
                            Log.i("error", "mediaplayer is not set");
                        }
                    } else {
                        if (mediaPlayer != null) {


                            mediaPlayer.start();
                            Log.i("MainActivity", "song is playing");
                            playpause.setImageResource(R.mipmap.pause);

                            finalTime = mediaPlayer.getDuration();
                            Log.i("song duration",Integer.toString(finalTime));
                            startTime = mediaPlayer.getCurrentPosition();

                            if (startTime == 0) {
                                seekbar.setMax(finalTime);
                                String maxTimeString = millisecondsToString(finalTime);
                                textMaxTime.setText(maxTimeString);
                            } else if (startTime == finalTime) {
                                // Resets the MediaPlayer to its uninitialized state.
                                mediaPlayer.reset();
                                playpause.setImageResource(R.mipmap.ic_launcher);
                            }

                        } else {
                            //Toast.makeText(MainActivity.this, "medaiplayer is not set", Toast.LENGTH_SHORT).show();
                            Log.i("error", "mediaplayer is not set");
                        }
                    }
                    UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
                    threadHandler.postDelayed(updateSeekBarThread, 20);

                }
                catch(NullPointerException e){
                    e.printStackTrace();
                    Log.e("error",e.toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                    Log.e("error",e.toString());
                }
            }
        });


        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            mediaPlayer.pause();
                mediaPlayer.seekTo(0);
               playpause.setImageResource(R.mipmap.ic_launcher);
                Intent intent = new Intent(MainActivity.this, PlayListActivity.class);


                startActivity(intent);
            }
        });
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int temp = startTime;
                if ((temp - backwardTime) > 0) {

                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo(startTime);
                    Log.i("backward","You have Jumped backward 15 seconds");
                    Toast.makeText(getApplicationContext(), "You have Jumped backward 15 seconds", Toast.LENGTH_SHORT).show();

                } else {
                    Log.i("backward","cannot Jumped forward 15 seconds");
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 15 seconds", Toast.LENGTH_SHORT).show();
                }
            }

        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                if (fromUser) {
                   /* int temp=  startTime;
                    startTime=startTime+progress;*/
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int temp = startTime;
                if ((temp + forwardTime) <= finalTime) {


                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo(startTime);
                    Log.i("forward","You have Jumped forward 15 seconds");
                    Toast.makeText(getApplicationContext(), "You have Jumped forward 15 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("forward","cannot Jumped forward 15 seconds");
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 15 seconds", Toast.LENGTH_SHORT).show();
                }


            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                playpause.setImageResource(R.mipmap.ic_launcher);


            }


        });

    }


    @Override
    public void onBackPressed() {
        if (mediaPlayer!= null) {

            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            playpause.setImageResource(R.mipmap.ic_launcher);
            //this.finish();
            super.onBackPressed();
        }
    }

    private String millisecondsToString(int milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) milliseconds);
        return minutes + ":" + seconds;
    }


    class UpdateSeekBarThread implements Runnable {

        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            String currentPositionStr = millisecondsToString(startTime);
            textCurrentPosition.setText(currentPositionStr);

          seekbar.setProgress(startTime);
            if((startTime == finalTime)){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);

                playpause.setImageResource(R.mipmap.ic_launcher);
            }
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 20);
        }
    }
}