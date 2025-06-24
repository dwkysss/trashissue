package com.example.tubes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScanResultActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView ivScannedImage;
    private TextView tvWasteType;
    private TextView tvConfidence;
    private TextView tvDescription;

    // Sample data for different waste types
    private String[] wasteTypes = {"Sampah Anorganik", "Sampah Organik"};
    private String[] confidenceLevels = {"95% (Sangat Akurat)", "92% (Sangat Akurat)", "88% (Akurat)"};
    private String[] descriptions = {
            "Sampah anorganik tidak dapat terurai secara alami dan memerlukan penanganan khusus untuk daur ulang.",
            "Sampah organik dapat terurai secara alami dan cocok untuk kompos atau pengolahan biogas.",
            "Sampah campuran memerlukan pemilahan lebih lanjut untuk pengelolaan yang optimal."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        initViews();
        setupClickListeners();
        loadScanResult();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        ivScannedImage = findViewById(R.id.ivScannedImage);
        tvWasteType = findViewById(R.id.tvWasteType);
        tvConfidence = findViewById(R.id.tvConfidence);
        tvDescription = findViewById(R.id.tvDescription);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadScanResult() {
        // Get image path from intent if available
        String imagePath = getIntent().getStringExtra("image_path");
        byte[] imageByteArray = getIntent().getByteArrayExtra("image_bitmap");

        if (imagePath != null) {
            // Load image from file path
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                ivScannedImage.setImageBitmap(bitmap);
            }
        } else if (imageByteArray != null) {
            // Load image from byte array
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            if (bitmap != null) {
                ivScannedImage.setImageBitmap(bitmap);
            }
        } else {
            // Load sample image (you should replace this with your sample image)
            loadSampleImage();
        }

        // Load sample result data
        loadSampleData();
    }

    private void loadSampleImage() {
        // Create a sample colored background for demonstration
        // In real implementation, this should be the actual scanned image
        ivScannedImage.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
         // Add your sample image
    }

    private void loadSampleData() {
        // Simulate ML prediction result
        int randomIndex = (int) (Math.random() * wasteTypes.length);
        int confidenceIndex = (int) (Math.random() * confidenceLevels.length);

        // Get result type from intent or use random
        String resultType = getIntent().getStringExtra("result_type");
        if (resultType == null) {
            resultType = wasteTypes[randomIndex];
        }

        // Set the results
        tvWasteType.setText(resultType);
        tvConfidence.setText(confidenceLevels[confidenceIndex]);

        // Set description based on waste type
        if (resultType.contains("Anorganik")) {
            tvDescription.setText(descriptions[0]);
        } else if (resultType.contains("Organik")) {
            tvDescription.setText(descriptions[1]);
        } else {
            tvDescription.setText(descriptions[2]);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}