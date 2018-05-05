package ru.prsolution.winstrike.ui.common;
/*
 * Created by oleg on 01.02.2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ChooseSeatLinearLayout extends LinearLayout {
//    private Float scale = CarouselSeatAdapter.BIG_SCALE;
    private Float scale = 0f;

    public ChooseSeatLinearLayout(Context context) {
        super(context);
    }

    public ChooseSeatLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setScaleBoth(Float scale) {
        this.scale = scale;
        this.invalidate();    // If you want to see the scale every time you set
        // scale you need to have this line here,
        // invalidate() function will call onDraw(Canvas)
        // to redraw the view for you
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        canvas.scale(scale, scale, (float) (w / 2), (float) (h / 2));
        super.onDraw(canvas);
    }
}
