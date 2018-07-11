package com.example.intific.iradsensorlog.sensorlog;

import java.util.ArrayList;

/**
 * Created by John Nelson on 6/18/18.
 */

public class SensorLogInstance {

    public ArrayList<SensorLogEntry> sensorLogEntries;

    public SensorLogInstance(){
        // Initialize all sensor reading array lists
        this.sensorLogEntries = new ArrayList<SensorLogEntry>();
    }

    public void addSensorLogEntry(SensorLogEntry sensorLogEntry){
        sensorLogEntries.add(sensorLogEntry);
    }
}
