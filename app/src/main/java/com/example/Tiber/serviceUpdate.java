package com.example.Tiber;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.Tiber.ui.home.HomeFragment;

public class serviceUpdate extends Service {

    HomeFragment homeFragment = new HomeFragment();

    public serviceUpdate() {
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        while (true){

                            //calling method to transmit data to the server
                            TransmitDadaToServer();

                            Log.e("Service", "Service is running." );



                            try{

                                Thread.sleep(10000);
                            }
                            catch (InterruptedException e){

                                e.printStackTrace();
                            }
                        }

                    }
                }
        ).start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void TransmitDadaToServer() {

        //calling method to transmit data to the server
        //homeFragment.TransmitDataToServer();
        /*
        SomeAsyncOperation.init(new Callback() {
            @Override
            public void onSuccess(HomeFragment initializedInstance) {
                homeFragment = initializedInstance;
                // Now it's safe to access remoteClassInstance
            }

            @Override
            public void onFailure() {
                // Handle failure
            }
        });
        */

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}