package cn.ycl.com.lolview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yechenglong on 2017/4/19.
 */

public class LolMoneyView extends View{
    private List<MoneyBean> winList;//胜利方数据集合
    private List<MoneyBean> loseList;//
    private List<Integer> winHeightList;
    private List<Integer> loseHeightList;
     private String sectionTime;
    private String sectionMoney;
    private String maxMoney;
    private Paint winPaint;//胜利方画笔
    private Paint losePaint;
    private Paint linePaint;
    private Paint textPaint;
    private int mTableHeight;
    private int mWidth;
    private int mHeight;
    private int rowHeight;
    private int rowWidth;
    private static final int LINE_HEIGHT_HEAD=20;
    private static final int  lineMarginLeft=50;
    private Bitmap mBitmap;
    private Context mContext;
    private PorterDuffXfermode xferMode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
    private PorterDuffXfermode xferMode2 = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
    public LolMoneyView(Context context) {
        this(context,null);
    }

    public LolMoneyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LolMoneyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDatas();
        mContext = context;
        winPaint = new Paint();
        winPaint.setAntiAlias(true);
        winPaint.setStrokeWidth(5);
        winPaint.setColor(Color.parseColor("#FF50FF40"));
        losePaint  = new Paint();
        losePaint.setAntiAlias(true);
        losePaint.setStrokeWidth(5);
        losePaint.setColor(Color.parseColor("#FFF44336"));
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#9A9FA6"));
        linePaint.setStyle(Paint.Style.STROKE);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dp2px(10));
        textPaint.setColor(Color.parseColor("#444444"));
        Drawable drawable = getResources().getDrawable(R.drawable.red_circle);
        drawableToBitamp(drawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode==MeasureSpec.AT_MOST){
//            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            widthSize =dp2px(300);
        }
        if (heightMode ==MeasureSpec.AT_MOST){
            heightSize = dp2px(200);
        }
        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight = h;
        rowHeight = h/9;
        mTableHeight = rowHeight*8+LINE_HEIGHT_HEAD;
        rowWidth = (w-lineMarginLeft)/4;
        winHeightList = new ArrayList<>();
        winHeightList.add(mTableHeight);
        loseHeightList = new ArrayList<>();
        loseHeightList.add(mTableHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0;i<9;i++){
            canvas.drawLine(lineMarginLeft,i*rowHeight+LINE_HEIGHT_HEAD,mWidth,i*rowHeight+LINE_HEIGHT_HEAD,linePaint);
            if (i%2==0){
                int j= i/2;
                canvas.drawText(21.4f*j+"k",0,mTableHeight-i*rowHeight+dp2px(5),textPaint);
            }
        }
        for (int i=0;i<4;i++){

            canvas.drawLine(i*rowWidth+lineMarginLeft,0,i*rowWidth+lineMarginLeft,mTableHeight,linePaint);
            //winPaint.setXfermode(xferMode2);
            canvas.drawLine(lineMarginLeft+i*rowWidth,winHeightList.get(i),(i+1)*rowWidth+lineMarginLeft,getListHeight(winList,i,winHeightList),winPaint);
            int sc = canvas.saveLayer((i+1)*rowWidth+lineMarginLeft-dp2px(5),winHeightList.get(i+1)-dp2px(5),(i+1)*rowWidth+lineMarginLeft+dp2px(5),winHeightList.get(i+1)+dp2px(5), null, Canvas.ALL_SAVE_FLAG);
            winPaint.setXfermode(xferMode);
            canvas.drawBitmap(mBitmap,(i+1)*rowWidth+lineMarginLeft-dp2px(5),winHeightList.get(i+1)-dp2px(5),winPaint);
            winPaint.setXfermode(null);
            canvas.restoreToCount(sc);
            canvas.drawLine(lineMarginLeft+i*rowWidth,loseHeightList.get(i),(i+1)*rowWidth+lineMarginLeft,getListHeight(loseList,i,loseHeightList),losePaint);
            if (i<3)
            canvas.drawText(winList.get(i).getTime(),(i+1)*rowWidth+lineMarginLeft-dp2px(5)*2,mHeight-dp2px(5),textPaint);
        }
    }
    private void initDatas(){
        winList = new ArrayList<>();
        winList.add(new MoneyBean("8978","8:15"));
        winList.add(new MoneyBean("44799","16:30"));
        winList.add(new MoneyBean("49846","24:45"));
        winList.add(new MoneyBean("74821","34:00"));
        loseList = new ArrayList<>();
        loseList.add(new MoneyBean("7970","8:15"));
        loseList.add(new MoneyBean("37863","16:30"));
        loseList.add(new MoneyBean("39730","24:45"));
        loseList.add(new MoneyBean("55806","34:00"));
    }
    private int dp2px(int dp){
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(scale*dp+0.5f);
    }
    private int getListHeight(List<MoneyBean> list,int i,List<Integer> heighList){
        String money = list.get(i).getMoney();
        int moneyInt = Integer.parseInt(money);
        float percent = moneyInt*100/(21400*4);
        int height = (int)(mTableHeight-LINE_HEIGHT_HEAD-mTableHeight*(percent/100.0f));
        heighList.add(i+1,height);
        return height;
    }
    private void drawableToBitamp(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        mBitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(mBitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
    }
}
