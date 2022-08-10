package com.example.tetris;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import com.example.tetris.data.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardService {

    private static final Integer WIDTH = 100;
    private static final Integer HEIGHT = 100;

    public ImageButton prepareImageButton(Context context, Integer xPosition, Integer yPosition) {
        ImageButton imageButton = new ImageButton(context);

        imageButton.setxPosition(xPosition);
        imageButton.setyPosition(yPosition);
        imageButton.setImageDrawable(getRandomlyImage(context));
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setLayoutParams(prepareLayoutParams());

        return imageButton;
    }

    private Drawable getRandomlyImage(Context context) {
        List<Drawable> list = new ArrayList<>();
        list.add(context.getResources().getDrawable(R.drawable.flower));
        list.add(context.getResources().getDrawable(R.drawable.sun));
        list.add(context.getResources().getDrawable(R.drawable.umbrella));

        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    private LinearLayout.LayoutParams prepareLayoutParams() {
        return new LinearLayout.LayoutParams(WIDTH, HEIGHT);
    }

}
