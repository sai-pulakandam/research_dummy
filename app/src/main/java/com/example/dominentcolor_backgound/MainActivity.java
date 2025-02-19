package com.example.dominentcolor_backgound;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.image);
        int defaultColor = 0xFF000000;
        ImageView backgroundImageView = findViewById(R.id.background);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.final_main_img);
        int background = PaletteUtils.getMostFrequentColorFromMiddleSection(bitmap);
        backgroundImageView.setBackgroundColor(background);
    }
}