package com.example.intific.iradsensorlog.sensorlog;

/**
 * Created by John Nelson on 6/18/18.
 */

public class SensorLoggerConfig {
    Boolean logAccelerometer;
    Boolean logGyroscope;
    Boolean logOrientation;
    Boolean logGravity;
    Boolean logActivity;

    public SensorLoggerConfig(boolean logAll){
        this.logAccelerometer = this.logGyroscope = this.logOrientation = this.logGravity = this.logActivity = logAll;
    }
}
