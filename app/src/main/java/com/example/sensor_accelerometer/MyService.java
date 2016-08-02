package com.example.sensor_accelerometer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.SensorEvent;

import java.util.Random;


public class MyService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();
    final static String MY_ACTION = "MY_ACTION";


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */

    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }




    //TextView textX, textY, textZ, text1;
    //CheckBox c1;
    float x,y,z;
    SensorManager sensorManager;
    Sensor sensor;
    float currentx=0,currenty=0,currentz=0;
    int n=0,x1,x2,x3;


    public MyService() {

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(accelListener, sensor,SensorManager.SENSOR_DELAY_UI);

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        sensorManager.unregisterListener(accelListener);
    }


    SensorEventListener accelListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {

        }

        public void onSensorChanged(SensorEvent event) {



            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            x1=(int)x;x2=(int)y;x3=(int)z;
            Intent intent;
            intent = new Intent();

            intent.setAction(MY_ACTION);
            intent.putExtra("D1", x1);
            intent.putExtra("D2", x2);
            //intent.putExtra("D3", x3);
            intent.putExtra("D3",n);
            sendBroadcast(intent);
            if((currentx!=0&&currentx!=x1)||(currenty!=0&&currenty!=x2)||(currentz!=0&&currentz!=x3))
            {
                n=n+1;
                //text1.setText(String.valueOf(n));

                if(n>=100){
                    restartservice();
                }
            }

            //textX.setText("X : " + (int)x);
            //textY.setText("Y : " + (int)y);
            //textZ.setText("Z : " + (int)z);
            currentx = (int)x; currenty=(int)y; currentz=(int)z;

        }
    };

    void restartservice(){
        n=0;

        //Toast.makeText(this, "Limit Reached...!!!", Toast.LENGTH_LONG).show();
        //if (c1.isChecked()){
        //sendSMSMessage();
        //}
        // Get instance of Vibrator from current Context

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 1000 milliseconds
        v.vibrate(400);

    }

    // SMS alert code

    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        String phoneNo = "99999999"; //phone number here
        String message = "Alert.....!!!";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}