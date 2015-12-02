package com.example.nataliajastrzebska.urbangame.laby;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nataliajastrzebska on 01/12/15.
 */
public class DirectionsView extends View {

    Direction direction = Direction.NO;
    String msg;

    public DirectionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        drawArrow(canvas);
        if(msg!=null && msg.length() > 0)
            drawMsg(canvas);
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }


    private void drawMsg(Canvas canvas){
        Paint p =  new Paint();
        p.setTextSize(35);
        canvas.drawText(msg,150,50,p);
    }

    private void drawArrow(Canvas canvas){
        Paint p = new Paint();
        p.setStrokeWidth(15);
        canvas.drawLine(10,80,110,80,p);
        if(direction == Direction.LEFT){
            canvas.drawLine(10,80,60,30,p);
            canvas.drawLine(10,80,60,120,p);
        }
        else if(direction == Direction.RIGHT){
            canvas.drawLine(110,80,60,30,p);
            canvas.drawLine(110,80,60,120,p);
        }
        p.setStrokeWidth(7);
        canvas.drawLine(130,0,130,300,p);
    }
}
