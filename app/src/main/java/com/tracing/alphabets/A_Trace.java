package com.tracing.alphabets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class A_Trace extends AppCompatActivity {

    TextView msg;
    //TouchView touchView;
    DrawingView dv ;
    private Paint mPaint;
    TextView textInfo,textSource;
    ImageView imageResult;

    Bitmap bitmapMaster;
    Canvas canvasMaster;
    Uri source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__trace);

        textSource = (TextView)findViewById(R.id.sourceuri);
        imageResult = (ImageView)findViewById(R.id.result);

        startService(new Intent(getBaseContext(),MyService.class));

        RelativeLayout mtile=(RelativeLayout)findViewById(R.id.tracea);
        dv = new DrawingView(this);
        mtile.addView(dv);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10f);
    }

    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;
        int colour;
        int reset = 0;
        int count = 0;
        int check = 0;
        int start_outside = 0;
        int start_inside = 0;
        int N = 11;
        int count_complete = 0;
        int set = 0;
        int z = 0;
        int clear = 0;

        int flag[][] = new int[N][3];

        float boxes[][] = new float[9][4];

        public void resetFlags()
        {
            flag[0][1] = 1;
            for(int i =1;i<N;i++)
            {
                flag[i][1] = 0;
            }

            flag[0][2] = -1;
            flag[1][2] = 8;
            flag[2][2] = -1;
            flag[3][2] = -1;
            flag[4][2] = -1;
            flag[5][2] = -1;
            flag[6][2] = 10;
            flag[7][2] = -1;
            flag[8][2] = -1;
            flag[9][2] = -1;
            flag[10][2] = -1;

        }

        public void setTracingOrder()
        {
            flag[0][0] = 0;
            flag[1][0] = 1;
            flag[2][0] = 2;
            flag[3][0] = 3;
            flag[4][0] = 4;
            flag[5][0] = 5;
            flag[6][0] = 6;
            flag[7][0] = 7;
            flag[8][0] = 1;
            flag[9][0] = 8;
            flag[10][0] = 6;

        }

        public void setBoxes()
        {
            boxes[0][0] = 0.0f;
            boxes[0][1] = 245.0f;
            boxes[0][2] = 483.0f;
            boxes[0][3] = 666.0f;

            boxes[1][0] = 0.0f;
            boxes[1][1] = 245.0f;
            boxes[1][2] = 377.0f;
            boxes[1][3] = 483.0f;

            boxes[2][0] = 0.0f;
            boxes[2][1] = 335.0f;
            boxes[2][2] = 255.0f;
            boxes[2][3] = 377.0f;

            boxes[3][0] = 0.0f;
            boxes[3][1] = 335.0f;
            boxes[3][2] = 0.0f;
            boxes[3][3] = 255.0f;

            boxes[4][0] = 335.0f;
            boxes[4][1] = 1170.0f;
            boxes[4][2] = 0.0f;
            boxes[4][3] = 255.0f;

            boxes[5][0] = 335.0f;
            boxes[5][1] = 1170.0f;
            boxes[5][2] = 255.0f;
            boxes[5][3] = 377.0f;

            boxes[6][0] = 421.0f;
            boxes[6][1] = 1170.0f;
            boxes[6][2] = 377.0f;
            boxes[6][3] = 483.0f;

            boxes[7][0] = 421.0f;
            boxes[7][1] = 1170.0f;
            boxes[7][2] = 483.0f;
            boxes[7][3] = 666.0f;

          /*boxes[8][0] = 0.0f;
          boxes[8][1] = 245.0f;
          boxes[8][2] = 377.0f;
          boxes[8][3] = 483.0f;*/

            boxes[8][0] = 245.0f;
            boxes[8][1] = 421.0f;
            boxes[8][2] = 377.0f;
            boxes[8][3] = 666.0f;

          /*boxes[10][0] = 421.0f;
          boxes[10][1] = 645.0f;
          boxes[10][2] = 377.0f;
          boxes[10][3] = 483.0f;*/

        }


        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
            setBoxes();
            resetFlags();
            setTracingOrder();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        public float mX, mY;
        public static final float TOUCH_TOLERANCE = 4;

        public void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            Log.d("start xy==>", x + "," + y);
        }
        public void touch_move(float x, float y) {
            Log.d("move xy==>", x+","+y);
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if ((dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }
        public void touch_up() {
            mPath.lineTo(mX, mY);
            Log.d("end xy", mX+","+mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        private int getProjectedColor(ImageView iv, Bitmap bm, int x, int y){

            int projectedX = (int)((double)x * ((double)bm.getWidth()/(double)iv.getWidth()));
            int projectedY = (int)((double)y * ((double)bm.getHeight()/(double)iv.getHeight()));

            /*textSource.setText(x + ":" + y + "/" + iv.getWidth() + " : " + iv.getHeight() + "\n" +
                            projectedX + " : " + projectedY + "/" + bm.getWidth() + " : " + bm.getHeight()
            );*/

            return bm.getPixel(projectedX, projectedY);
        }

        public boolean insideBoundary(int color) {
            if (color != 0) //inside boundary
                return true;
            else
                return false; //outside boundary color = 0 impales transparent
        }

        public void clearScreen()
        {
                /*mPath.reset();
                circlePath.reset();
                mCanvas.drawColor(0);
                postInvalidate();*/

            resetFlags();
            count_complete = 0;
            z = 0;
            count = 0;
            Paint paint1 = new Paint();
            paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            Rect rect = new Rect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
            mCanvas.drawRect(rect, paint1);
            //Toast.makeText(getApplicationContext(), "Trace Again", Toast.LENGTH_SHORT).show();
        }

        public void imageCache() {
            /*imageResult.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            imageResult.layout(0, 0, imageResult.getMeasuredWidth(), imageResult.getMeasuredHeight());*/


            imageResult.buildDrawingCache();
            bitmapMaster = imageResult.getDrawingCache();
        }

        public int findBox(float x, float y)
        {
            for(int i =0;i<9;i++)
            {
                if(x >= boxes[i][0] && x< boxes[i][1] && y >= boxes[i][2] && y< boxes[i][3])
                    return (i);

            }

            return (50);
        }
        @Override

        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    textSource.setText("ACTION_DOWN- " + x + " : " + y);
                    imageCache();
                    colour = getProjectedColor(imageResult, bitmapMaster,(int) x,(int) y);
                    textSource.setText("color is " + colour + "box is" +findBox(x,y));
                    textSource.setBackgroundColor(colour);

                    if(insideBoundary(colour)) {
                        textSource.setText("color is " + colour + "box is" +findBox(x,y));
                        textSource.setBackgroundColor(colour);
                        start_inside = 1;
                    }


                    else {
                        start_outside = 1;
                        start_inside = 0;
                    }

                    if(colour != -59111){
                        count++;
                    }

                    if(flag[z][0] != findBox(x,y)){
                        clear = 1;
                    }

                    if(findBox(x,y)!= flag[z][0] && flag[findBox(x,y)][1] == 0)
                    {
                        clear = 1;
                    }


                    if(flag[z][0] == findBox(x,y) && flag[z][1] == 1) {
                        flag[z][1] = 2;
                        if(z<10) {
                            flag[z + 1][1] = 1;
                            z++;
                        }
                    }

                    /*else{
                         clear = 1;
                    }*/


                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    textSource.setText("ACTION_MOVE- " + x + " : " + y);
                    imageCache();
                    colour = getProjectedColor(imageResult, bitmapMaster,(int) x,(int) y);
                    textSource.setText("color is " + colour+ "box is" +findBox(x,y));
                    textSource.setBackgroundColor(colour);

                    if(insideBoundary(colour)) {
                        textSource.setText("color is " + colour + "box is" +findBox(x,y));
                        textSource.setBackgroundColor(colour);
                    }

                    if (colour == -59111)
                        reset = 0;

                    if (reset == 0) {
                        if (colour != -59111) {//if (colour == -14034428)
                            reset = 1;
                            count++;
                        }
                    }

                    if(!insideBoundary(colour))
                        clear = 1;

                    if(findBox(x,y)!= flag[z][0] && flag[findBox(x,y)][1] == 0)
                    {
                        clear = 1;
                    }


                    if(flag[z][0] == findBox(x,y) && flag[z][1] == 1) {
                        flag[z][1] = 2;
                        if(z<10) {
                            flag[z + 1][1] = 1;
                            z++;
                        }
                    }

                    /*else{
                        clear = 1;
                    }*/

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    textSource.setText("ACTION_UP- " + x + " : " + y);
                    imageCache();
                    colour = getProjectedColor(imageResult, bitmapMaster,(int) x,(int) y);
                    textSource.setText("color is " + colour + "box is" +findBox(x,y));
                    textSource.setBackgroundColor(colour);

                    /*if(insideBoundary(colour)) {
                        textSource.setText("color is " + colour);
                        textSource.setBackgroundColor(colour);
                    }*/

                    if((insideBoundary(colour) && start_outside == 1) ||!insideBoundary(colour) )
                    {
                        clearScreen();
                        start_outside = 0;
                        start_inside = 0;
                    }

                    if(clear == 1)
                    {
                        clearScreen();
                        clear = 0;
                    }

                    reset = 0;

                    count_complete = 0;
                    for(int i=0;i<11;i++)
                    {
                        if(flag[i][1] == 2)
                            count_complete++;
                    }

                    if(count_complete == 11) {
                        //Toast.makeText(getApplicationContext(), "Congratulations", Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(A_Trace.this, B_Trace.class);
                        SharedPreferences access = getSharedPreferences("access file",MODE_PRIVATE);
                        SharedPreferences.Editor edit = access.edit();
                        edit.putInt("b",1);
                        edit.commit();
                        startActivity(b);
                        finish();
                    }

                    invalidate();
                    break;
            }
            return true;
        }
    }

    public void updateMsg(String tMsg, int color){
        msg.setTextColor(color);
        msg.setText(tMsg+ "color is"+ color);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(A_Trace.this, TraingSplash.class));
        finish();

    }

}
