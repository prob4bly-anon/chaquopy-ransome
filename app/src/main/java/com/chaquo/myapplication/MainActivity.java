package com.chaquo.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;

import com.chaquo.python.PyException;
import com.chaquo.python.Python;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.myapplication.*;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.PrintStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Environment;
import android.os.AsyncTask;
import android.widget.Toast;
import android.os.*;
import android.util.*;

public class MainActivity extends Activity {

    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private TextView logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logTextView = findViewById(R.id.logTextView);

        requestPermissions();

       
    }
	
	private void requestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS }, REQUEST_STORAGE_PERMISSION); 
		}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
  exepy();
				}else{
                Log.v("MAINACTIVITY", "Storage permission denied.");
            }
        }
	
        
    }
	
    private void exepy() {
        // Start Python if necessary
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

 
		try {
			Python py = Python.getInstance();
			PyObject object = py.getModule("main").callAttr("main"); // Assuming the main function is in main.py
			final String output = object.toString();
	/*PyObject sys = py.getModule("sys");
	PyObject stdout = sys.stdout;  // This is the correct way to access stdout
			final String capturedOutput = stdout.toString();
			log("STDOUT\n" + capturedOutput);*/
			log("STDOUT\n" + output);
		} catch (Exception e) {
			log("STDERR\n" + e.toString());
		}
	}
	private void log(String message) {

		logTextView.append(message + "\n");

	};

}
