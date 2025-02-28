package com.example.dominentcolor_backgound;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout backgroundLayout;
    private Spinner imageSpinner, colorSpinner;
    private Handler handler = new Handler();
    private Runnable hideDropdowns;
    private String[] imageOptions = {"sampleimage1", "sampleimage2", "sampleimage3", "sampleimage4", "sampleimage5", "sampleimage6", "sampleimage7", "sampleimage8", "sampleimage9", "sampleimage10"};
    private HashMap<String, Integer> imageMap;
    int defaultColor = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundLayout = findViewById(R.id.backgroundLayout);
        ImageView imageView = findViewById(R.id.imageView);
        imageSpinner = findViewById(R.id.imageSpinner);
        colorSpinner = findViewById(R.id.colorMethodSpinner);

        imageMap = new HashMap<>();
        for (String imageName : imageOptions) {
            int resID;
            resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
            imageMap.put(imageName, resID);
        }

        ArrayAdapter<String> imageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, imageOptions);
        imageSpinner.setAdapter(imageAdapter);

        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{
                        "Dominant Color (Full Image)",
                        "Dominant Color (Center 50%)",
                        "Dominant Color (Center 25%)",
                        "Most Saturated Color (100% Sat, 50% Lightness)",
                        "getMostHueAndSaturatedColor"
                }
        );
        colorSpinner.setAdapter(colorAdapter);

        imageView.setOnClickListener(v -> {
            imageSpinner.setVisibility(View.VISIBLE);
            colorSpinner.setVisibility(View.VISIBLE);
            resetDropdownTimer();
        });

        imageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateBackgroundColor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateBackgroundColor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        hideDropdowns = () -> {
            imageSpinner.setVisibility(View.GONE);
            colorSpinner.setVisibility(View.GONE);
        };
    }

    private void resetDropdownTimer() {
        handler.removeCallbacks(hideDropdowns);
        handler.postDelayed(hideDropdowns, 10000);
    }

    private void updateBackgroundColor() {
        String selectedImage = (String) imageSpinner.getSelectedItem();
        if (selectedImage == null || !imageMap.containsKey(selectedImage)) {
            Log.e("ImageError", "Invalid image selection");
            return;
        }

        int imageResId = imageMap.get(selectedImage);
        Log.d("GlideDebug", "Loading image: " + imageResId);

        // Display the image in ImageView
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load(imageResId)
                .into(imageView);

        // Extract dominant color
        Glide.with(this)
                .asBitmap()
                .load(imageResId)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        if (bitmap == null) {
                            Log.e("BitmapError", "Failed to load bitmap");
                            return;
                        }

                        int selectedColorOption = colorSpinner.getSelectedItemPosition();
                        switch (selectedColorOption) {
                            case 0: // Full image dominant color
                                backgroundLayout.setBackgroundColor(getDominantColor(bitmap,defaultColor));
                                break;

                            case 1: // 50% × 50% center section
                                backgroundLayout.setBackgroundColor(getDominantColorFromCenter50Section(bitmap));
                                break;

                            case 2: // 25% × 25% center section
                                backgroundLayout.setBackgroundColor(getDominantColorFromCenter25Section(bitmap));
                                break;

                            case 3: // Most saturated color with 100% saturation and 50% lightness
                                backgroundLayout.setBackgroundColor(getAdjustedSaturatedColor(bitmap));
                                break;
                            case 4: // Most saturated color with 100% saturation and 50% lightness
                                backgroundLayout.setBackgroundColor(getMostHueAndSaturatedColor(bitmap));
                                break;

                            default:
                                Log.e("Error", "Invalid color selection");
                                break;
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Cleanup if needed
                    }
                });
    }
//    private void updateBackgroundColor() {
//        String selectedImage = imageSpinner.getSelectedItem().toString();
//        int imageResId = imageMap.get(selectedImage);
//        Log.d("GlideDebug", "Loading image: " + imageResId);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sampleimage1);
//        if (bitmap == null) {
//            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sampleimage1);
//        }
//
//        if (colorSpinner.getSelectedItemPosition() == 0) {
//            backgroundLayout.setBackgroundColor(getDominantColor(bitmap));
//        } else {
//            backgroundLayout.setBackgroundColor(getDominantColorFromCenterSection(bitmap));
//        }
//    }

    public static int getDominantColor(Bitmap bitmap, int defaultColor) {
        Palette palette3 = Palette.from(bitmap).generate();
        return palette3.getDominantColor(defaultColor);
    }

//    private int getDominantColor(Bitmap bitmap) {
//        int redBucket = 0, greenBucket = 0, blueBucket = 0;
//        int pixelCount = 0;
//
//        for (int y = 0; y < bitmap.getHeight(); y++) {
//            for (int x = 0; x < bitmap.getWidth(); x++) {
//                int color = bitmap.getPixel(x, y);
//                redBucket += Color.red(color);
//                greenBucket += Color.green(color);
//                blueBucket += Color.blue(color);
//                pixelCount++;
//            }
//        }
//
//        return Color.rgb(redBucket / pixelCount, greenBucket / pixelCount, blueBucket / pixelCount);
//    }

    private int getDominantColorFromCenter50Section(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int sectionWidth = width / 2;
        int sectionHeight = height / 2;
        int startX = (width - sectionWidth) / 2;
        int startY = (height - sectionHeight) / 2;

        Bitmap centerSection = Bitmap.createBitmap(bitmap, startX, startY, sectionWidth, sectionHeight);
        return getDominantColor(centerSection,defaultColor);
    }

    private int getDominantColorFromCenter25Section(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int sectionWidth = width / 4;
        int sectionHeight = height / 4;
        int startX = (width - sectionWidth) / 2;
        int startY = (height - sectionHeight) / 2;

        Bitmap centerSection = Bitmap.createBitmap(bitmap, startX, startY, sectionWidth, sectionHeight);
        return getDominantColor(centerSection,defaultColor);
    }

    private int getAdjustedSaturatedColor(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixelCount = width * height;

        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        float maxSaturation = 0;
        int bestColor = Color.BLACK;

        for (int pixel : pixels) {
            float[] hsv = new float[3];
            Color.colorToHSV(pixel, hsv);

            if (hsv[1] > maxSaturation) { // Check saturation
                maxSaturation = hsv[1];
                bestColor = pixel;
            }
        }

        // Convert best color to HSV and adjust saturation & lightness
        float[] bestHsv = new float[3];
        Color.colorToHSV(bestColor, bestHsv);

        bestHsv[1] = 1.0f; // Set Saturation to 100%
        bestHsv[2] = 1.0f; //(bestHsv[2]*(2-bestHsv[1]))/2; // Set Lightness to 50%
        Log.d("ColorDebug", "H: "+bestHsv[0]+"S: "+bestHsv[1]+"B: "+bestHsv[2]);
        return Color.HSVToColor(bestHsv);
    }
    private int getMostHueAndSaturatedColor(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int pixelCount = width * height;

            int[] pixels = new int[pixelCount];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            float maxHue = 0, maxSaturation = 0;
            int bestColor = Color.BLACK;

            for (int pixel : pixels) {
                float[] hsv = new float[3];
                Color.colorToHSV(pixel, hsv);

                // Check if hue and saturation are highest
                if (hsv[0] > maxHue || (hsv[0] == maxHue && hsv[1] > maxSaturation)) {
                    maxHue = hsv[0];
                    maxSaturation = hsv[1];
                    bestColor = pixel;
                }
            }

            float[] bestHsv = new float[3];
            Color.colorToHSV(bestColor, bestHsv);

            // Set saturation to 100% and adjust brightness to HSL Lightness 50%
            bestHsv[1] = 1.0f;
            bestHsv[2] = 1.0f; //(bestHsv[2] * (2 - bestHsv[1])) / 2;

          return Color.HSVToColor(bestHsv);
        }


}





























































































































































































































































