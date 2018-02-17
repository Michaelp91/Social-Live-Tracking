package com.slt.utils;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Usman Ahmad on 15.02.2018.
 */

public class FunctionalityLogger {
    private FileWriter writer;
    private FileWriter errorLogger;
    private static final FunctionalityLogger ourInstance = new FunctionalityLogger();

    public static FunctionalityLogger getInstance() {
        return ourInstance;
    }

    private FunctionalityLogger() {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            boolean test = false;
            if (!root.exists()) {
                test = root.mkdirs();
                boolean test2 = false;
            }
            File gpxfile = new File(root, "TimelineLog.txt");
            File file2 = new File(root, "ErrorLog.txt");
            writer = new FileWriter(gpxfile);
            errorLogger = new FileWriter(file2);
            errorLogger.append("Errors: ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddLog(String string) {
        try {
            writer.append("\n" + string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddErrorLog(String string) {
        try {
            errorLogger.append("\n" + string);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FinishFileWriting() {
        try {
            errorLogger.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
