package com.srainbow.leisureten.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.srainbow.leisureten.R;

import java.lang.ref.WeakReference;

/**
 * Created by SRainbow on 2017/3/29.
 */

public class RectangleImageView extends AppCompatImageView {

    private Context mContext;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;
    private WeakReference<Bitmap> mWeakBitmap;
    private int type;//图片类型
    private String mText;//显示文字
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private static final int BODER_RADIUS_DEFAULT = 15; //圆角大小默认值
    private int mBorderRadius;
    private boolean isText = false;
    public RectangleImageView(Context context) {
        this(context, null);
        initPaint();
    }

    public RectangleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RectangleImageView);
        mBorderRadius = a.getDimensionPixelSize(R.styleable.RectangleImageView_borderRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BODER_RADIUS_DEFAULT,
                        getResources().getDisplayMetrics()));//默认为10dp
        type = a.getInt(R.styleable.RectangleImageView_type, TYPE_ROUND);//默认为圆角
        mText = a.getString(R.styleable.RectangleImageView_imgText);
        a.recycle();
    }

    public void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果是圆形，重新设置大小
        if(type == TYPE_CIRCLE){
            int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = mWeakBitmap == null ? null : mWeakBitmap.get();
        if(bitmap == null || bitmap.isRecycled()){
            Drawable drawable = getDrawable();
            if(drawable != null){
                int dWidth = drawable.getIntrinsicWidth();
                int dHeight = drawable.getIntrinsicHeight();
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                float scale = 1.0f;
                Canvas drawCanvas = new Canvas(bitmap);
                //计算缩放比例保证图片不失真
                if(type == TYPE_ROUND){
                    scale = Math.max(getWidth() * 1.0f / dWidth, getHeight() * 1.0f / dHeight);
                }else{
                    scale = getWidth() * 1.0f / Math.min(dWidth, dHeight);
                }
                drawable.setBounds(0, 0, (int)(scale * dWidth), (int)(scale * dHeight));
                drawable.draw(drawCanvas);
                if(mMaskBitmap == null || mMaskBitmap.isRecycled()){
                    mMaskBitmap = getBitmap();
                }
                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(mXfermode);
                //开始形状
                drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
                mPaint.setXfermode(null);
                //绘制bitmap
                canvas.drawBitmap(bitmap, 0, 0, null);

                //如果在加上下面一行代码时，使用Picasso加载图片时会在图片上出现一层朦胧的效果
//                //缓存b;pitmap， 避免每次调用onDraw，分配内存
//                mWeakBitmap = new WeakReference<Bitmap>(bitmap);
            }

        }
        if(bitmap != null){
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
        }
        if(mText != null && !mText.equals("")){
            mTextPaint.setTextSize(bitmap.getWidth() / 5);
            int mWidth = bitmap.getWidth();
            int mHeight = bitmap.getHeight();
            int baseX = (int) (mWidth / 2 - mTextPaint.measureText(mText) / 2);
            int baseY = (int) ((mHeight / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
            RectF rectF = new RectF(mWidth / 9, mHeight / 5, mWidth * 8 / 9, mHeight *4 / 5);
            mTextPaint.setColor(Color.parseColor("#d7ccc8"));
            mTextPaint.setAlpha(145);
            canvas.drawRoundRect(rectF, 30, 30, mTextPaint);
            mTextPaint.setColor(Color.parseColor("#795548"));
            mTextPaint.setAlpha(200);
            canvas.drawText(mText, baseX, baseY, mTextPaint);
        }
    }

    //绘制形状
    public Bitmap  getBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        if(type == TYPE_ROUND){
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mBorderRadius, mBorderRadius, paint);
        }else{
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);
        }
        return bitmap;
    }

    @Override
    public void invalidate() {
          mWeakBitmap = null;
        if(mMaskBitmap != null){
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
    }

    public void setImgText(String text){
        this.mText = text;
        invalidate();
    }

    public String getImgText(){
        return this.mText;
    }
}
