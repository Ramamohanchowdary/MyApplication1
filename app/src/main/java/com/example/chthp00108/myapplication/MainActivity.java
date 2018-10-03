package com.example.chthp00108.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    ImageButton rewind, forward, playpause, stop;
    TextView musicplayer, textCurrentPosition, textMaxTime, songName;
    SeekBar seekbar;
    Button btback;
    LinearLayout layout2;
    MediaPlayer mediaPlayer;
    int[] songs={R.raw.song,R.raw.song1,R.raw.song2,R.raw.song3,R.raw.song4};
    private int startTime = 0;
    private int finalTime = 0;
    private int forwardTime = 15000;
    private int backwardTime = 15000;
    private Handler threadHandler = new Handler();
    //private AudioManager audioManager =new AudioManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rewind = (ImageButton) findViewById(R.id.btnPrevious);
        forward = (ImageButton) findViewById(R.id.btnForward);
        playpause = (ImageButton) findViewById(R.id.btnPlay);
        stop = (ImageButton) findViewById(R.id.btnStop);
        musicplayer = (TextView) findViewById(R.id.textview);
        textCurrentPosition = (TextView) findViewById(R.id.textView1);
        textMaxTime = (TextView) findViewById(R.id.textView3);
        songName = (TextView) findViewById(R.id.textView2);
        layout2= (LinearLayout) findViewById(R.id.layout2);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        btback = (Button) findViewById(R.id.btback);
        seekbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekbar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

           Intent intent = getIntent();

            final int position = intent.getIntExtra("position",0);
            String songname = intent.getStringExtra("songname");
            songName.setText(songname);

       // String filePath = Environment.getExternalStorageDirectory()+"/Home/Music/song.mp3";

        /// final String url="https://archive.org/details/Mp3Songs_175/khelein-hum-jee-jaan-sey01www.songs.pk.mp3";
        seekbar.setEnabled(false);
        mediaPlayer=new MediaPlayer();
       mediaPlayer = MediaPlayer.create(this, songs[position]);
        //mediaPlayer = MediaPlayer.create(this,Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Home/Music/song.mp3"));
       // mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
     /*   try {
            mediaPlayer.setDataSource(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setEnabled(true);
                //mediaPlayer=new MediaPlayer();



                  if(mediaPlayer.isPlaying()){
                      if(mediaPlayer!=null){
                          mediaPlayer.pause();

                          playpause.setImageResource(R.mipmap.ic_launcher);
                      }
                      else{
                          Toast.makeText(MainActivity.this, "medaiplayer is not set", Toast.LENGTH_SHORT).show();
                      }
                  }
                  else{
                      if(mediaPlayer!=null){


                          mediaPlayer.start();
                          playpause.setImageResource(R.mipmap.pause);

                          finalTime = mediaPlayer.getDuration();
                          startTime = mediaPlayer.getCurrentPosition();

                          if (startTime == 0) {
                              seekbar.setMax(finalTime);
                              String maxTimeString = millisecondsToString(finalTime);
                              textMaxTime.setText(maxTimeString);
                          } else if (startTime == finalTime) {
                              // Resets the MediaPlayer to its uninitialized state.
                              mediaPlayer.reset();
                          }

                      }
                      else{
                          Toast.makeText(MainActivity.this, "medaiplayer is not set", Toast.LENGTH_SHORT).show();
                      }
                  }
                UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
                threadHandler.postDelayed(updateSeekBarThread, 50);

            }
        });



        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.reset();
                Intent intent=new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int temp = startTime;
                if((temp-backwardTime)>0) {

                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo( startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 15 seconds",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Cannot jump backward 15 seconds",Toast.LENGTH_SHORT).show();
                }
                }

        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                if(fromUser){
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


                int temp=  startTime;
                if((temp+forwardTime)<=finalTime){


                    startTime=startTime +forwardTime;
                     mediaPlayer.seekTo(startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 15 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 15 seconds",Toast.LENGTH_SHORT).show();
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
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }
    }
}