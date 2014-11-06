package com.gc.phonetutorial.views;

import com.gc.phonetutorial.R;
import com.gc.phonetutorial.utils.Utils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class ButtonFloat extends RelativeLayout{
	
	int sizeIcon = 24;
	int sizeRadius = 28;
	
	// Complete in child class 
		int minWidth;
		int minHeight;
		int background;
		float rippleSpeed = 10f;
		int rippleSize = 3;
		
		OnClickListener onClickListener;
		int backgroundColor = Color.parseColor("#FFFFFF");
		
		public boolean isShow = false;
		
		float showPosition;
		float hidePosition;
	
	
	ImageView icon; // Icon of float button
	Drawable drawableIcon;
	
	public ButtonFloat(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.drawable.background_button_float);
		sizeRadius = 28;
		setDefaultProperties();
		icon = new ImageView(context);
		if(drawableIcon != null) {
			try {
				icon.setBackground(drawableIcon);
			} catch (NoSuchMethodError e) {
				icon.setBackgroundDrawable(drawableIcon);
			}
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.dpToPx(sizeIcon, getResources()),Utils.dpToPx(sizeIcon, getResources()));
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		icon.setLayoutParams(params);
		addView(icon);		
		
		LayerDrawable layer = (LayerDrawable) getBackground();
		GradientDrawable shape =  (GradientDrawable) layer.findDrawableByLayerId(R.id.shape_bacground);
		shape.setColor(backgroundColor);
		
		post(new Runnable() {
			
			@Override
			public void run() {
				showPosition = ViewHelper.getY(ButtonFloat.this) - Utils.dpToPx(24, getResources());
				hidePosition = ViewHelper.getY(ButtonFloat.this) + getHeight() * 3;
				ViewHelper.setY(ButtonFloat.this, hidePosition);
			}
		});
		
	}
	
	protected void setDefaultProperties(){
		rippleSpeed = Utils.dpToPx(2, getResources());
		rippleSize = Utils.dpToPx(5, getResources());
		minWidth = Utils.dpToPx(56, getResources());
		minHeight = Utils.dpToPx(56, getResources());
		setMinimumHeight(minHeight);
		setMinimumWidth(minWidth);
		background = R.drawable.background_button_float;
	}
	
	
		
	Integer height;
	Integer width;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (x != -1) {
			Rect src = new Rect(0, 0, getWidth(), getHeight());
			Rect dst = new Rect(Utils.dpToPx(1, getResources()), Utils.dpToPx(2, getResources()), getWidth()-Utils.dpToPx(1, getResources()), getHeight()-Utils.dpToPx(2, getResources()));
			canvas.drawBitmap(cropCircle(makeCircle()), src, dst, null);
		}
		invalidate();
	}
	
	public void show(){
		ObjectAnimator animator = ObjectAnimator.ofFloat(ButtonFloat.this, "y", showPosition);
		animator.setInterpolator(new BounceInterpolator());
		animator.setDuration(1500);
		animator.start();
		isShow = true;
	}
	
	public void hide(){
		
		ObjectAnimator animator = ObjectAnimator.ofFloat(ButtonFloat.this, "y", hidePosition);
		animator.setInterpolator(new BounceInterpolator());
		animator.setDuration(1500);
		animator.start();
		
		isShow = false;
	}
	

	public Bitmap cropCircle(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	            bitmap.getWidth()/2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    return output;
	}
	
	float x = -1, y = -1;
	float radius = -1;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			radius = getHeight()/rippleSize;
			x = event.getX();
			y = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			radius = getHeight()/rippleSize;
			x = event.getX();
			y = event.getY();
			if(!((event.getX()<= getWidth() && event.getX() >= 0) && 
					(event.getY()<= getHeight() && event.getY() >= 0))){
				x = -1;
				y = -1;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if((event.getX()<= getWidth() && event.getX() >= 0) && 
					(event.getY()<= getHeight() && event.getY() >= 0)){
				radius++;
			}else{
				x = -1;
				y = -1;
			}
		}
		return true;
	}
	
	public Bitmap makeCircle(){
		Bitmap output = Bitmap.createBitmap(getWidth()-Utils.dpToPx(6, getResources()),
	            getHeight()-Utils.dpToPx(7, getResources()), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		canvas.drawARGB(0, 0, 0, 0);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(makePressColor());
		canvas.drawCircle(x, y, radius, paint);
		if(radius > getHeight()/rippleSize)
			radius += rippleSpeed;
		if(radius >= getWidth()){
			x = -1;
			y = -1;
			radius = getHeight()/rippleSize;
			if(onClickListener != null)
				onClickListener.onClick(this);
		}
		return output;
	}
	
	/**
	 * Make a dark color to ripple effect
	 * @return
	 */
	protected int makePressColor(){
		int r = (this.backgroundColor >> 16) & 0xFF;
		int g = (this.backgroundColor >> 8) & 0xFF;
		int b = (this.backgroundColor >> 0) & 0xFF;
		r = (r-30 < 0) ? 0 : r-30;
		g = (g-30 < 0) ? 0 : g-30;
		b = (b-30 < 0) ? 0 : b-30;
		return Color.rgb(r, g, b);		
	}
	
	
	public void setBackgroundColor(int color){
		this.backgroundColor = color;
		LayerDrawable layer = (LayerDrawable) getBackground();
		GradientDrawable shape =  (GradientDrawable) layer.findDrawableByLayerId(R.id.shape_bacground);
		shape.setColor(backgroundColor);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.onClickListener = l;
	}
	
	public void setIcon(Drawable drawable){
		try {
			icon.setBackground(drawable);
		} catch (NoSuchMethodError e) {
			icon.setBackgroundDrawable(drawable);
		}
	}

}
