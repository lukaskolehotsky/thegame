package com.example.tetris.data;

import android.content.Context;

public class ImageButton extends androidx.appcompat.widget.AppCompatImageButton {

    private Integer xPosition;

    private Integer yPosition;

    public ImageButton(Context context) {
        super(context);

    }

    public Integer getxPosition() {
        return xPosition;
    }

    public void setxPosition(Integer xPosition) {
        this.xPosition = xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public void setyPosition(Integer yPosition) {
        this.yPosition = yPosition;
    }
}
