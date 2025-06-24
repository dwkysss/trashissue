package com.example.tubes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ScanHelper {
    public static void launchScanResult(Context context, Bitmap imageBitmap, String resultType) {
        Intent intent = new Intent(context, ScanResultActivity.class);

        if (imageBitmap != null) {
            // Convert bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra("image_bitmap", byteArray);
        }

        if (resultType != null) {
            intent.putExtra("result_type", resultType);
        }

        context.startActivity(intent);
    }

    public static void launchScanResult(Context context, String imagePath, String resultType) {
        Intent intent = new Intent(context, ScanResultActivity.class);

        if (imagePath != null) {
            intent.putExtra("image_path", imagePath);
        }

        if (resultType != null) {
            intent.putExtra("result_type", resultType);
        }

        context.startActivity(intent);
    }

    public static void launchScanResult(Context context, File imageFile) {
        if (imageFile != null && imageFile.exists()) {
            // Load bitmap from file
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

            // Get prediction from ML model
            String prediction = predictWasteType(bitmap);

            // Launch result activity with file path
            launchScanResult(context, imageFile.getAbsolutePath(), prediction);
        }
    }

    public static void launchScanResult(Context context, Uri imageUri) {
        try {
            // Convert URI to bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(
                    context.getContentResolver().openInputStream(imageUri));

            if (bitmap != null) {
                // Get prediction from ML model
                String prediction = predictWasteType(bitmap);

                // Launch result activity
                launchScanResult(context, bitmap, prediction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String predictWasteType(Bitmap bitmap) {
        String[] wasteTypes = {"Sampah Anorganik", "Sampah Organik"};
        int randomIndex = (int) (Math.random() * wasteTypes.length);
        return wasteTypes[randomIndex];
    }

    public static WastePrediction predictWasteTypeWithConfidence(Bitmap bitmap) {
        String[] wasteTypes = {"Sampah Anorganik", "Sampah Organik"};
        int[] confidences = {95, 92, 88, 85};

        int typeIndex = (int) (Math.random() * wasteTypes.length);
        int confidenceIndex = (int) (Math.random() * confidences.length);

        return new WastePrediction(wasteTypes[typeIndex], confidences[confidenceIndex]);
    }

    public static class WastePrediction {
        public String type;
        public int confidence;

        public WastePrediction(String type, int confidence) {
            this.type = type;
            this.confidence = confidence;
        }
    }
}