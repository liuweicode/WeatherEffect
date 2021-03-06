package co.liuwei.weathereffect.view;

import java.util.ArrayList;
import java.util.Random;

import co.liuwei.weathereffect.R;
import co.liuwei.weathereffect.view.SnowSurfaceView.Coordinate;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class RainSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

	/**
	 * 雪花图片
	 */
	Bitmap bitmap_snows[] = new Bitmap[5];

	Bitmap bitmap_bg;

	Thread thread;

	/**
	 * 图片是否在移动
	 */
	public  boolean IsRunning = true;

//	private int left;
//	private int top;

	private float screenWidth;
	private float screenHeiht;

//	private boolean flag = true;

	// 花的位置
	//private Coordinate[] snows = new Coordinate[80];

	private static Random random = new Random();

	/**
	 * 图片移动速度
	 */
//	private int dx = 1;
//	private int dy = 100;
//
//	private int sleepTime;
//
//	private int offset = 0;

	private ArrayList<Snow> snowflake_xxl = new ArrayList<Snow>();
	private ArrayList<Snow> snowflake_xl = new ArrayList<Snow>();
	private ArrayList<Snow> snowflake_m = new ArrayList<Snow>();
	private ArrayList<Snow> snowflake_s = new ArrayList<Snow>();
	private ArrayList<Snow> snowflake_l = new ArrayList<Snow>();

	private SurfaceHolder holder;

	public RainSurfaceView(Context context) {
		super(context);
		Log.i("icer", "-->RainSurfaceView()");
		Log.i("icer", "-->isRunning()-->"+IsRunning);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		
		holder = getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888); // 顶层绘制SurfaceView设成透明
		getViewSize(context);
		LoadSnowImage();
		addRandomSnow();

	}

	// 获取屏幕的分辨率
	private void getViewSize(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		this.screenHeiht = metrics.heightPixels;
		this.screenWidth = metrics.widthPixels;
	}

	/**
	 * 加载雪花图片到内存中
	 * 
	 */
	public void LoadSnowImage() {
		//Resources r = this.getContext().getResources();
		bitmap_snows[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.rain_l);
		bitmap_snows[1] = BitmapFactory.decodeResource(getResources(),
				R.drawable.rain_s);
		bitmap_snows[2] = BitmapFactory.decodeResource(getResources(),
				R.drawable.rain_m);
		bitmap_snows[3] = BitmapFactory.decodeResource(getResources(),
				R.drawable.rain_xl);
		bitmap_snows[4] = BitmapFactory.decodeResource(getResources(),
				R.drawable.rain_xxl);
		bitmap_bg = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_rain);
	}

	public void addRandomSnow() {

		// 初始化雪花
		for (int i = 0; i < 20; i++) {
			snowflake_xxl.add(new Snow(bitmap_snows[4], random.nextFloat()
					* screenWidth, random.nextFloat() * screenHeiht, 25f,
					1 - random.nextFloat() * 2));
			snowflake_xl.add(new Snow(bitmap_snows[3], random.nextFloat()
					* screenWidth, random.nextFloat() * screenHeiht, 25f,
					1 - random.nextFloat() * 2));
			snowflake_m.add(new Snow(bitmap_snows[2], random.nextFloat()
					* screenWidth, random.nextFloat() * screenHeiht, 25f,
					1 - random.nextFloat() * 2));
			snowflake_s.add(new Snow(bitmap_snows[1], random.nextFloat()
					* screenWidth, random.nextFloat() * screenHeiht, 25f,
					1 - random.nextFloat() * 2));
			snowflake_l.add(new Snow(bitmap_snows[0], random.nextFloat()
					* screenWidth, random.nextFloat() * screenHeiht, 25f,
					1 - random.nextFloat() * 2));
		}

	}

	public void drawSnow(Canvas canvas) {
		Paint paint = new Paint();

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);

		RectF rectF = new RectF(0, 0, screenWidth, screenHeiht);
		canvas.drawBitmap(bitmap_bg, null, rectF, paint);
		Snow snow = null;
		for (int i = 0; i < 10; i++) {
			snow = snowflake_xxl.get(i);
			canvas.drawBitmap(snow.bitmap, snow.x, snow.y, paint);
			snow = snowflake_xl.get(i);
			canvas.drawBitmap(snow.bitmap, snow.x, snow.y, paint);
			snow = snowflake_m.get(i);
			canvas.drawBitmap(snow.bitmap, snow.x, snow.y, paint);
			snow = snowflake_s.get(i);
			canvas.drawBitmap(snow.bitmap, snow.x, snow.y, paint);
			snow = snowflake_l.get(i);
			canvas.drawBitmap(snow.bitmap, snow.x, snow.y, paint);

		}

	}

	@Override
	public void run() {

		while (IsRunning) {
			Log.i("icer", "-->run(");

			Canvas canvas = null;

			synchronized (this) {

				try {

					canvas = holder.lockCanvas();
					if (canvas != null) {
						drawSnow(canvas);
						Snow snow;

						for (int i = 0; i < 20; i++) {
							snow = snowflake_xxl.get(i);
							SnowDown(snow);

							snow = snowflake_xl.get(i);
							SnowDown(snow);

							snow = snowflake_m.get(i);
							SnowDown(snow);

							snow = snowflake_s.get(i);
							SnowDown(snow);

							snow = snowflake_l.get(i);
							SnowDown(snow);

						}
						Thread.sleep(15);
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (null != canvas) {
						holder.unlockCanvasAndPost(canvas);
					}
				}

			}

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			IsRunning = false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 雪花下落
	 * 
	 * @param snow
	 */
	private void SnowDown(Snow snow) {
		// 雪花的落出屏幕后又让它从顶上下落
		if (snow.x > screenWidth || snow.y > screenHeiht) {
			snow.y = 0;
			snow.x = random.nextFloat() * screenWidth;
		}
		snow.x += snow.offset;// 下落飘的偏移量
		snow.y += snow.speed;// 下落的速度
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i("icer", "-->surfaceCreated(");
		thread = new Thread(this);
		thread.start();
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i("icer", "-->surfaceDestroyed(");
		IsRunning = false;

	}

	public class Coordinate {
		int x;
		int y;

		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

}
