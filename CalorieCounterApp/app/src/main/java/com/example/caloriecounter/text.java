package com.example.caloriecounter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class text extends AppCompatActivity {
    SurfaceView cameraView;
    TextView imageText;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_text);
        cameraView = findViewById(R.id.surface_view);
        imageText = findViewById(R.id.id_imageText);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.d("MainActivity", "Not Available");
        }
        else
            {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1280, 1024).setRequestedFps(2.0f).setAutoFocusEnabled(true).build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(text.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> detectedItems = detections.getDetectedItems();

                    if (detectedItems.size() != 0 ){
                        imageText.post(new Runnable() {

                            public boolean isNum(char c){
                                if ((c >= 48) && (c <= 57)){
                                    return true;
                                }
                                return false;
                            }

                            @Override
                            public void run() {
                                String s = " ";
                                boolean checked = false;
                                StringBuilder stringBuilder = new StringBuilder();

                                for (int i = 0; i < detectedItems.size(); i++){
                                    TextBlock item = detectedItems.valueAt(i);

                                    if ((item.getValue().contains("Calories")) && (item.getValue().length() > 9)) {
                                        Log.d("Processor", "Text detected! " + item.getValue());
                                        s = item.getValue();
                                        s = s.substring(s.indexOf("Calories"));

                                        String[] lines = s.split("\r\n|\r|\n");

                                        for (String line : lines) {
                                            System.out.println(line);
                                        }

                                        s = lines[0];
                                        int startIndex = 0;

                                        for (int j = 0; j < s.length(); j++) {
                                            if (startIndex == 0) {
                                                if (isNum(s.charAt(j))) {
                                                    startIndex = j;
                                                }
                                            }
                                        }

                                        s = s.substring(startIndex);
                                        int endIndex = s.length();

                                        for (int j = 0; j < s.length(); j++) {
                                            if (endIndex == s.length()) {
                                                if (isNum(s.charAt(j))) {
                                                    //
                                                } else endIndex = j;
                                            }
                                        }

                                        s = s.substring(0, endIndex);
                                        int calorieInt = -1;

                                        try {
                                            calorieInt = Integer.valueOf(s);
                                            Log.d("calorieKEY", endIndex + "This has " + calorieInt + " calories.");

                                            Intent sendInfoBack = new Intent();
                                            sendInfoBack.putExtra(MainActivity.INTENT_CODE, calorieInt);
                                            setResult(RESULT_OK, sendInfoBack);
                                            finish();

                                            checked = true;
                                        } catch (NumberFormatException e) {
                                            Log.d("calorieKey", "please try again, not detected: " + s);
                                        }
                                        ;
                                        imageText.setText("This has " + calorieInt + " calories.");
                                    }
                                }
                            }
                        });
                    }
                }
            });

        }

    }
}