package com.example.chthp00108.myapplication;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by chthp00108 on 9/10/18.
 */

public class MyPersonalApp extends Application {



    public void onCreate() {

        super.onCreate();

        if (isExternalStorageWritable()) {
            File appDirectory = new File(Environment.getExternalStorageDirectory() + "/MyPersonalAppFolder");

            File logDirectory = new File(appDirectory + "/log");

            File logFile = new File(logDirectory, "logcat" + System.currentTimeMillis()+ ".txt");


            if (!appDirectory.exists()) {

                appDirectory.mkdir();

            }


            // create log folder

            if (!logDirectory.exists()) {

                logDirectory.mkdir();

            }
            try {

                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -f " + logFile);
                //process = Runtime.getRuntime().exec( "logcat -f " + logFile + " *:S MyActivity:D MyActivity2:D");

            } catch (IOException e) {

                e.printStackTrace();
                Log.e("error",e.toString());

            }
        } else if (isExternalStorageReadable()) {

            // only readable

        } else {

            // not accessible

        }
    }

        public boolean isExternalStorageWritable() {

            String state = Environment.getExternalStorageState();

            if ( Environment.MEDIA_MOUNTED.equals( state ) ) {

                return true;

            }

            return false;



    }
    public boolean isExternalStorageReadable() {

        String state = Environment.getExternalStorageState();

        if ( Environment.MEDIA_MOUNTED.equals( state ) ||

                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {

            return true;

        }

        return false;

    }

}
