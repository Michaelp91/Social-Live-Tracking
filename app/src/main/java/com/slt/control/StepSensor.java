package com.slt.control;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.util.Log;

/**
 * Class StepSenor, used to track the steps of the user
 */
public class StepSensor implements SensorEventListener {

    /**
     * used to get an ID for each sensor
     */
    private static int ID = 0;

    /**
     * ID of the sensor
     */
    private int sensorID;

    /**
     * Tag for the logger
     */
    public static final String TAG = "StepSensor";

    /**
     *  Sensor latency used, 0 means continuous mode
     */
    private static final int SENSOR_LATENCY = 0;


    /**
     *  Start steps that were detected at the beginning, used as a base
     */
    private int myStartSteps = 0;

    /**
     *  Current Steps that were detected
     */
    private int myCurrentSteps = 0;

    /**
     *  Constructor
     */
    public StepSensor() {
        this.myStartSteps = 0;
        this.myCurrentSteps = 0;
        registerEventListener();
        this.sensorID = ++StepSensor.ID;
    }

    /**
     *  Unregister listeners on destroy
     */
    @Override
     protected void finalize(){
        unregisterListeners();
    }


    /**
     * Registers the event Listener for the Steps
     */
    private void registerEventListener() {
        // Get the default sensor for the sensor type from the SenorManager
        SensorManager sensorManager =
                (SensorManager) ApplicationController.getContext().getSystemService(Activity.SENSOR_SERVICE);

        // get DefaultSensor
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Register the listener for this sensor in batch mode since delay is 0 they are delivered
        // in continuous mode
        final boolean batchMode = sensorManager.registerListener(
                this, sensor, SensorManager.SENSOR_DELAY_NORMAL, StepSensor.SENSOR_LATENCY);

        Log.i(TAG, "Sensor listener "+ sensorID + " registered.");
    }

    /**
     * Unregisters the sensor listener if it is registered.
     */
    public void unregisterListeners() {
        SensorManager sensorManager =
                (SensorManager) ApplicationController.getContext().getSystemService(Activity.SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
        Log.i(TAG, "Sensor listener unregistered.");
    }

    /**
     * Overwritten onSensorChangedEvents, just initializes counters and updates them on Receiving
     * of an event
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        //If first event initialize counters
        if (this.myStartSteps < 1) {
            // initialize value
            myCurrentSteps = myStartSteps = (int) event.values[0];
        }

        // Calculate steps taken based on first counter value received.
        this.myCurrentSteps = (int) event.values[0];
    }

    /**
     * Overwritten onAccuracyChanged Method, just do nothing
     * @param sensor - Sensor we want to change
     * @param accuracy - Accurracy of the sensor
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Get the currently counted steps
     * @return The counted steps
     */
    public int getSteps(){
        int steps = this.myCurrentSteps - this.myStartSteps;
        this.myStartSteps = this.myCurrentSteps;
        Log.i(TAG, "Step detected by STEP_COUNTER sensor "+ sensorID + ". Steps: " + steps);
        return steps;
    }

}
