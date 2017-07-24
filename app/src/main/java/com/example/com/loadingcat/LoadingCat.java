package com.example.com.loadingcat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class LoadingCat extends View {

    private final static int HEAD_LENGTH = 60;
    private final static int STROKE_WIDTH = 24;
//    private final static int PAW_WIDTH = 64;
    // private final static int PAW_LIGHT_WIDTH = 92;

    Rect drawingRect;
    RectF drawingRectF;
    RectF internalRectF;
    RectF internalLeftRectF;
    RectF internalRightRectF;
    RectF internalMiddleRecrF;

    Paint strokePaint;
    Paint fillPaint;

    Paint bodyPatint;

    Paint pawFill;
    Paint pawStroke;
    Paint pawLightStroke;
    Paint tailStroke;
    Paint mouthStroke;

    Matrix rotateMatrix;

    float alph = 100;
    boolean inc;

    int bodyColor;
    int strokeColor;
    int bodyLightColor;
    int bodyDarkColor;
    Path path;
    Point point;

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
        internalMiddleRecrF = new RectF();

        bodyColor = ContextCompat.getColor(context, R.color.bodyColor);
        bodyLightColor = ContextCompat.getColor(context, R.color.bodyLightColor);
        strokeColor = ContextCompat.getColor(context, R.color.strokeColor);
        bodyDarkColor = ContextCompat.getColor(context, R.color.bodyDarkColor);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(STROKE_WIDTH);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);

        mouthStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        mouthStroke.setColor(strokeColor);
        mouthStroke.setStyle(Paint.Style.STROKE);
        mouthStroke.setStrokeWidth(STROKE_WIDTH  / 3);
        mouthStroke.setStrokeCap(Paint.Cap.ROUND);
        mouthStroke.setStrokeJoin(Paint.Join.ROUND);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(bodyColor);

        pawStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        pawStroke.setColor(strokeColor);
        pawStroke.setStyle(Paint.Style.STROKE);
        pawStroke.setStrokeCap(Paint.Cap.ROUND);
        pawStroke.setStrokeJoin(Paint.Join.ROUND);

        pawFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        pawFill.setColor(bodyColor);
        pawFill.setStyle(Paint.Style.STROKE);

        pawFill.setStrokeCap(Paint.Cap.ROUND);
        pawFill.setStrokeJoin(Paint.Join.ROUND);

        pawLightStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        pawLightStroke.setColor(bodyLightColor);
        pawLightStroke.setStyle(Paint.Style.STROKE);

        pawLightStroke.setStrokeCap(Paint.Cap.ROUND);
        pawLightStroke.setStrokeJoin(Paint.Join.ROUND);

        bodyPatint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bodyPatint.setColor(bodyColor);
        bodyPatint.setStyle(Paint.Style.STROKE);
        bodyPatint.setStrokeCap(Paint.Cap.BUTT);

        tailStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        tailStroke.setColor(bodyDarkColor);
        tailStroke.setStyle(Paint.Style.STROKE);
        tailStroke.setStrokeCap(Paint.Cap.ROUND);
        tailStroke.setStrokeJoin(Paint.Join.ROUND);


        rotateMatrix = new Matrix();
        path = new Path();
        point = new Point();
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeHalf = Math.min(getMeasuredHeight(), getMeasuredWidth()) >> 1;
        int sizeQuad = (int) (sizeHalf * 0.42f);
        int sizeQuadHalf = sizeQuad >> 1;
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

        internalMiddleRecrF.set(
                drawingRect.left + sizeQuadHalf,
                drawingRect.top + sizeQuadHalf,
                drawingRect.right - sizeQuadHalf,
                drawingRect.bottom - sizeQuadHalf
        );


        pawStroke.setStrokeWidth(earSize);
        pawFill.setStrokeWidth(earSize - STROKE_WIDTH);
        tailStroke.setStrokeWidth(earSize - STROKE_WIDTH);
        pawLightStroke.setStrokeWidth(sizeQuadHalf);
        bodyPatint.setStrokeWidth(sizeQuadHalf + STROKE_WIDTH * 2.2f);
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
            alph += 1;
            if (alph > 170)
                inc = false;
        } else {
            alph -= 1;
            if (alph < 60)
                inc = true;
        }

        canvas.rotate(i -= 2, pX, pY);
        canvas.save();

        rotateMatrix.setTranslate(0, 40);
        point.set(pX + sizeHalf - earSize, pY);

        //head

        path.arcTo(drawingRectF, HEAD_LENGTH, -HEAD_LENGTH);
        transformPoint(point, rotateMatrix);
        path.lineTo(point.x, point.y);
        point.set(pX + internalSizeHalf + earSize, pY);
        transformPoint(point, rotateMatrix);
        path.lineTo(point.x, point.y);
        path.arcTo(internalRectF, 0, HEAD_LENGTH - 7 + interanlA);
        canvas.drawPath(path, strokePaint);
        path.close();
        canvas.drawPath(path, fillPaint);

        //tail
        canvas.drawArc(internalMiddleRecrF, HEAD_LENGTH + alph - 30, 40, false, pawStroke);
        canvas.drawArc(internalMiddleRecrF, HEAD_LENGTH + alph - 30, 40, false, tailStroke);
        //back paws
        canvas.drawArc(internalLeftRectF, HEAD_LENGTH, alph, false, pawStroke);
        canvas.drawArc(internalRightRectF, HEAD_LENGTH, alph, false, pawStroke);

        canvas.drawArc(internalLeftRectF, HEAD_LENGTH - 10, alph + 10, false, pawFill);
        canvas.drawArc(internalRightRectF, HEAD_LENGTH - 10, alph + 10, false, pawFill);

        //body
        canvas.drawArc(internalMiddleRecrF, HEAD_LENGTH - 20, alph + 10, false, bodyPatint);
        canvas.drawArc(internalMiddleRecrF, HEAD_LENGTH - 20, alph - 15, false, pawLightStroke);

        //face paws
        canvas.drawArc(internalLeftRectF, HEAD_LENGTH - 17, 15, false, pawStroke);
        canvas.drawArc(internalRightRectF, HEAD_LENGTH - 17, 15, false, pawStroke);

        canvas.drawArc(internalLeftRectF, HEAD_LENGTH - 17 - 10, 15 + 10, false, pawFill);
        canvas.drawArc(internalRightRectF, HEAD_LENGTH - 17 - 10, 15 + 10, false, pawFill);

        canvas.restore();

        //TODO hardcode
        canvas.translate(0, 70);
        canvas.rotate(3, pX, pY);

        float rightEyeX = pX + sizeHalf - earSize - strokePaint.getStrokeWidth() / 4;
        float leftEyeX = pX + internalSizeHalf + earSize + strokePaint.getStrokeWidth() / 4;
        float distanceEyeX = (rightEyeX - leftEyeX);
        float distanceEyeQuadX = (rightEyeX - leftEyeX) / 4;
        float centerEyeX = leftEyeX + distanceEyeX / 2;

        canvas.drawPoint(leftEyeX, pY, strokePaint);
        canvas.drawPoint(rightEyeX, pY, strokePaint);

        canvas.translate(0, 30);

        path.reset();
        path.moveTo(leftEyeX + distanceEyeQuadX, pY);
        path.quadTo(centerEyeX - distanceEyeQuadX / 2, pY + distanceEyeQuadX * 0.7f, centerEyeX, pY);
        path.quadTo(centerEyeX + distanceEyeQuadX / 2, pY + distanceEyeQuadX * 0.7f, rightEyeX - distanceEyeQuadX, pY);

        canvas.drawPath(path, mouthStroke);
        // canvas.drawPath(path,strokePaint);

        // invalidate();
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
