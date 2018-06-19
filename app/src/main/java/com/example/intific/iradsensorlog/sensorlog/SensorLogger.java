package com.example.intific.iradsensorlog.sensorlog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by John Nelson on 6/18/18.
 */

public class SensorLogger implements SensorEventListener {
    // Sensor API related objects
    private SensorManager mSensorManager;
    private Sensor linearAccelerometer;
    private Sensor rawAccelerometer;
    private Sensor rawGyroscope;
    private Sensor gameRotationSensor;// The game rotation is more accurate than geomagnetic, and we don't care about "north"

    // Logging tools
    private SensorLoggerConfig sensorLoggerParameters;
    private SensorLogInstance sensorLogInstance;

    public SensorLogger(SensorLoggerConfig sensorLoggerParameters, Context context){
        this.sensorLoggerParameters = sensorLoggerParameters;

        // Initialize sensor API related objects
        this.mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.linearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        this.rawAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.rawGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gameRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        // Initialize the instance of sensor logs
        this.sensorLogInstance = new SensorLogInstance();
    }

    //TODO Start
    public void startLogger(){
        //TODO Just log all interesting sensors we can get a hold of for now
    }

    //TODO Stop
    //TODO Maybe include an "onSensorLogStopped"
    public void stopLogger(){
        //TODO Export to CSV and reinitialize data

        //TODO Log frequency domain if selected... Assume this will take some time...
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Handle all the sensors we're interested in
        switch (sensorEvent.sensor.getType()){
            case Sensor.TYPE_LINEAR_ACCELERATION:
                float linearAccelerationX, linearAccelerationY, linearAccelerationZ = 0;
                linearAccelerationX = sensorEvent.values[0];
                linearAccelerationY= sensorEvent.values[1];
                linearAccelerationZ = sensorEvent.values[2];
                break;
            case Sensor.TYPE_ACCELEROMETER:
                float accelerometerX, accelerometerY, accelerometerZ = 0;
                accelerometerX = sensorEvent.values[0];
                accelerometerY= sensorEvent.values[1];
                accelerometerZ = sensorEvent.values[2];
                break;
            case Sensor.TYPE_GYROSCOPE:
                float gyroscopeX, gyroscopeY, gyroscopeZ = 0;
                gyroscopeX = sensorEvent.values[0];
                gyroscopeY= sensorEvent.values[1];
                gyroscopeZ = sensorEvent.values[2];
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                float rotationX, rotationY, rotationZ = 0;
                rotationX = sensorEvent.values[0];
                rotationY= sensorEvent.values[1];
                rotationZ = sensorEvent.values[2];
                break;
            default:
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //TODO Handle (?)
    }

}
