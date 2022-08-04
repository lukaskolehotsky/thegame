package com.example.tetris;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tetris.data.ImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PlayActivity extends AppCompatActivity {

    private static final Integer WIDTH = 100;
    private static final Integer HEIGHT = 100;
    private static final Integer ROW_AND_COLUMN_COUNT = 9;

    float dX;
    float dY;
    float firstX_point, firstY_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        GridLayout gridLayout = findViewById(R.id.mygridLayout);

        for (int xPosition = 0; xPosition < gridLayout.getColumnCount(); xPosition++) {
            for (int yPosition = 0; yPosition < gridLayout.getRowCount(); yPosition++) {
                ImageButton btn = prepareImageButton(this, xPosition, yPosition);
                gridLayout.addView(btn);

                btn.setOnTouchListener(new View.OnTouchListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public boolean onTouch(View view_from, MotionEvent event) {
                        int action = event.getAction();

                        switch (action) {

                            case MotionEvent.ACTION_DOWN:
                                firstX_point = event.getRawX();
                                firstY_point = event.getRawY();

                                dY = view_from.getY() - event.getRawY();
                                dX = view_from.getX() - event.getRawX();
                                break;

                            case MotionEvent.ACTION_UP:

                                float finalX = event.getRawX();
                                float finalY = event.getRawY();

                                int distanceX = (int) (finalX - firstX_point);
                                int distanceY = (int) (finalY - firstY_point);

                                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                                    if ((firstX_point < finalX)) {
//                                        Log.d("Test", "Left to Right swipe performed");
//                                        Toast.makeText(PlayActivity.this, "Left to Right swipe performed ", Toast.LENGTH_SHORT).show();

                                        ImageButton imageButton_from = (ImageButton) view_from;
                                        moveLeftOrRight(view_from, imageButton_from.getxPosition(), imageButton_from.getyPosition() + 1);
                                    } else {
//                                        Log.d("Test", "Right to Left swipe performed");
//                                        Toast.makeText(PlayActivity.this, "Right to Left swipe performed ", Toast.LENGTH_SHORT).show();

                                        ImageButton imageButton_from = (ImageButton) view_from;
                                        moveLeftOrRight(view_from, imageButton_from.getxPosition(), imageButton_from.getyPosition() - 1);
                                    }
                                } else {
                                    if ((firstY_point < finalY)) {
//                                        Log.d("Test", "Up to Down swipe performed");
//                                        Toast.makeText(PlayActivity.this, "Up to Down swipe performed", Toast.LENGTH_SHORT).show();

                                        ImageButton imageButton_from = (ImageButton) view_from;
                                        moveUpOrDown(view_from, imageButton_from.getxPosition() + 1, imageButton_from.getyPosition());

                                    } else {
//                                        Log.d("Test", "Down to Up swipe performed");
//                                        Toast.makeText(PlayActivity.this, "Down to Up swipe performed", Toast.LENGTH_SHORT).show();

                                        ImageButton imageButton_from = (ImageButton) view_from;
                                        moveUpOrDown(view_from, imageButton_from.getxPosition() - 1, imageButton_from.getyPosition());
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void moveLeftOrRight(View view_from, Integer xPositionFrom, Integer yPositionFrom) {
        ViewParent viewParent = view_from.getParent();
        GridLayout gridLayout = (GridLayout) viewParent;

        ImageButton imageButton_from = (ImageButton) view_from;
        View view_to = null;

        int count = gridLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof ImageButton) {
                ImageButton imb = (ImageButton) view;
                if (checkToPosition(yPositionFrom)
                        && imb.getxPosition().equals(xPositionFrom)
                        && imb.getyPosition().equals(yPositionFrom)) {
                    view_to = view;
                }
            }
        }

        if (view_to != null) {
            ImageButton imageButton_to = (ImageButton) view_to;

            setImageButtonXPositions(imageButton_from, imageButton_to);
            setImageButtonYPositions(imageButton_from, imageButton_to);

            setViewXPositions(view_from, view_to);

            List<View> foundViews = findVerticalRow(gridLayout, view_from);
            findSimilarViews(foundViews, view_from);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void moveUpOrDown(View view_from, Integer xPositionFrom, Integer yPositionFrom) {
        ViewParent viewParent = view_from.getParent();
        GridLayout gridLayout = (GridLayout) viewParent;

        ImageButton imageButton_from = (ImageButton) view_from;
        View view_to = null;

        int count = gridLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof ImageButton) {
                ImageButton imb = (ImageButton) view;
                if (checkToPosition(yPositionFrom)
                        && imb.getxPosition().equals(xPositionFrom)
                        && imb.getyPosition().equals(yPositionFrom)) {
                    view_to = view;
                }
            }
        }

        if (view_to != null) {
            ImageButton imageButton_to = (ImageButton) view_to;

            setImageButtonXPositions(imageButton_from, imageButton_to);
            setImageButtonYPositions(imageButton_from, imageButton_to);

            setViewYPositions(view_from, view_to);

            List<View> foundViews = findHorizontalRow(gridLayout, view_from);
            findSimilarViews(foundViews, view_from);
        }

    }

    private List<View> findVerticalRow(GridLayout gridLayout, View view_from_moved) {
        int count = gridLayout.getChildCount();

        ImageButton imageButton_from_moved = (ImageButton) view_from_moved;

        List<View> foundViews = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            View view = gridLayout.getChildAt(i);

            if (view instanceof ImageButton) {
                ImageButton imageButton = (ImageButton) view;

                Integer yPosition = imageButton.getyPosition();
                Integer yPosition_from_moved = imageButton_from_moved.getyPosition();

                if (yPosition.equals(yPosition_from_moved)) {

                    Integer xPosition = imageButton.getxPosition();
                    Integer xPosition_from_moved = imageButton_from_moved.getxPosition();

                    if (xPosition < xPosition_from_moved + 3
                            && xPosition > xPosition_from_moved - 3) {
                        foundViews.add(view);
                    }
                }

            }
        }

        return foundViews;
    }

    private List<View> findHorizontalRow(GridLayout gridLayout, View view_from_moved) {
        int count = gridLayout.getChildCount();

        ImageButton imageButton_from_moved = (ImageButton) view_from_moved;

        List<View> foundViews = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof ImageButton) {
                ImageButton imageButton = (ImageButton) view;

                Integer xPosition = imageButton.getxPosition();
                Integer xPosition_from_moved = imageButton_from_moved.getxPosition();

                if (xPosition.equals(xPosition_from_moved)) {

                    Integer yPosition = imageButton.getyPosition();
                    Integer yPosition_from_moved = imageButton_from_moved.getyPosition();

                    if (yPosition < yPosition_from_moved + 3
                            && yPosition > yPosition_from_moved - 3) {
                        foundViews.add(view);
                    }
                }
            }
        }

        return foundViews;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void findSimilarViews(List<View> foundViews, View view_from) {

        Collections.sort(foundViews, Comparator.comparing(view -> ((ImageButton) view).getyPosition()));

        List<View> fiveList = foundViews.stream()
                .filter(view -> ((ImageButton) view).getDrawable().getConstantState().equals(((ImageButton) view_from).getDrawable().getConstantState()))
                .collect(Collectors.toList());

        List<View> firstFourList = foundViews.stream().limit(4)
                .filter(view -> ((ImageButton) view).getDrawable().getConstantState().equals(((ImageButton) view_from).getDrawable().getConstantState()))
                .collect(Collectors.toList());

        List<View> lastFourList = foundViews.stream().skip(Math.max(0, foundViews.size() - 4))
                .filter(view -> ((ImageButton) view).getDrawable().getConstantState().equals(((ImageButton) view_from).getDrawable().getConstantState()))
                .collect(Collectors.toList());

        List<View> firstThreeList = foundViews.stream().limit(3)
                .filter(view -> ((ImageButton) view).getDrawable().getConstantState().equals(((ImageButton) view_from).getDrawable().getConstantState()))
                .collect(Collectors.toList());

        List<View> lastThreeList = foundViews.stream().skip(Math.max(0, foundViews.size() - 3))
                .filter(view -> ((ImageButton) view).getDrawable().getConstantState().equals(((ImageButton) view_from).getDrawable().getConstantState()))
                .collect(Collectors.toList());

        List<View> centerThreeList = foundViews.stream()
                .skip(Math.max(0, foundViews.size() - 4))
                .limit(3)
                .filter(view -> ((ImageButton) view).getDrawable().getConstantState().equals(((ImageButton) view_from).getDrawable().getConstantState()))
                .collect(Collectors.toList());

        if (fiveList.size() == 5) {
            fiveList.forEach(view -> {
                int color = getResources().getColor(R.color.FloralWhite);
                ((ImageButton) view).setBackgroundColor(color);
            });
        } else {
            if (firstFourList.size() == 4) {
                firstFourList.forEach(view -> {
                    int color = getResources().getColor(R.color.LemonChiffon);
                    ((ImageButton) view).setBackgroundColor(color);
                });
            } else {
                if(lastFourList.size() == 4) {
                    lastFourList.forEach(view -> {
                        int color = getResources().getColor(R.color.Snow);
                        ((ImageButton) view).setBackgroundColor(color);
                    });
                } else {
                    if (centerThreeList.size() == 3) {
                        centerThreeList.forEach(view -> {
                            int color = getResources().getColor(R.color.Yellow);
                            ((ImageButton) view).setBackgroundColor(color);
                        });
                    } else if (firstThreeList.size() == 3) {
                        firstThreeList.forEach(view -> {
                            int color = getResources().getColor(R.color.LightYellow);
                            ((ImageButton) view).setBackgroundColor(color);
                        });
                    } else if (lastThreeList.size() == 3) {
                        lastThreeList.forEach(view -> {
                            int color = getResources().getColor(R.color.blackOverlay);
                            ((ImageButton) view).setBackgroundColor(color);
                        });
                    }
                }
            }
        }

    }

    private void setImageButtonXPositions(ImageButton imageButton_from, ImageButton imageButton_to) {
        Integer xPositionFrom = imageButton_from.getxPosition();
        Integer xPositionTo = imageButton_to.getxPosition();

        imageButton_from.setxPosition(xPositionTo);
        imageButton_to.setxPosition(xPositionFrom);
    }

    private void setImageButtonYPositions(ImageButton imageButton_from, ImageButton imageButton_to) {
        Integer yPositionFrom = imageButton_from.getyPosition();
        Integer yPositionTo = imageButton_to.getyPosition();

        imageButton_from.setyPosition(yPositionTo);
        imageButton_to.setyPosition(yPositionFrom);
    }

    private void setViewYPositions(View view_from, View view_to) {
        float fromY = view_from.getY();
        float toY = view_to.getY();

        view_from.setY(toY);
        view_to.setY(fromY);
    }

    private void setViewXPositions(View view_from, View view_to) {
        float fromX = view_from.getX();
        float toX = view_to.getX();

        view_from.setX(toX);
        view_to.setX(fromX);
    }

    private Boolean checkToPosition(Integer toPosition) {
        return toPosition > -1 && toPosition < 9;
    }

    private ImageButton prepareImageButton(Context context, Integer xPosition, Integer yPosition) {
        ImageButton imageButton = new ImageButton(context);

        imageButton.setxPosition(xPosition);
        imageButton.setyPosition(yPosition);
        imageButton.setImageDrawable(getRandomlyImage());
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setLayoutParams(prepareLayoutParams());

        return imageButton;
    }

    private Drawable getRandomlyImage() {
        List<Drawable> list = new ArrayList<>();
        list.add(getResources().getDrawable(R.drawable.flower));
        list.add(getResources().getDrawable(R.drawable.sun));
        list.add(getResources().getDrawable(R.drawable.umbrella));

        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    private LinearLayout.LayoutParams prepareLayoutParams() {
        return new LinearLayout.LayoutParams(WIDTH, HEIGHT);
    }

}
