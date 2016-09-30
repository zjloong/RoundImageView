package com.loong.roundimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Describe the function of the class
 *
 * @author zhujinlong@ichoice.com
 * @date 2016/9/30
 * @time 11:32
 * @description Describe the place where the class needs to pay attention.
 */
public class RoundImageView extends ImageView {

    private static final int COLORDRAWABLE_DIMENSION = 2;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    public static final int TYPE_CIRCLE = 0;

    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;

    private RoundedBitmapDrawable roundedBitmapDrawable;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        // 默认为Circle
        type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);
        // 默认为10dp
        mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius, dip2px(context,8));
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            if (roundedBitmapDrawable != null || getDrawable() instanceof RoundedBitmapDrawable) {
                roundedBitmapDrawable = null;
            } else {
                Bitmap bm = getBitmapFromDrawable(getDrawable());
                roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bm);
                if(type == TYPE_CIRCLE){
                    roundedBitmapDrawable.setCircular(true);
                }else {
                    roundedBitmapDrawable.setCornerRadius(mBorderRadius);
                }
                setImageDrawable(roundedBitmapDrawable);
            }
            super.onDraw(canvas);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}