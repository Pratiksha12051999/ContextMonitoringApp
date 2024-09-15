package com.example.contextmonitoringapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "HeartRateMonitor";
    private Camera camera;
    private SurfaceView cameraPreview;
    private SurfaceHolder previewHolder;
    private TextView heartRateTextView;
    private Button calculateHeartRateButton;
    private Button respiratoryStartButton;
    private Button symptomsViewButton;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView respiratoryRateTextView;

    private boolean measuring = false;
    private boolean isMeasuringRespiration = false;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private long startTime = 0;
    private int beats = 0;
    private int heartBeatCount = 0;
    private int respiratoryCount = 0;
    private long startTimeRespiration = 0;
    private int breathCount = 0;
    private static final int SAMPLING_RATE = 200000; // microseconds
    private static final int MEASUREMENT_INTERVAL = 60000; // 1 minute in milliseconds
    private float previousZ = 0;
    private boolean inhaling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        heartRateTextView = findViewById(R.id.heartbeatTextView);
        calculateHeartRateButton = findViewById(R.id.buttonCalculateHeartRate2);
        respiratoryRateTextView = findViewById(R.id.respiratoryRateTextView);
        respiratoryStartButton = findViewById(R.id.buttonCalculateRespiratoryRate);
        symptomsViewButton = findViewById(R.id.symptomsButton);

        cameraPreview = findViewById(R.id.cameraPreview);
        previewHolder = cameraPreview.getHolder(); // Obtain SurfaceHolder from SurfaceView
        previewHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Surface is created, we can start the camera preview here
                if (camera != null) {
                    startCameraPreview();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                if (previewHolder.getSurface() == null) {
                    return;
                }
                try {
                    if (camera != null) {
                        camera.stopPreview();
                        startCameraPreview();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error restarting camera preview: " + e.getMessage());
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera != null) {
                    camera.stopPreview();
                }
            }
        });

        calculateHeartRateButton.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                if (measuring) {
                    stopMeasuring();
                } else {
                    startMeasuring();
                }
            }
        });

        respiratoryStartButton.setOnClickListener(v -> {
            if (!isMeasuringRespiration) {
                startMeasuringRespiration();
            } else {
                Toast.makeText(MainActivity.this, "Measurement is already in progress", Toast.LENGTH_SHORT).show();
            }
        });

        symptomsViewButton.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, Symptoms.class);

            intent.putExtra("heartBeats", heartBeatCount);
            intent.putExtra("respiratoryBeats", respiratoryCount);
            startActivity(intent);
        });

    }

    private void startCameraPreview() {
        try {
            camera.setPreviewDisplay(previewHolder);
            camera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private void startMeasuring() {
        if (camera == null) {
            Log.i(TAG, "Called start measuring method now");
            camera = Camera.open();  // Open the back camera
            Camera.Parameters params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            params.setPreviewFormat(ImageFormat.NV21);
            camera.setParameters(params);

            try {
                startCameraPreview();
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    private double baselineBrightness = 0.0;
                    private int baselineFrames = 30;
                    private int frameCount = 0;

                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        if (!measuring) return;

                        long currentTime = System.currentTimeMillis();

                        if (frameCount < baselineFrames) {
                            baselineBrightness += calculateBrightness(data, camera);
                            frameCount++;
                            if (frameCount == baselineFrames) {
                                baselineBrightness /= baselineFrames;
                                Log.i(TAG, "Baseline brightness set to: " + baselineBrightness);
                            }
                            return;
                        }

                        double brightness = calculateBrightness(data, camera);
                        double threshold = baselineBrightness * 1.05; // 10% increase threshold

                        if (brightness > threshold) {
                            beats++;
                        }

                        if (currentTime - startTime >= 10000) {
                            int heartRate = (beats * 6);
                            updateHeartRate(heartRate);
                            beats = 0;
                            startTime = System.currentTimeMillis();
                        }
                    }
                });

                startTime = System.currentTimeMillis();
                measuring = true;
                Toast.makeText(this, "Heart rate measurement started", Toast.LENGTH_SHORT).show();
                calculateHeartRateButton.setText("Stop");

            } catch (Exception e) {
                Log.e(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    private void stopMeasuring() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
            measuring = false;
            calculateHeartRateButton.setText("Start");
        }
    }

    private double calculateBrightness(byte[] data, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        int width = size.width;
        int height = size.height;

        long sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += (data[i] & 0xFF);
        }
        return sum / (double) (width * height);
    }

    private void updateHeartRate(final int heartRate) {
        heartBeatCount = heartRate;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> heartRateTextView.setText(" " + heartBeatCount));
    }

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (measuring) {
                    startMeasuring();
                }
            } else {
                Toast.makeText(this, "Camera permission is required to measure heart rate", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (measuring && camera == null) {
            startMeasuring();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            stopMeasuring();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isMeasuringRespiration && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            float z = event.values[2];

            if (Math.abs(z - previousZ) > 0.2f) {
                if (!inhaling) {
                    inhaling = true;
                    breathCount++;
                }
            } else {
                inhaling = false;
            }

            previousZ = z;

            if (currentTime - startTimeRespiration >= MEASUREMENT_INTERVAL) {
                int respiratoryRate = breathCount;  // Breaths per minute
                respiratoryCount = respiratoryRate;
                respiratoryRateTextView.setText(" " + respiratoryRate);
                breathCount = 0;
                startTimeRespiration = System.currentTimeMillis();
                isMeasuringRespiration = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No changes needed here
    }

    private void startMeasuringRespiration() {
        breathCount = 0;
        previousZ = 0;
        startTimeRespiration = System.currentTimeMillis();
        isMeasuringRespiration = true;
        sensorManager.registerListener(this, accelerometer, SAMPLING_RATE);
        Toast.makeText(this, "Respiratory rate measurement started", Toast.LENGTH_SHORT).show();
    }
}
