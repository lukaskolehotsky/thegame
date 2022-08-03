package com.example.tetris;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetris.data.ImageButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private static final Integer WIDTH = 50;
    private static final Integer HEIGHT = 50;
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
                                        Log.d("Test", "Left to Right swipe performed");
                                        Toast.makeText(PlayActivity.this, "Left to Right swipe performed ", Toast.LENGTH_SHORT).show();

                                        ImageButton imageButton_from = (ImageButton) view_from;
                                        moveLeftOrRight(view_from, imageButton_from.getxPosition(), imageButton_from.getyPosition() + 1);
                                    } else {
                                        Log.d("Test", "Right to Left swipe performed");
                                        Toast.makeText(PlayActivity.this, "Right to Left swipe performed ", Toast.LENGTH_SHORT).show();

                                        ImageButton imageButton_from = (ImageButton) view_from;
                                        moveLeftOrRight(view_from, imageButton_from.getxPosition(), imageButton_from.getyPosition() - 1);
                                    }
                                } else {
                                    if ((firstY_point < finalY)) {
                                        Log.d("Test", "Up to Down swipe performed");
                                        Toast.makeText(PlayActivity.this, "Up to Down swipe performed", Toast.LENGTH_SHORT).show();

                                        ImageButton imageButton_from = (ImageButton) view_from;
                                        moveUpOrDown(view_from, imageButton_from.getxPosition() + 1, imageButton_from.getyPosition());

                                    } else {
                                        Log.d("Test", "Down to Up swipe performed");
                                        Toast.makeText(PlayActivity.this, "Down to Up swipe performed", Toast.LENGTH_SHORT).show();

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
        }
    }

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

            List<View> foundViews = findHorizontalRow(gridLayout, view_from, 3);
//            findSimilarViews(foundViews);
        }

    }

    private List<View> findHorizontalRow(GridLayout gridLayout, View view_from_moved, Integer sameOccurences) {
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

                    if (yPosition < yPosition_from_moved + sameOccurences
                            && yPosition > yPosition_from_moved - sameOccurences) {

                        Drawable drawable = imageButton.getDrawable();
                        Drawable drawable_from_moved = imageButton_from_moved.getDrawable();

                        if (drawable.getConstantState().equals(drawable_from_moved.getConstantState())) {
                            view.setBackgroundColor(Color.RED);
                        }
                        view.setBackgroundColor(Color.RED);
                        foundViews.add(view);
                    }
                }
            }
        }

        return foundViews;
    }

    private void findSimilarViews(List<View> foundViews) {
        List<View> sortedList = new LinkedList<>();

        int pp = 0;
        do {

            for (View view : foundViews) {
                ImageButton imgbtn = (ImageButton) view;

                if (imgbtn.getyPosition().equals(pp)) {
                    sortedList.add(view);
                    pp++;
                }
            }


//            for (int j = 0; j < foundViews.size(); j++) {
//                ImageButton imgbtn = (ImageButton) foundViews.get(j);
//                if (imgbtn.getyPosition().equals(pp)) {
//                    sortedList.add(view);
//                    pp++;
//                }
//            }
        } while (pp < foundViews.size());


        // find left 3
        List<View> leftThreeList = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            leftThreeList.add(sortedList.get(j));
            sortedList.get(j).setBackgroundColor(Color.RED);
        }

        // find right 3
//            List<View> rightThreeList = new ArrayList<>();
//            for (int j = foundViews.size(); j > 2; j --) {
//                rightThreeList.add(foundViews.get(j));
//            }

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
