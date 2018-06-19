package com.example.intific.iradsensorlog.sensorlog;

import java.util.ArrayList;

/**
 * Created by John Nelson on 6/18/18.
 */

public class SensorLogInstance {

    ArrayList<Float> linearAccelerometerX;
    ArrayList<Float> linearAccelerometerY;
    ArrayList<Float> linearAccelerometerZ;
    ArrayList<Float> accelerometerX;
    ArrayList<Float> accelerometerY;
    ArrayList<Float> accelerometerZ;
    ArrayList<Float> gyroscopeX;
    ArrayList<Float> gyroscopeY;
    ArrayList<Float> gyroscopeZ;
    ArrayList<Float> gameRotationX;
    ArrayList<Float> gameRotationY;
    ArrayList<Float> gameRotationZ;

    //TODO Set activity value

    public SensorLogInstance(){
        // Initialize all sensor reading array lists
        this.linearAccelerometerX = this.linearAccelerometerY = this.linearAccelerometerZ = this.accelerometerX = this.accelerometerY = this.accelerometerZ =
        this.gyroscopeX = this.gyroscopeY = this.gyroscopeZ = this.gameRotationX = this.gameRotationY = this.gameRotationZ  = new ArrayList<Float>();
    }
}
