package com.tutu.gogogo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SegmentLayout extends FrameLayout {
	private int mDragDistance = 0;
	private int mTriggerDistance = 100;
	private int touchY = 0;

	private int lastY = 0;
	private boolean isChildVisable = false;
	private View mChild0;
	private View mChild1;
	private Scroller mScrollerUP;
	private Scroller mScrollerDown;

	public SegmentLayout(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {

		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		mScrollerUP = new Scroller(context);
		mScrollerDown = new Scroller(context);
	}

	public SegmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		mScrollerUP = new Scroller(context);
		mScrollerDown = new Scroller(context);
	}

	public SegmentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		mScrollerUP = new Scroller(context);
		mScrollerDown = new Scroller(context);
	}

	public SegmentLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mScrollerUP = new Scroller(context);
		mScrollerDown = new Scroller(context);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mChild0 = getChildAt(0);
		mChild1 = getChildAt(1);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchY = (int) event.getY();
			lastY = (int) event.getY();
			if (isChildVisable)
				break;
			mTriggerDistance = mChild0.getMeasuredHeight();

			break;
		case MotionEvent.ACTION_UP:
			FrameLayout.LayoutParams paramsUP = (LayoutParams) mChild1
					.getLayoutParams();
			if (isChildVisable) {

				if (paramsUP.topMargin < 0) {
					mScrollerDown.startScroll(0, 0, 0, 0, 500);
				} else {
					mScrollerDown.startScroll(0, paramsUP.topMargin, 0,
							mTriggerDistance - paramsUP.topMargin, 500);
				}

				invalidate();

			} else {

				mScrollerUP.startScroll(0, paramsUP.topMargin, 0,
						-paramsUP.topMargin, 500);
				invalidate();
			}

			break;
		case MotionEvent.ACTION_MOVE:

			FrameLayout.LayoutParams params = (LayoutParams) mChild1
					.getLayoutParams();
			mDragDistance = (int) Math.abs((event.getY() - touchY));
			int diff = (int) (event.getY() - lastY);
			lastY = (int) event.getY();
			if (Math.abs(mDragDistance) < 10)
				break;
			if (Math.abs(params.topMargin) >= mTriggerDistance && diff > 0) {
				mDragDistance = mTriggerDistance;
				isChildVisable = true;
				break;
			}
			if (Math.abs(params.topMargin) >= mTriggerDistance / 2) {
				if (params.topMargin + diff > mTriggerDistance) {
					diff = mTriggerDistance - params.topMargin;
				}
				params.setMargins(0, params.topMargin + diff, 0, 0);

				mChild1.setLayoutParams(params);
				isChildVisable = true;
			} else {
				params.setMargins(0, params.topMargin + diff, 0, 0);
				mChild1.setLayoutParams(params);
				isChildVisable = false;
			}

			break;

		default:
			break;
		}

		return true;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);

		FrameLayout.LayoutParams cparams = (LayoutParams) mChild1
				.getLayoutParams();
		if (isChildVisable) {

			mChild0.setAlpha((float) cparams.topMargin
					/ (float) mChild0.getMeasuredHeight());
			if (mChild1 != null && mScrollerDown != null
					&& mScrollerDown.computeScrollOffset()) {
				FrameLayout.LayoutParams params = (LayoutParams) mChild1
						.getLayoutParams();
				params.setMargins(0, mScrollerDown.getCurrY(), 0, 0);
				mChild1.setLayoutParams(params);
				postInvalidate();
			}
		} else {
			mChild0.setAlpha((float) cparams.topMargin
					/ (float) mChild0.getMeasuredHeight());
			if (mChild1 != null && mScrollerUP != null
					&& mScrollerUP.computeScrollOffset()) {
				FrameLayout.LayoutParams params = (LayoutParams) mChild1
						.getLayoutParams();
				params.setMargins(0, mScrollerUP.getCurrY(), 0, 0);
				mChild1.setLayoutParams(params);
				postInvalidate();
			}
		}

	}
}
