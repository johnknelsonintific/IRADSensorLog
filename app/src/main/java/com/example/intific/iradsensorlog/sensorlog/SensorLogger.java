package com.example.intific.iradsensorlog.sensorlog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.intific.iradsensorlog.csv.SensorLogCsvWriter;
import com.example.intific.iradsensorlog.googleactivityrecognition.GoogleActivityRecognition;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by John Nelson on 6/18/18.
 */

public class SensorLogger implements SensorEventListener, GoogleActivityRecognition.GoogleActivityRecognizedListener {
    // Context
    private Context context;

    // Sensor API related objects
    private SensorManager mSensorManager;
    private Sensor linearAccelerometer;
    private Sensor rawAccelerometer;
    private Sensor rawGyroscope;
    private Sensor gameRotationSensor;// The game rotation is more accurate than geomagnetic, and we don't care about "north"

    // Google acgtiviy recognition API class
    private GoogleActivityRecognition googleActivityRecognition;

    // Logging tools
    private SensorLoggerConfig sensorLoggerParameters;
    private SensorLogInstance sensorLogInstance;
    private SensorLogEntry currentSensorLogEntry;

    // Csv writer
    private SensorLogCsvWriter sensorLogCsvWriter;

    // Timer for appending new sensor data to logs
    private Timer logEntryTimer;

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

        // Initialize our Google activity recognition API
        this.googleActivityRecognition = new GoogleActivityRecognition(context, this);

        // Initialize csv writer
        this.sensorLogCsvWriter = new SensorLogCsvWriter(context);

        // Initialize the instance of sensor logs
        this.sensorLogInstance = new SensorLogInstance();
        this.currentSensorLogEntry = new SensorLogEntry();
    }

    public void startLogger(){
        // Reset log instance
        sensorLogInstance = new SensorLogInstance();

        // Register for all interesting sensors we can get a hold of
        mSensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, rawAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, rawGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, gameRotationSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Instantiate timer that will periodically add our latest log entry to the log entry list
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SensorLogEntry sensorLogEntryAddition = new SensorLogEntry(currentSensorLogEntry);
                sensorLogInstance.addSensorLogEntry(sensorLogEntryAddition);
            }
        };
        logEntryTimer = new Timer();
        logEntryTimer.scheduleAtFixedRate(task, 0, 100);

        // Register our instance of GoogleActivityRecognition class to recieve activity updates
        googleActivityRecognition.registerForActivityTransitions();

        // Notify caller that we have begun
        sensorLogListener.sensorLoggingStarted();
    }

    // Stop logging sensor information
    public void stopLogger(){
        // Unregister from sensor events and activity recognition
        mSensorManager.unregisterListener(this);
        googleActivityRecognition.unregisterFromActivityTransitions();

        // Stop the timer
        logEntryTimer.cancel();

        // Export to CSV and reinitialize data
        try {
            sensorLogCsvWriter.exportSensorLogCsv(this.sensorLogInstance);
        } catch (IOException e) {
            //TODO Notify user of an error writing to the logs
            e.printStackTrace();
        }

        //TODO Log frequency domain if selected... Assume this will take some time...

        this.sensorLogListener.sensorLoggingEnded();
    }


    public void setPosture(Integer postureStanding) {

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

    @Override
    public void activityRecognized(Integer activityInteger) {

    }

    public interface SensorLogListenerInterface {
        void sensorLoggingStarted();
        void sensorLoggingEnded();
    }

}
