package com.example.intific.iradsensorlog.sensorlog;

import android.hardware.Sensor;

/**
 * Created by John Nelson on 7/10/18.
 */

public class SensorLogEntry {

    public Float linearAccelerometerX;
    public Float linearAccelerometerY;
    public Float linearAccelerometerZ;
    public Float accelerometerX;
    public Float accelerometerY;
    public Float accelerometerZ;
    public Float gyroscopeX;
    public Float gyroscopeY;
    public Float gyroscopeZ;
    public Float gameRotationX;
    public Float gameRotationY;
    public Float gameRotationZ;
    
    public SensorLogEntry(){}

    public SensorLogEntry(SensorLogEntry sensorLogEntry){
        this.linearAccelerometerX = sensorLogEntry.linearAccelerometerX;
        this.linearAccelerometerY = sensorLogEntry.linearAccelerometerY;
        this.linearAccelerometerZ = sensorLogEntry.linearAccelerometerZ;
        this.accelerometerX = sensorLogEntry.accelerometerX;
        this.accelerometerY = sensorLogEntry.accelerometerY;
        this.accelerometerZ = sensorLogEntry.accelerometerZ;
        this.gyroscopeX = sensorLogEntry.gyroscopeX;
        this.gyroscopeY = sensorLogEntry.gyroscopeY;
        this.gyroscopeZ = sensorLogEntry.gyroscopeZ;
        this.gameRotationX = sensorLogEntry.gameRotationX;
        this.gameRotationY = sensorLogEntry.gameRotationY;
        this.gameRotationZ = sensorLogEntry.gameRotationZ;
    }
}
