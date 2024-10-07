package com.example.resumescreening;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.resumescreening.ml.MnistModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class HandwrittenDigit extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView imageView;
    private TextView textViewPrediction;
    private BarChart barChart;
    private MnistModel mnistModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwritten_digit);

        imageView = findViewById(R.id.imageView);
        textViewPrediction = findViewById(R.id.textViewPrediction);
        barChart = findViewById(R.id.barChart);
        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);

        // Load the TFLite model
        try {
            mnistModel = MnistModel.newInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) { // Check if bitmap is valid
                    imageView.setImageBitmap(bitmap);
                    classifyImage(bitmap);
                } else {
                    textViewPrediction.setText("Error loading image");
                }
            } catch (Exception e) {
                e.printStackTrace();
                textViewPrediction.setText("Error loading image");
            }
        }
    }

    private void classifyImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, true);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(28 * 28 * 4); // For FLOAT32, each pixel is 4 bytes
        byteBuffer.order(ByteOrder.nativeOrder());

        // Load bitmap pixel data into ByteBuffer as normalized grayscale values
        for (int y = 0; y < 28; y++) {
            for (int x = 0; x < 28; x++) {
                int pixel = resizedBitmap.getPixel(x, y);
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Convert RGB to grayscale
                float grayscaleValue = (red + green + blue) / 3.0f / 255.0f;
                byteBuffer.putFloat(grayscaleValue);
            }
        }

        // Prepare input TensorBuffer
        TensorBuffer inputBuffer = TensorBuffer.createFixedSize(new int[]{1, 28, 28, 1}, DataType.FLOAT32);
        inputBuffer.loadBuffer(byteBuffer);

        // Run model inference
        MnistModel.Outputs outputs = mnistModel.process(inputBuffer);
        TensorBuffer outputBuffer = outputs.getOutputFeature0AsTensorBuffer();

        // Retrieve prediction result
        float[] probabilities = outputBuffer.getFloatArray();

        // Get top 3 predicted classes
        int[] topClasses = getTop3Indices(probabilities);

        // Update UI
        textViewPrediction.setText("Top Predicted Classes: " + topClasses[0] + ", " + topClasses[1] + ", " + topClasses[2]);
        displayBarGraph(probabilities);
    }

    private int[] getTop3Indices(float[] output) {
        int[] indices = new int[3];
        float[] topValues = new float[3];

        for (int i = 0; i < output.length; i++) {
            if (output[i] > topValues[0]) {
                topValues[2] = topValues[1];
                indices[2] = indices[1];
                topValues[1] = topValues[0];
                indices[1] = indices[0];
                topValues[0] = output[i];
                indices[0] = i;
            } else if (output[i] > topValues[1]) {
                topValues[2] = topValues[1];
                indices[2] = indices[1];
                topValues[1] = output[i];
                indices[1] = i;
            } else if (output[i] > topValues[2]) {
                topValues[2] = output[i];
                indices[2] = i;
            }
        }
        return indices;
    }

    private void displayBarGraph(float[] probabilities) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < probabilities.length; i++) {
            entries.add(new BarEntry(i, probabilities[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Digit Probabilities");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // Refresh the chart
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release model resources if no longer used.
        if (mnistModel != null) {
            mnistModel.close();
        }
    }
}
