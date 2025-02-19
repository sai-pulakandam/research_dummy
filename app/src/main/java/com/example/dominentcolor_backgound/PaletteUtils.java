package com.example.dominentcolor_backgound;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.palette.graphics.Palette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaletteUtils {
    public static int getMostSaturatedColor(Bitmap bitmap) {
        Palette palette = Palette.from(bitmap).generate();
        List<Palette.Swatch> swatches = palette.getSwatches();
        int mostSaturatedColor = Color.BLACK;
        float maxSaturation = 0F;

        for (Palette.Swatch swatch : swatches) {
            float[] hsl = swatch.getHsl();
            //hsl[1] is for setting the Saturation
            float saturation = hsl[1];
            if (saturation > maxSaturation) {
                maxSaturation = saturation;
                mostSaturatedColor = swatch.getRgb();
            }
        }
        return mostSaturatedColor;
    }

    public static int getVibrantColor(Bitmap bitmap, int defaultColor) {
        Palette palette2 = Palette.from(bitmap).generate();
        return palette2.getVibrantColor(defaultColor);
    }

    public static int getDominantColor(Bitmap bitmap, int defaultColor) {
        Palette palette3 = Palette.from(bitmap).generate();
        return palette3.getDominantColor(defaultColor);
    }

    public static int getCenterColor(Bitmap bitmap) {
        //Palette palette4= Palette.from(bitmap).generate();
        int centerX = bitmap.getWidth() / 2;
        int centerY = bitmap.getHeight() / 2;
        return bitmap.getPixel(centerX, centerY);
    }

    public static int getMostHueColor(Bitmap bitmap) {
        Palette palette5 = Palette.from(bitmap).generate();
        List<Palette.Swatch> swatches = palette5.getSwatches();
        int mostHueColor = Color.BLACK;
        float maxHue = 0F;

        for (Palette.Swatch swatch : swatches) {
            float[] hsl = swatch.getHsl();
            float hue = hsl[0];
            if (hue > maxHue) {
                maxHue = hue;
                mostHueColor = swatch.getRgb();
            }
        }
        return mostHueColor;
    }

    public static int getMostHueAndSaturatedColor(Bitmap bitmap) {
        Palette palette6 = Palette.from(bitmap).generate();
        List<Palette.Swatch> swatches = palette6.getSwatches();
        int bestColor = Color.BLACK;
        float maxSaturation = 0F;

        for (Palette.Swatch swatch : swatches) {
            float[] hsl = swatch.getHsl();
            float saturation = hsl[1];
            if (saturation > maxSaturation) {
                maxSaturation = saturation;
                bestColor = swatch.getRgb();
            }
        }
        float[] hsl = new float[3];
        Color.colorToHSV(bestColor, hsl);
        hsl[0] = (hsl[0] + 30) % 360;
        hsl[1] = Math.min(1.0f, hsl[1] * 1.5f);

        return Color.HSVToColor(hsl);
    }

    public static int getMostHueAndSaturatedAndLightnessColor(Bitmap bitmap) {
        Palette palette7 = Palette.from(bitmap).generate();
        List<Palette.Swatch> swatches = palette7.getSwatches();
        int bestColor = Color.BLACK;
        float maxSaturation = 0F;

        for (Palette.Swatch swatch : swatches) {
            float[] hsl = swatch.getHsl();
            float saturation = hsl[1];
            if (saturation > maxSaturation) {
                maxSaturation = saturation;
                bestColor = swatch.getRgb();
            }
        }
        float[] hsl = new float[3];
        Color.colorToHSV(bestColor, hsl);
        hsl[0] = (hsl[0] + 30) % 360;
        hsl[1] = Math.min(1.0f, hsl[1] * 1.5f);
        hsl[2] = 0.5f;

        return Color.HSVToColor(hsl);
    }

    public static int getMostSaturatedAndLightnessColor(Bitmap bitmap) {
        Palette palette8 = Palette.from(bitmap).generate();
        List<Palette.Swatch> swatches = palette8.getSwatches();
        int bestColor = Color.BLACK;
        float maxSaturation = 0F;

        for (Palette.Swatch swatch : swatches) {
            float[] hsl = swatch.getHsl();
            float saturation = hsl[1];
            if (saturation > maxSaturation) {
                maxSaturation = saturation;
                bestColor = swatch.getRgb();
            }
        }
        float[] hsl = new float[3];
        Color.colorToHSV(bestColor, hsl);
        hsl[1] = Math.min(1.0f, hsl[1] * 1.5f);
        hsl[2] = 0.5f;

        return Color.HSVToColor(hsl);
    }

    public static int getMostFrequentColorFromMiddleSection(Bitmap bitmap) {
            if (bitmap == null) return Color.BLACK; // Default color in case of failure

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // Define middle section (50% of width and height)
            int cropWidth = width / 2;
            int cropHeight = height / 2;
            int startX = width / 4;
            int startY = height / 4;

            Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, startX, startY, cropWidth, cropHeight);

            Map<Integer, Integer> colorCountMap = new HashMap<>();
            int mostFrequentColor = Color.BLACK;
            int maxCount = 0;

            for (int y = 0; y < croppedBitmap.getHeight(); y++) {
                for (int x = 0; x < croppedBitmap.getWidth(); x++) {
                    int pixel = croppedBitmap.getPixel(x, y);

                    // Count the color occurrences
                    int count = colorCountMap.getOrDefault(pixel, 0) + 1;
                    colorCountMap.put(pixel, count);

                    // Update most frequent color
                    if (count > maxCount) {
                        maxCount = count;
                        mostFrequentColor = pixel;
                    }
                }
            }
            croppedBitmap.recycle(); // Free memory
            return mostFrequentColor;
        }
    }




