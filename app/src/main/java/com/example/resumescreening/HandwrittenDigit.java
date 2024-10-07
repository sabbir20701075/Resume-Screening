package com.example.resumescreening;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.example.resumescreening.ml.MnistModel;
import java.io.IOException;


public class HandwrittenDigit extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView imageView;
    private TextView textViewPrediction;
    private MnistModel mnistModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwritten_digit);

        imageView = findViewById(R.id.imageView);
        textViewPrediction = findViewById(R.id.textViewPrediction);
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
                imageView.setImageBitmap(bitmap);
                classifyImage(bitmap);
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
        int predictedClass = getMaxIndex(probabilities);
        textViewPrediction.setText("Predicted Class: " + predictedClass);
    }

    private int getMaxIndex(float[] output) {
        int index = 0;
        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[index]) {
                index = i;
            }
        }
        return index;
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
