package com.example.ptechv001;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private SensorManager mSensorManager; // attributs for sensors
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mMagnetometer;
    private SensorEventListener sensorEventListener;

    private float[] gravity= new float[3];
    private float[] linear_acceleration=new float[3];
    private float[] gyro=new float[3];
    private float[] magne=new float[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetometer=mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener( this, mAccelerometer, 50000000);
        mSensorManager.registerListener( this, mGyroscope, 50000000);
        mSensorManager.registerListener( this, mMagnetometer, 50000000);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    /** Called when the user taps the Send button */
    public void sendMessage(View view) {

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);// Do something in response to button

    }

    public void onSensorChanged(SensorEvent event){
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                // Capture the layout's TextView and set the string as its text
                final float alpha = (float) 0.8;

                // Isolate the force of gravity with the low-pass filter.
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                // Remove the gravity contribution with the high-pass filter.
                linear_acceleration[0] = event.values[0]- gravity[0];
                linear_acceleration[1] = event.values[1]- gravity[1];
                linear_acceleration[2] = event.values[2]- gravity[2];
                break;

            case Sensor.TYPE_GYROSCOPE:
                gyro[0] = event.values[0];
                gyro[1] = event.values[1];
                gyro[2] = event.values[2];
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magne[0] = event.values[0];
                magne[1] = event.values[1];
                magne[2] = event.values[2];
                break;
        }
        TextView x_grav_textview = findViewById(R.id.X_grav_text);
        x_grav_textview.setText(String.format("%.02f", linear_acceleration[0]));

        TextView y_grav_textview = findViewById(R.id.Y_grav_text);
        y_grav_textview.setText(String.format("%.02f", linear_acceleration[1]));

        TextView z_grav_textview = findViewById(R.id.Z_grav_text);
        z_grav_textview.setText(String.format("%.02f", linear_acceleration[2]));

        TextView x_gyro = findViewById(R.id.TextViewGyroX);
        x_gyro.setText(String.format("%.02f", gyro[0]));

        TextView y_gyro = findViewById(R.id.TextViewGyroY);
        y_gyro.setText(String.format("%.02f", gyro[1]));

        TextView z_gyro = findViewById(R.id.TextViewGyroZ);
        z_gyro.setText(String.format("%.02f", gyro[2]));

        TextView x_mag = findViewById(R.id.textViewMagX);
        x_mag.setText(String.format("%.02f", magne[0]));

        TextView y_mag = findViewById(R.id.textViewMagY);
        y_mag.setText(String.format("%.02f", magne[1]));

        TextView z_mag = findViewById(R.id.textViewMagZ);
        z_mag.setText(String.format("%.02f", magne[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}