package com.zhuyun.jingxi.android.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickIndexBar extends View {
	private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private Paint paint;
	private int width;
	private float cellHeight;//格子的高度
	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public QuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public QuickIndexBar(Context context) {
		super(context);
		init();
	}
	/**
	 * 初始化的方法
	 */
	private void init(){
		float textSize = 10;






		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
//		paint.setFlags(Paint.ANTI_ALIAS_FLAG)
		paint.setColor(Color.GRAY);//设置灰色字体
		paint.setTextSize(textSize);
		paint.setTextAlign(Align.CENTER);//设置文本绘制的起点是底边的中心点

		//设置加粗
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = getMeasuredWidth();
		cellHeight = getMeasuredHeight()*1f/indexArr.length;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		//依次将26个字母绘制到指定的位置上面
		for (int i = 0; i < indexArr.length; i++) {
			String text = indexArr[i];
			float x = width/2;
			float y = cellHeight/2 + getTextHeight(text)/2 + i*cellHeight;

			paint.setColor(i == lastIndex? Color.BLACK: Color.GRAY);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(20);

			canvas.drawText(text, x, y, paint);
		}
	}
	/**
	 * 获取文本的高度
	 * @param text
	 * @return
	 */
	private int getTextHeight(String text){
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);//该方法执行完，bounds就有宽高值了
		return bounds.height();
	}
	
	private int lastIndex = -1;//上次字母的索引
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			int y = (int) event.getY();
			int index = (int) (y/cellHeight);//得到的是对应字母的索引
			if(index!=lastIndex){
				//如果当前的字母索引和上个字母索引不一样，则打印
//				Log.e("tag", indexArr[index]);
				if(index>=0 && index<indexArr.length){
					//对index作安全性的检测
					if(listener!=null){
						listener.onTouchWord(indexArr[index]);
					}
				}
			}
			lastIndex = index;
			break;
		case MotionEvent.ACTION_UP:
			lastIndex = -1;//重置索引
			break;
		}
		invalidate();//引起重绘
		return true;//因为要接受和处理TouchMove
	}

	private OnTouchWordListener listener;
	/**
	 * 设置触摸字母的监听
	 * @param listener
	 */
	public void setOnTouchWordListener(OnTouchWordListener listener){
		this.listener = listener;
	}
	
	/**
	 * 触摸字母的监听
	 * @author Administrator
	 *
	 */
	public interface OnTouchWordListener{
		/**
		 * 当触摸到字母
		 * @param word
		 */
		void onTouchWord(String word);
	}
}
