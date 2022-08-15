package com.example.tetris;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.tetris.constants.ImageViewConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardService {

    private final AnimationService animationService;
    private final SharedPreferencesService sharedPreferencesService;
    private final ViewService viewService;

    public DashboardService(
            AnimationService animationService,
            SharedPreferencesService sharedPreferencesService,
            ViewService viewService) {
        this.animationService = animationService;
        this.sharedPreferencesService = sharedPreferencesService;
        this.viewService = viewService;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void prepareDashboard(Context context, GridLayout gridLayout) {
        for (int i = 0; i < gridLayout.getRowCount(); i++) {
            for (int j = 0; j < gridLayout.getColumnCount(); j++) {
                ImageView imageView = prepareImageView(context);
                gridLayout.addView(imageView);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ImageView prepareImageView(Context context) {

        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(getRandomlyImage(context));
        imageView.setLayoutParams(prepareLayoutParams());

        imageView.setOnClickListener(view -> {
            Toast.makeText(context, "You touched X:" + view.getX() + ", Y:" + view.getY(), Toast.LENGTH_SHORT).show();

            animationService.clickAnimation(view);

            if (!sharedPreferencesService.existsPositions(context)) {
                sharedPreferencesService.storePositions(context, view);
            } else {
                if (viewService.validateNextPosition(context, view)) {
                    viewService.swapViews(context, view);
                    sharedPreferencesService.clearSharedPreferences(context);
                } else {
                    sharedPreferencesService.clearSharedPreferences(context);
                }

            }

        });

        return imageView;
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
        return new LinearLayout.LayoutParams(ImageViewConstants.IMAGE_VIEW_WIDTH, ImageViewConstants.IMAGE_VIEW_HEIGHT);
    }

}
