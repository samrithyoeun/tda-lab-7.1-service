package com.fluffy.samrith.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;

/**
 * Created by samrith on 8/12/17.
 */

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //we want this service to continue running until it is explicitly
        //stopped ,so return sticky

        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        try{
            new DoBackgroundTask().execute(
                    new URL("https://goo.gl/2asYYt"));

        }catch (Exception e){e.printStackTrace();}

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private int DownloadFile(URL urls){
        try{
            //--simulate taking some time to download a file --
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }

        //--return an arbitrary number representing the size of the file download --
        return 100;
    }

    public class DoBackgroundTask extends AsyncTask<URL,Integer,Long> {

        @Override
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalByteDownloaeded =0;
            for(int  i=0;i<count;i++){
                totalByteDownloaeded = DownloadFile(urls[i]);
                //-- calcualte percentage downloaded and report its progress
                publishProgress((int)(((i+1)/(float)count)*100));

            }
            return totalByteDownloaeded;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("downloading files",String.valueOf(values[0])+"% downloaded");
            Toast.makeText(getApplicationContext(), String.valueOf(values[0]) +"% downloaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Long values) {
            Toast.makeText(getApplicationContext(), String.valueOf(values) +"byetes downloaded", Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }
}
