package edu.njupt.zhb.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.njupt.zhb.slidemenu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


public class RadarView extends View {
	Bitmap bitmapBlue;
	Bitmap bitmapGray;
	Bitmap bitmapRadar;
	int height;
	float percent;
	int value;// 显示的值
	float degrees;

	float ox;
	float oy;
	float cx;
	float cy;

	boolean hideRadar;

	public RadarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bitmapBlue = BitmapFactory.decodeResource(getResources(),
				R.drawable.circle_blue);
		bitmapGray = BitmapFactory.decodeResource(getResources(),
				R.drawable.circle_gray);
		bitmapRadar = BitmapFactory.decodeResource(getResources(),
				R.drawable.radar);
		ox = (bitmapBlue.getWidth() - bitmapRadar.getWidth()) >> 1;
		oy = (bitmapBlue.getHeight() - bitmapRadar.getHeight()) >> 1;
		cx = bitmapBlue.getWidth() >> 1;
		cy = bitmapBlue.getHeight() >> 1;
		hideRadar = true;
	}

	public void dismiss() {
		hideRadar = true;//停止线程
	}

	public void startScanfThread() {
		hideRadar = false;//启动一个线程进行不断的旋转角度
		ExecutorService CachedThreadPool = Executors.newSingleThreadExecutor();  
		CachedThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (!hideRadar) {
					degrees = (degrees += 2) >= 360 ? 0 : degrees;
					postInvalidate();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		height = getHeight();
		percent = height / 100f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawBitmap(bitmapBlue, 0, 0, null);
		canvas.save();
		canvas.clipRect(0, 0, getWidth(), (100 - value) * percent);
		canvas.drawBitmap(bitmapGray, 0, 0, null);
		canvas.restore();
		if (!hideRadar) {
			canvas.save();
			canvas.rotate(degrees, cx, cy);
			canvas.drawBitmap(bitmapRadar, ox, oy, null);
			canvas.restore();
		}
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public float getDegrees() {
		return degrees;
	}

	public void setDegrees(float degrees) {
		this.degrees = degrees;
	}

	public boolean isHideRadar() {
		return hideRadar;
	}

	public void setHideRadar(boolean hideRadar) {
		this.hideRadar = hideRadar;
	}
}
