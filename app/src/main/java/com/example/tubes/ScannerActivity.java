package com.example.tubes;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;

    private PreviewView previewView;
    private ImageView btnBack, btnFlash, btnSettings, btnCaptureImageView;
    private FrameLayout btnCaptureLayout;
    private View btnGallery, btnHistory;
    private View scanningLine;
    private ProgressBar progressBar;
    private ImageView[] corners;

    private ProcessCameraProvider cameraProvider;
    private ImageCapture imageCapture;
    private Camera camera;
    private boolean isFlashEnabled = false;
    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        initViews();
        setupClickListeners();

        if (checkCameraPermission()) {
            startCamera();
        } else {
            requestCameraPermission();
        }
    }

    private void initViews() {
        previewView = findViewById(R.id.previewView);
        btnBack = findViewById(R.id.btnBack);
        btnFlash = findViewById(R.id.btnFlash);
        btnSettings = findViewById(R.id.btnSettings);

        // Mengakses FrameLayout btnCapture dan ImageView di dalamnya
        btnCaptureLayout = findViewById(R.id.btnCapture);
        btnCaptureImageView = btnCaptureLayout.findViewById(R.id.btnCaptureImageView);

        btnGallery = findViewById(R.id.btnGallery);
        btnHistory = findViewById(R.id.btnHistory);
        scanningLine = findViewById(R.id.scanningLine);
        progressBar = findViewById(R.id.progressBar);

        corners = new ImageView[]{
                findViewById(R.id.cornerTopLeft),
                findViewById(R.id.cornerTopRight),
                findViewById(R.id.cornerBottomLeft),
                findViewById(R.id.cornerBottomRight)
        };
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnFlash.setOnClickListener(v -> toggleFlash());

        btnSettings.setOnClickListener(v -> {
            // Open settings activity
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
        });

        // Click listener untuk capture button - start waste scanning
        btnCaptureImageView.setOnClickListener(v -> {
            if (!isScanning) {
                startWasteScan();
            }
        });

        btnGallery.setOnClickListener(v -> openGallery());

        btnHistory.setOnClickListener(v -> {
            // Open history activity
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting camera: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        try {
            cameraProvider.unbindAll();
            camera = cameraProvider.bindToLifecycle(
                    (LifecycleOwner) this, cameraSelector, preview, imageCapture);
        } catch (Exception e) {
            Toast.makeText(this, "Error binding camera: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void toggleFlash() {
        if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
            isFlashEnabled = !isFlashEnabled;
            camera.getCameraControl().enableTorch(isFlashEnabled);

            // Update flash icon
            btnFlash.setImageResource(isFlashEnabled ?
                    R.drawable.ic_flash_on : R.drawable.ic_flash_off);
        }
    }

    // Renamed from startDocumentScan to startWasteScan for better clarity
    private void startWasteScan() {
        if (isScanning) return;

        isScanning = true;

        // Show scanning animation
        showScanningAnimation();

        // Show scanning message
        Toast.makeText(this, "Scanning waste type...", Toast.LENGTH_SHORT).show();

        // Simulate waste detection process
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate processing time

                runOnUiThread(() -> {
                    isScanning = false;
                    hideScanningAnimation();
                    captureImage();
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    isScanning = false;
                    hideScanningAnimation();
                });
            }
        }).start();
    }

    private void showScanningAnimation() {
        scanningLine.setVisibility(View.VISIBLE);

        // Animate scanning line
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            scanningLine.setTranslationY(progress * 200 - 100);
        });

        animator.start();

        // Highlight corners with green color for scanning
        for (ImageView corner : corners) {
            corner.setColorFilter(ContextCompat.getColor(this, R.color.green_500));
        }
    }

    private void hideScanningAnimation() {
        scanningLine.setVisibility(View.GONE);

        // Reset corners to default color
        for (ImageView corner : corners) {
            corner.setColorFilter(ContextCompat.getColor(this, R.color.gray_500));
        }
    }

    private void captureImage() {
        if (imageCapture == null) return;

        // Create output file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String filename = "WASTE_SCAN_" + timestamp + ".jpg";

        File outputFile = new File(getExternalFilesDir(null), filename);

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(outputFile).build();

        imageCapture.takePicture(outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                        Toast.makeText(ScannerActivity.this,
                                "Waste scanned successfully!", Toast.LENGTH_SHORT).show();

                        // Update progress
                        updateProgress();

                        // Process the captured image and launch result activity
                        processCapturedImage(outputFile);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(ScannerActivity.this,
                                "Error capturing image: " + exception.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // New method to process captured image
    private void processCapturedImage(File imageFile) {
        // Show analyzing message
        Toast.makeText(this, "Analyzing waste type...", Toast.LENGTH_SHORT).show();

        // Add small delay for better UX, then launch result activity
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 1 second delay

                runOnUiThread(() -> {
                    // Launch scan result activity with captured image
                    ScanHelper.launchScanResult(this, imageFile);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    ScanHelper.launchScanResult(this, imageFile);
                });
            }
        }).start();
    }

    private void updateProgress() {
        int currentProgress = progressBar.getProgress();
        int newProgress = Math.min(currentProgress + 20, 100);

        ValueAnimator progressAnimator = ValueAnimator.ofInt(currentProgress, newProgress);
        progressAnimator.setDuration(500);
        progressAnimator.addUpdateListener(animation ->
                progressBar.setProgress((int) animation.getAnimatedValue()));
        progressAnimator.start();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                // Process selected image from gallery
                processGalleryImage(selectedImage);
            }
        }
    }

    // New method to process gallery image
    private void processGalleryImage(Uri imageUri) {
        Toast.makeText(this, "Processing image from gallery...", Toast.LENGTH_SHORT).show();

        // Launch scan result activity with image from gallery
        ScanHelper.launchScanResult(this, imageUri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }
}