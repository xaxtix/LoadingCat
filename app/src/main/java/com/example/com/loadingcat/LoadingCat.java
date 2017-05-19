package com.example.com.loadingcat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class LoadingCat extends View {

    private final static int HEAD_LENGTH = 60;
    Rect drawingRect;
    RectF drawingRectF;
    RectF internalRectF;
    RectF internalLeftRectF;
    RectF internalRightRectF;

    Paint strokePaint;
    Paint fillPaint;

    Paint pawFill;
    Paint pawStroke;

    Matrix rotateMatrix;

    float alph = 100;
    boolean inc;

    public LoadingCat(Context context) {
        super(context);
        init(context, null);
    }


    public LoadingCat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingCat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        drawingRect = new Rect();
        drawingRectF = new RectF();
        internalRectF = new RectF();
        internalLeftRectF = new RectF();
        internalRightRectF = new RectF();

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(ContextCompat.getColor(context, R.color.strokeColor));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(4);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.YELLOW);

        pawStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        pawStroke.setColor(ContextCompat.getColor(context, R.color.strokeColor));
        pawStroke.setStyle(Paint.Style.STROKE);
        pawStroke.setStrokeWidth(64);
        pawStroke.setStrokeCap(Paint.Cap.ROUND);
        pawStroke.setStrokeJoin(Paint.Join.ROUND);

        rotateMatrix = new Matrix();
        setLayerType(LAYER_TYPE_HARDWARE,null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeHalf = Math.min(getMeasuredHeight(), getMeasuredWidth()) >> 1;
        int sizeQuad = (int) (sizeHalf * 0.4f);
        int cX = getMeasuredWidth() >> 1;
        int cY = getMeasuredHeight() >> 1;
        int padding = (int) (strokePaint.getStrokeWidth() * 0.5f);
        float strokeHalf = pawStroke.getStrokeWidth() / 2 - strokePaint.getStrokeWidth() / 2;

        drawingRect.set(
                cX - sizeHalf + padding,
                cY - sizeHalf + padding,
                cX + sizeHalf - padding,
                cY + sizeHalf - padding
        );

        drawingRectF.set(drawingRect);
        internalRectF.set(
                drawingRect.left + sizeQuad,
                drawingRect.top + sizeQuad,
                drawingRect.right - sizeQuad,
                drawingRect.bottom - sizeQuad
        );

        int earSize = ((int) (drawingRectF.bottom - internalRectF.bottom)) / 4;

        internalLeftRectF.set(
                internalRectF.left - strokeHalf,
                internalRectF.top - strokeHalf,
                internalRectF.right + strokeHalf,
                internalRectF.bottom + strokeHalf
        );

        internalRightRectF.set(
                drawingRectF.left + strokeHalf,
                drawingRectF.top + strokeHalf,
                drawingRectF.right - strokeHalf,
                drawingRectF.bottom - strokeHalf
        );
    }

    int i = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int pX = drawingRect.centerX();
        int pY = drawingRect.centerY();
        int sizeHalf = drawingRect.height() >> 1;
        int internalSizeHalf = ((int) internalRectF.height()) >> 1;
        int leftSizeHalf = ((int) internalLeftRectF.height()) >> 1;
        int rightSizeHalf = ((int) internalRightRectF.height()) >> 1;


        int earSize = ((int) (drawingRectF.bottom - internalRectF.bottom)) / 4;
        float interanlAr = (float) Math.asin(Math.sin(Math.toRadians(7) * sizeHalf / internalSizeHalf));
        float interanlA = (float) Math.toDegrees(interanlAr);

        float leftAr = (float) Math.asin(Math.sin(Math.toRadians(7) * sizeHalf / leftSizeHalf));
        float leftA = (float) Math.toDegrees(leftAr);

        float rightAr = (float) Math.asin(Math.sin(Math.toRadians(7) * sizeHalf / rightSizeHalf));
        float rightA = (float) Math.toDegrees(rightAr);


        if (inc) {
            alph += 3;
            if (alph > 300)
                inc = false;
        } else {
            alph -= 3;
            if (alph < 60)
                inc = true;
        }

        canvas.rotate(i-=5,pX,pY);
        canvas.save();

        rotateMatrix.setTranslate(0, 40);
        Point point = new Point(pX + sizeHalf - earSize, pY);

        //head
        Path path = new Path();
        path.arcTo(drawingRectF, HEAD_LENGTH, -HEAD_LENGTH);
        transformPoint(point, rotateMatrix);
        path.lineTo(point.x, point.y);
        point.set(pX + internalSizeHalf + earSize, pY);
        transformPoint(point, rotateMatrix);
        path.lineTo(point.x, point.y);
        path.arcTo(internalRectF, 0, HEAD_LENGTH - 7 + interanlA);
        canvas.drawPath(path,strokePaint);
        path.close();
        canvas.drawPath(path,fillPaint);

        canvas.drawArc(internalLeftRectF,HEAD_LENGTH,alph,false,pawStroke);
        canvas.drawArc(internalRightRectF,HEAD_LENGTH,alph,false,pawStroke);

        //


        float offset = (float) (sizeHalf - sizeHalf * Math.cos(Math.toRadians(7)));
        float offset1 = (float) (internalSizeHalf - internalSizeHalf * Math.cos(interanlAr));

        point.set((int) (pX + leftSizeHalf - offset), pY);
        rotateMatrix.setRotate(alph - 7 + leftA, pX, pY);
        transformPoint(point, rotateMatrix);
        path.cubicTo(point.x, point.y,point.x - 50, point.y,point.x, point.y);
        path.arcTo(internalLeftRectF, alph - 7 + leftA, -2 * leftA);

        point.set((int) (pX + rightSizeHalf - offset1), pY);

        rotateMatrix.setRotate(alph - 7 -rightA, pX, pY);
        transformPoint(point, rotateMatrix);
        path.lineTo(point.x, point.y);
        path.arcTo(internalRightRectF,alph - 7 - rightA, 2 * rightA);
        path.close();

      //  canvas.drawPath(path, strokePaint);

      //    invalidate();
        //canvas.drawCircle(pX, pY, sizeHalf, strokePaint);
    }

    public void transformPoint(Point point, Matrix matrix) {
        float[] pts = new float[2];

        pts[0] = point.x;
        pts[1] = point.y;

        matrix.mapPoints(pts);

        point.set((int) pts[0], (int) pts[1]);
    }


}
