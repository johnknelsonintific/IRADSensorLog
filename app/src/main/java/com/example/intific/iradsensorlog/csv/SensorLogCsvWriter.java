package com.example.intific.iradsensorlog.csv;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.example.intific.iradsensorlog.sensorlog.SensorLogEntry;
import com.example.intific.iradsensorlog.sensorlog.SensorLogInstance;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by John Nelson on 7/11/18.
 */

public class SensorLogCsvWriter {
    // Location where we'll write logs to
    private static final String LOG_FILE_BASE_DIRECTORY = Environment.DIRECTORY_DOCUMENTS + "/IRADSensorLog";
    private static final String LOG_FILE_BASE_NAME = "/IRADSensorLog";

    // Member variables
    Context context;

    public SensorLogCsvWriter(Context context){
        this.context = context;
    }

    public void exportSensorLogCsv(SensorLogInstance sensorLogInstance) throws IOException {
        // If we don't have permissions, exit the method
        if(! haveWritePermissions()){
            return;
        }

        // Set up the file
        Calendar calendar = Calendar.getInstance();
        String formattedDateString = new SimpleDateFormat("MM_dd_hh:mm:ss").format(calendar.getTime());
        File sensorLogFile = new File(Environment.getExternalStoragePublicDirectory(LOG_FILE_BASE_DIRECTORY).toString(), LOG_FILE_BASE_NAME + formattedDateString + ".txt");
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(sensorLogFile.getAbsolutePath()));

        // Initialize the headers
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("linearAccelerometerX",
                        "linearAccelerometerY",
                        "linearAccelerometerZ",
                        "accelerometerX",
                        "accelerometerY",
                        "accelerometerZ",
                        "gyroscopeX",
                        "gyroscopeY",
                        "gyroscopeZ",
                        "gameRotationX",
                        "gameRotationY",
                        "gameRotationZ"));

        // Log all the entried
        for (SensorLogEntry entry: sensorLogInstance.sensorLogEntries) {
            csvPrinter.printRecord(entry.linearAccelerometerX,
                    entry.linearAccelerometerY,
                    entry.linearAccelerometerZ,
                    entry.accelerometerX,
                    entry.accelerometerY,
                    entry.accelerometerZ,
                    entry.gyroscopeX,
                    entry.gyroscopeY,
                    entry.gyroscopeZ,
                    entry.gameRotationX,
                    entry.gameRotationY,
                    entry.gameRotationZ);
        }

        csvPrinter.flush();
    }

    private boolean haveWritePermissions(){
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
