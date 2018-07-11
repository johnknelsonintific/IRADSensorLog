package com.example.intific.iradsensorlog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.intific.iradsensorlog.sensorlog.SensorLogger;
import com.example.intific.iradsensorlog.sensorlog.SensorLoggerConfig;

public class MainActivity extends AppCompatActivity implements SensorLogger.SensorLogListenerInterface {

    // sensor logger
    SensorLogger sensorLogger;
    Boolean currentlyLogging = false;

    // UI references
    Button logButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate sensor logger
        SensorLoggerConfig sensorLoggerConfig = new SensorLoggerConfig(true);
        sensorLogger = new SensorLogger(sensorLoggerConfig, this, this);

        // Natively bind members to UI elements
        logButton = findViewById(R.id.log_button);

        // Define click behavior for UI
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlyLogging){
                    sensorLogger.stopLogger();
                    logButton.setText(getString(R.string.start_logging));
                } else {
                    sensorLogger.startLogger();
                    logButton.setText(R.string.stop_logging);
                }
                currentlyLogging = !currentlyLogging;// Turn logging on or off
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_view_log_files) {
            //TODO Either open the folder containing the log files or create a way to view them
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPostureRadioButtonClicked(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_standing:
                if (checked)
                    break;
            case R.id.radio_crouching:
                if (checked)
                    break;
            case R.id.radio_prone:
                if (checked)
                    break;

        }
    }

    @Override
    public void sensorLoggingStarted() {
        //TODO Update the UI as needed
    }

    @Override
    public void sensorLoggingEnded() {
        //TODO Update the UI as needed when the logging is done
    }
}
