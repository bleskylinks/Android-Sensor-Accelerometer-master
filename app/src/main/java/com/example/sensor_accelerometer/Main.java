package com.example.sensor_accelerometer;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	TextView textX, textY, textZ, text1;
	CheckBox c1;
	float x,y,z;
	SensorManager sensorManager;
	Sensor sensor;
	float currentx=0,currenty=0,currentz=0;
	int n=0,x1,x2,x3;
	MyReceiver myReceiver;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
 
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		textX = (TextView) findViewById(R.id.textX);
		textY = (TextView) findViewById(R.id.textY);
		textZ = (TextView) findViewById(R.id.textZ);
		text1 = (TextView) findViewById(R.id.t1);
		c1 = (CheckBox) findViewById(R.id.checkBox);
	}
 
	public void onResume() {
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MyService.MY_ACTION);
		registerReceiver(myReceiver, intentFilter);
		super.onResume();
		//sensorManager.registerListener(accelListener, sensor,
				//SensorManager.SENSOR_DELAY_UI);
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			// Toast.makeText(Main.this,"Triggered by Service!",Toast.LENGTH_LONG).show();
			int datapassed = arg1.getIntExtra("D1", 0);
			int datapassed1 = arg1.getIntExtra("D2", 0);
			//int datapassed2 = arg1.getIntExtra("D3", 0);
			int n1 = arg1.getIntExtra("D3",0);
			textX = (TextView) findViewById(R.id.textX);
			textY = (TextView) findViewById(R.id.textY);
			textZ = (TextView) findViewById(R.id.textZ);
			text1 = (TextView) findViewById(R.id.t1);
			textX.setText("X:"+String.valueOf(datapassed));
			textY.setText("Y:"+String.valueOf(datapassed1));
			//textZ.setText("Z:"+String.valueOf(datapassed2));
			text1.setText(String.valueOf(n1));

		}

	}
 
	public void onStop() {
		super.onStop();
		//sensorManager.unregisterListener(accelListener);
	}

	SensorEventListener accelListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int acc) {

		}

		public void onSensorChanged(SensorEvent event) {

			x = event.values[0];
			y = event.values[1];
			z = event.values[2];
			x1=(int)x;x2=(int)y;x3=(int)z;
			if((currentx!=0&&currentx!=x1)||(currenty!=0&&currenty!=x2)||(currentz!=0&&currentz!=x3))
			{
				n=n+1;
				//text1.setText(String.valueOf(n));
				if(n>=100){
				restartservice();
				}
			}
			textX.setText("X : " + (int)x);
			textY.setText("Y : " + (int)y);
			textZ.setText("Z : " + (int)z);
			currentx = (int)x; currenty=(int)y; currentz=(int)z;

		}
	};

	void restartservice(){
		n=0;
		if (c1.isChecked()){
			sendSMSMessage();
		}

	}

	protected void sendSMSMessage() {
		Log.i("Send SMS", "");
		//Phone Number here
		String phoneNo = "9999999";
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

	public void startService(View view) {
		startService(new Intent(getBaseContext(), MyService.class));
	}

	// Method to stop the service
	public void stopService(View view) {
		stopService(new Intent(getBaseContext(), MyService.class));

	}
}