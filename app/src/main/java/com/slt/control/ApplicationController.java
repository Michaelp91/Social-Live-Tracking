package com.slt.control;

import android.app.Application;
import android.content.Context;
import android.util.Log;


/**
 * ApplicationController allows us to get the Context of the application if we need it
 */
public class ApplicationController extends Application {

    /**
     * Tag for the Logger
     */
    public static final String TAG = "ApplicationController";

    /**
     * The instance of the Application Controller
     */
    private static ApplicationController myInstance;

    /**
     * Initializes the attributes
     */
    @Override
    public void onCreate() {
        Log.i(TAG, "On Create method called.");
        super.onCreate();
        myInstance = this;
    }

    /**
     * Get the instance of the controller
     * @return The instance of the Application Controller
     */
    public static synchronized ApplicationController getInstance() {

        return myInstance;
    }

    /**
     * Get the application context
     * @return The context of the application
     */
    public static Context getContext(){
        return myInstance.getApplicationContext();
    }

}