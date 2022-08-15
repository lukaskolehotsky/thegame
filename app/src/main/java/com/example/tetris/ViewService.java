package com.example.tetris;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;

import androidx.annotation.RequiresApi;

import com.example.tetris.constants.SharedPreferencesConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViewService {

    private final SharedPreferencesService sharedPreferencesService;

    public ViewService(SharedPreferencesService sharedPreferencesService) {
        this.sharedPreferencesService = sharedPreferencesService;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void swapViews(Context context, View view) {
        float xFrom = sharedPreferencesService.retrievePosition(context, SharedPreferencesConstants.X);
        float yFrom = sharedPreferencesService.retrievePosition(context, SharedPreferencesConstants.Y);
        GridLayout gridLayout = (GridLayout) view.getParent();
        View viewFrom = findViewByPositions(gridLayout, xFrom, yFrom);

        float viewX = view.getX();
        float viewY = view.getY();
        float viewFromX = viewFrom.getX();
        float viewFromY = viewFrom.getY();

        TranslateAnimation fromAnimation = null;
        TranslateAnimation toAnimation = null;

        if (viewFrom.getX() < view.getX() && Float.compare(viewFrom.getY(), view.getY()) == 0) {
            fromAnimation = new TranslateAnimation(0, 100, 0, 0);
            toAnimation = new TranslateAnimation(0, -100, 0, 0);
        }

        if (viewFrom.getX() > view.getX() && Float.compare(viewFrom.getY(), view.getY()) == 0) {
            fromAnimation = new TranslateAnimation(0, -100, 0, 0);
            toAnimation = new TranslateAnimation(0, +100, 0, 0);
        }

        if (viewFrom.getY() < view.getY() && Float.compare(viewFrom.getX(), view.getX()) == 0) {
            fromAnimation = new TranslateAnimation(0, 0, 0, 100);
            toAnimation = new TranslateAnimation(0, 0, 0, -100);
        }

        if (viewFrom.getY() > view.getY() && Float.compare(viewFrom.getX(), view.getX()) == 0) {
            fromAnimation = new TranslateAnimation(0, 0, 0, -100);
            toAnimation = new TranslateAnimation(0, 0, 0, +100);
        }

        assert fromAnimation != null;
        fromAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewFrom.setX(viewX);
                viewFrom.setY(viewY);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        toAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setX(viewFromX);
                view.setY(viewFromY);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fromAnimation.setDuration(200);
        toAnimation.setDuration(200);

        viewFrom.startAnimation(fromAnimation);
        view.startAnimation(toAnimation);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View findViewByPositions(GridLayout gridLayout, float x, float y) {
        Optional<View> viewOptional = getAllViews(gridLayout)
                .stream()
                .filter(v -> Float.compare(v.getX(), x) == 0 && Float.compare(v.getY(), y) == 0)
                .findFirst();

        return viewOptional.orElse(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Boolean validateNextPosition(Context context, View view) {
        float xFrom = sharedPreferencesService.retrievePosition(context, SharedPreferencesConstants.X);
        float yFrom = sharedPreferencesService.retrievePosition(context, SharedPreferencesConstants.Y);

        return findAllArroundCross(view).stream()
                .anyMatch(v -> Float.compare(v.getX(), xFrom) == 0 && Float.compare(v.getY(), yFrom) == 0);
    }

    public List<View> getAllViews(GridLayout gridLayout) {
        List<View> views = new ArrayList<>();

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View view = gridLayout.getChildAt(i);
            views.add(view);
        }

        return views;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<View> findAllArroundCross(View view) {

        GridLayout gridLayout = (GridLayout) view.getParent();

        return getAllViews(gridLayout)
                .stream()
                .filter(v -> (Float.compare(v.getX(), view.getX() - 100f) == 0 && Float.compare(v.getY(), view.getY()) == 0)
                        || (Float.compare(v.getX(), view.getX()) == 0 && Float.compare(v.getY(), view.getY() - 100f) == 0)
                        || (Float.compare(v.getX(), view.getX() + 100f) == 0 && Float.compare(v.getY(), view.getY()) == 0)
                        || (Float.compare(v.getX(), view.getX()) == 0 && Float.compare(v.getY(), view.getY() + 100f) == 0)
                )
                .collect(Collectors.toList());
    }

    @Deprecated
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<View> findAllArroundDiagonally(View view) {

        GridLayout gridLayout = (GridLayout) view.getParent();

        return getAllViews(gridLayout)
                .stream()
                .filter(v -> (Float.compare(v.getX(), view.getX() + 100f) == 0 && Float.compare(v.getY(), view.getY() - 100f) == 0)
                        || (Float.compare(v.getX(), view.getX() - 100f) == 0 && Float.compare(v.getY(), view.getY() + 100f) == 0)
                        || (Float.compare(v.getX(), view.getX() + 100f) == 0 && Float.compare(v.getY(), view.getY() + 100f) == 0)
                        || (Float.compare(v.getX(), view.getX() - 100f) == 0 && Float.compare(v.getY(), view.getY() - 100f) == 0)
                )
                .collect(Collectors.toList());
    }

}
