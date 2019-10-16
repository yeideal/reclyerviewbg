package com.leaf.reclyerviewbg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;


/**
 * Created by ye on 2018/1/3.
 */

public class BgDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "recycler-bg";
    private Bitmap bmp;
    private Paint paint;
    private Rect srcRect;
    private Rect desRect;
    private int bmpHeight;
    private int bmWidth;
    SparseArray<Integer> sparseArray;
    private int defaultRes;
    private boolean isDefaultBm = false;
    private Context context;


    public BgDecoration(Context context, int defaultRes) {
        this.context = context;
        this.defaultRes = defaultRes;
        restoreDefaultBm();
        paint = new Paint();
        srcRect = new Rect();
        desRect = new Rect();
        sparseArray = new SparseArray<>();
    }

    private Bitmap getBitmap(int vectorDrawableId) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    public void setBmp(Bitmap bmp) {
        if (this.bmp != null) {
            this.bmp.recycle();
            this.bmp = null;
        }
        bmpHeight = bmp.getHeight();
        bmWidth = bmp.getWidth();
        this.bmp = bmp;
        isDefaultBm = false;
    }


    public synchronized void clearMap() {
        sparseArray.clear();
    }

    public synchronized void restoreDefaultBm() {
        if (!isDefaultBm) {
            if (this.bmp != null) {
                this.bmp.recycle();
                this.bmp = null;
            }
            this.bmp = getBitmap(defaultRes);
            bmpHeight = bmp.getHeight();
            bmWidth = bmp.getWidth();
            isDefaultBm = true;
        }
    }

    private Integer firstTop;
    private int childCount;
    private View firstView;
    private View preView;
    private View nowView;
    private Integer lastScrollY;
    private Integer nowScrollY;

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        childCount = parent.getChildCount();
        if (childCount == 0) {
            firstTop = 0;
        } else {
            firstView = parent.getChildAt(0);
            int position = parent.getChildAdapterPosition(firstView);
            if (sparseArray.size() == 0) {
                firstTop = 0;
                sparseArray.put(position, 0);
            } else {
                firstTop = sparseArray.get(position);
            }
            if (firstTop != null) {
                Integer preScrollY = firstTop;
                for (int index = 1, nowPos = position + 1; index < childCount; index++, nowPos++) {
                    Integer nowScroll = sparseArray.get(nowPos);
                    if (nowScroll == null) {
                        preView = parent.getChildAt(index - 1);
                        if (preView == null) {
                            break;
                        }
                        nowScroll = preScrollY + preView.getHeight();
                        sparseArray.put(nowPos, nowScroll);
                    }
                    preScrollY = nowScroll;
                }
            } else {
                int lastIndex = childCount - 1;
                int lastPos = position + lastIndex;
                lastScrollY = sparseArray.get(lastPos);
                for (int index = lastIndex - 1, nowPos = lastPos - 1; index >= 0; index--, nowPos--) {
                    Integer nowScrollY = sparseArray.get(nowPos);
                    if (nowScrollY == null) {
                        if (lastScrollY != null) {
                            nowView = parent.getChildAt(index);
                            if (nowView == null) {
                                break;
                            }
                            nowScrollY = lastScrollY - nowView.getHeight();
                            sparseArray.put(nowPos, nowScrollY);
                        }
                    }
                    lastScrollY = nowScrollY;
                }
                firstTop = sparseArray.get(position);
            }


            if (firstTop == null) {
                firstTop = 0;
            } else {
                firstTop -= firstView.getTop();
            }
        }


        int totalHeight = parent.getHeight();
        int totalWidth = parent.getWidth();

        float screenRate = (float) totalHeight / totalWidth;
        float widthRate = (float) totalWidth / bmWidth;

        int bmShowTotalHeight = Math.round(bmWidth * screenRate);
        int bmStart = Math.round(firstTop / widthRate);
        int bmTotalEnd = bmStart + bmShowTotalHeight;

        int nowStart = bmStart;
        int nowPage = floorDiv(nowStart, bmpHeight);
        int lastPage = floorDiv(bmTotalEnd, bmpHeight);

        int srcStart;
        int srcEnd;
        int desStart = 0;
        int desEnd;
        while (nowPage <= lastPage) {
            int pageEndHeight = (nowPage + 1) * bmpHeight;
            if (bmTotalEnd < pageEndHeight) {//未超出屏幕
                srcStart = floorMod(nowStart, bmpHeight);
                srcEnd = floorMod(bmTotalEnd, bmpHeight);
                desEnd = totalHeight;
                nowStart = bmTotalEnd;
            } else if (bmTotalEnd == pageEndHeight) {
                srcStart = floorMod(nowStart, bmpHeight);
                srcEnd = bmpHeight;
                desEnd = totalHeight;
                nowStart = bmTotalEnd;
            } else {
                srcStart = floorMod(nowStart, bmpHeight);
                srcEnd = bmpHeight;
                desEnd = desStart + (int) ((srcEnd - srcStart) * widthRate);
                nowStart = pageEndHeight;
            }

            srcRect.left = 0;
            srcRect.top = srcStart;
            srcRect.right = bmWidth;
            srcRect.bottom = srcEnd;

            desRect.left = 0;
            desRect.top = desStart;
            desRect.right = totalWidth;
            desRect.bottom = desEnd;
            canvas.drawBitmap(bmp, srcRect, desRect, paint);

            desStart = desEnd;
            nowPage++;
        }

    }

    private int floorDiv(int nowStart, int bmpHeight) {
        return (int) Math.floor((double) nowStart / bmpHeight);
    }

    public int floorMod(int x, int y) {
        int r = x - floorDiv(x, y) * y;
        return r;
    }

}
