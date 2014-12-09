package edu.oakland.racetracker;

import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
/**
 * 
 * @author Lukas Greib
 * Creates a surface to draw camera output on
 */
public class AvatarSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

	private Camera mCamera;
	SurfaceHolder surface_holder = null;
	private Canvas mCanvas;
	private Context mContext;
	
	private boolean cameraRunning = false;
	
	public Bitmap out = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
	
	public boolean isCameraRunning(){
		return cameraRunning;
	}
	
	public AvatarSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }
	
	public AvatarSurfaceView(Context context) {
		super(context);
		mContext = context;
		setup();
	}
	
	private void setup(){
		setDrawingCacheEnabled(true);
		measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
		if (surface_holder == null) {
            surface_holder = getHolder();
        }
		surface_holder.addCallback(this);
	}
	
	public void startCamera(){
		mCamera = Camera.open();
        try {
        	Camera.Parameters parameters = mCamera.getParameters();
            Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            if(display.getRotation() == Surface.ROTATION_0)
            {
                //parameters.setPreviewSize(height, width);                           
                mCamera.setDisplayOrientation(90);
            }

            if(display.getRotation() == Surface.ROTATION_90)
            {
                //parameters.setPreviewSize(width, height);                           
            }

            if(display.getRotation() == Surface.ROTATION_180)
            {
                //parameters.setPreviewSize(height, width);               
            }

            if(display.getRotation() == Surface.ROTATION_270)
            {
                //parameters.setPreviewSize(width, height);
                mCamera.setDisplayOrientation(180);
            }
            mCamera.setParameters(parameters);
             mCamera.setPreviewDisplay(surface_holder);  
             mCamera.startPreview();
             cameraRunning = true;
        } catch (IOException exception) {  
              mCamera.release();  
              mCamera = null;
        }
	}
	
	@SuppressLint("WrongCall")
	public void stopCamera(){
		mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        cameraRunning = false;
        //RaceTrackerApp.mProfile.avatar = getDrawingCache();
        this.onDraw(mCanvas);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas){
		mCanvas = canvas;
		super.onDraw(canvas);
		Bitmap bmp;
		if(true/*RaceTrackerApp.mCurrentTrack == null*/){
			DisplayMetrics metrics = new DisplayMetrics();
			float logicalDensity = metrics.density;
			int px = (int) Math.ceil(this.getWidth() * logicalDensity);
			int py = (int) Math.ceil(this.getHeight() * logicalDensity);
		  bmp = BitmapFactory.decodeResource(getResources(), R.drawable.squidward);
		  bmp = Bitmap.createScaledBitmap(bmp, 400, 400, true);
		}
		else{
			//bmp = Bitmap.createScaledBitmap(RaceTrackerApp.mProfile.avatar, 400, 400, true);
		}
		//canvas.drawColor(Color.BLACK);
		buildDrawingCache();
		out = Bitmap.createBitmap(getDrawingCache()); 
		
		
		
		FileOutputStream fos = null;
		try {
		    fos = new FileOutputStream("/sdcard/cap.png");
		    out.compress(Bitmap.CompressFormat.PNG, 100, fos); // bmp is your Bitmap instance
		    // PNG is a lossless format, the compression factor (100) is ignored
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (fos != null) {
		            fos.close();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
		
		
		
        canvas.drawBitmap(out, 0, 0, new Paint());
        //RaceTrackerApp.mProfile.avatar = bmp;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@SuppressLint("WrongCall")
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surface_holder = holder;
		Canvas canvas = null;
        try {
            canvas = holder.lockCanvas(null);
            synchronized (holder) {
                onDraw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(mCamera != null){
		mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
		}
	}
}
