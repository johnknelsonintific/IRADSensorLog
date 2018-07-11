package com.example.intific.iradsensorlog.sensorlog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by John Nelson on 6/18/18.
 */

public class SensorLogger implements SensorEventListener {
    // Context
    Context context;

    // Sensor API related objects
    private SensorManager mSensorManager;
    private Sensor linearAccelerometer;
    private Sensor rawAccelerometer;
    private Sensor rawGyroscope;
    private Sensor gameRotationSensor;// The game rotation is more accurate than geomagnetic, and we don't care about "north"

    // Logging tools
    private SensorLoggerConfig sensorLoggerParameters;
    private SensorLogInstance sensorLogInstance;
    private SensorLogEntry currentSensorLogEntry;

    // Timer for appending new sensor data to logs
    Timer logEntryTimer;

    // Interface for handling logging events from caller
    private SensorLogListenerInterface sensorLogListener;

    public SensorLogger(SensorLoggerConfig sensorLoggerParameters, Context context, SensorLogListenerInterface sensorLogListener){
        this.context = context;

        // Set parameters for logging
        this.sensorLoggerParameters = sensorLoggerParameters;

        // Set listener
        this.sensorLogListener = sensorLogListener;

        // Initialize sensor API related objects
        this.mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.linearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        this.rawAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.rawGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.gameRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        // Initialize the instance of sensor logs
        this.sensorLogInstance = new SensorLogInstance();
        this.currentSensorLogEntry = new SensorLogEntry();
        logEntryTimer = new Timer();
    }

    public void startLogger(){
        this.sensorLogListener = sensorLogListener;

        // Register for all interesting sensors we can get a hold of
        mSensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, rawAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, rawGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, gameRotationSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Instantiate timer that will periodically add our latest log entry to the log entry list
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sensorLogInstance.addSensorLogEntry(currentSensorLogEntry);
            }
        };
        logEntryTimer.scheduleAtFixedRate(task, 0, 100);

        // Notify caller that we have begun
        sensorLogListener.sensorLoggingStarted();
    }

    //TODO Stop
    //TODO Maybe include an "onSensorLogStopped"
    public void stopLogger(){
        // Unregister from sensor events
        mSensorManager.unregisterListener(this);

        // Stop the timer
        logEntryTimer.cancel();

        //TODO Export to CSV and reinitialize data

        //TODO Log frequency domain if selected... Assume this will take some time...

        this.sensorLogListener.sensorLoggingEnded();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Handle all the sensors we're interested in
        switch (sensorEvent.sensor.getType()){
            case Sensor.TYPE_LINEAR_ACCELERATION:
                currentSensorLogEntry.linearAccelerometerX = sensorEvent.values[0];
                currentSensorLogEntry.linearAccelerometerY= sensorEvent.values[1];
                currentSensorLogEntry.linearAccelerometerZ = sensorEvent.values[2];
                break;
            case Sensor.TYPE_ACCELEROMETER:
                currentSensorLogEntry.accelerometerX = sensorEvent.values[0];
                currentSensorLogEntry.accelerometerY= sensorEvent.values[1];
                currentSensorLogEntry.accelerometerZ = sensorEvent.values[2];
                break;
            case Sensor.TYPE_GYROSCOPE:
                currentSensorLogEntry.gyroscopeX = sensorEvent.values[0];
                currentSensorLogEntry.gyroscopeY= sensorEvent.values[1];
                currentSensorLogEntry.gyroscopeZ = sensorEvent.values[2];
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                currentSensorLogEntry.gameRotationX = sensorEvent.values[0];
                currentSensorLogEntry.gameRotationY= sensorEvent.values[1];
                currentSensorLogEntry.gameRotationZ = sensorEvent.values[2];
                break;
            default:
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //TODO Handle (?)
    }

    public interface SensorLogListenerInterface {
        void sensorLoggingStarted();
        void sensorLoggingEnded();
    }

}
