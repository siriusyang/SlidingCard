package widget.common.slidingcard;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class SlidingCardBehavior extends Behavior<SlidingCardLayout> {
	private int mInitialOffset;


	@Override
	public boolean onMeasureChild(CoordinatorLayout parent,
								  SlidingCardLayout child, int parentWidthMeasureSpec, int widthUsed,
								  int parentHeightMeasureSpec, int heightUsed) {
		//测量child
		//卡片的高度=父容器给的高度 - 上边和下边几个child的头部的高度和
		int offset = getChildMeasureOffset(parent, child);
		//性能的优化---避免了View的重复绘制问题
		int height = MeasureSpec.getSize(parentHeightMeasureSpec) - offset;//mode+size
		child.measure(parentWidthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
		return true;
	}

	private int getChildMeasureOffset(CoordinatorLayout parent,
									  SlidingCardLayout child) {
		int offset =0;
		// 上边和下边几个child的头部的高度和
		for (int i = 0; i < parent.getChildCount(); i++) {
			View view = parent.getChildAt(i);
			if(view!=child&&view instanceof SlidingCardLayout){
				offset += ((SlidingCardLayout)view).getHeaderHeight();
			}
		}
		return offset;
	}

	@Override
	public boolean onLayoutChild(CoordinatorLayout parent,
								 SlidingCardLayout child, int layoutDirection) {
		//按照默认情况摆放（卡片全部叠在一起了）
		parent.onLayoutChild(child, layoutDirection);//l,r,b,t
		//控制top的值就可以了
		SlidingCardLayout previous = getPreViousChild(parent,child);
		if(previous!=null){
			int offset = previous.getTop() + previous.getHeaderHeight();
			child.offsetTopAndBottom(offset);
		}
//		child.layout(0, t, child.getMeasuredWidth(), b);
		mInitialOffset = child.getTop();
		return true;
	}

	private SlidingCardLayout getPreViousChild(CoordinatorLayout parent,
											   SlidingCardLayout child) {
		int cardIndex = parent.indexOfChild(child);
		Log.e("getPreViousChild","cardIndex="+cardIndex);
		for (int i = cardIndex-1; i >=0; i--) {
			View v = parent.getChildAt(i);
			if(v instanceof SlidingCardLayout){
				return (SlidingCardLayout) v;
			}
		}
		return null;
	}
	private SlidingCardLayout getNextChild(CoordinatorLayout parent,
										   SlidingCardLayout child) {
		int cardIndex = parent.indexOfChild(child);
		for (int i = cardIndex+1; i <parent.getChildCount(); i++) {
			View v = parent.getChildAt(i);
			if(v instanceof SlidingCardLayout){
				return (SlidingCardLayout) v;
			}
		}
		return null;
	}

	@Override
	public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
									   SlidingCardLayout child, View directTargetChild, View target,
									   int nestedScrollAxes) {
		boolean isVertical = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL)!=0;
		return isVertical && child==directTargetChild;
	}

	@Override
	public void onNestedPreScroll(CoordinatorLayout parent,
								  SlidingCardLayout child, View target, int dx, int dy, int[] consumed) {
		//监听滑动情况，控制自己的滑动以及其他卡片的联动效果
		//手指滑动了多少---child要偏移的值，dy：往上滑 +；往下滑 -
		//dy要控制在一个有效范围
		int minOffset = mInitialOffset;
		int maxOffset = mInitialOffset + child.getHeight() - child.getHeaderHeight();
		int initialOffset = child.getTop();

		//1.控制自己的滑动
		int offset = clamp(initialOffset-dy, minOffset, maxOffset)-initialOffset;
		child.offsetTopAndBottom(offset);
		consumed[1] = -offset;
		//2.其他卡片的联动效果
		shiftSlidings(consumed[1],parent,child);
	}

	private void shiftSlidings(int shift, CoordinatorLayout parent,
							   SlidingCardLayout child) {
		//分方向处理
		if(shift==0){
			return;
		}
		if(shift>0){//往上推
			SlidingCardLayout current = child;
			SlidingCardLayout card = getPreViousChild(parent, current);
			while(card!=null){
				int offset = getHeaderOverlap(card,current);
				if(offset>0)
					card.offsetTopAndBottom(-offset);//往上推要位负的
				current = card;
				card = getPreViousChild(parent, current);
			}
		}else{//往下推
			SlidingCardLayout current = child;
			SlidingCardLayout card = getNextChild(parent, current);
			while(card!=null){
				int offset = getHeaderOverlap(current,card);
				if(offset>0)
					card.offsetTopAndBottom(offset);
				current = card;
				card = getNextChild(parent, current);
			}
		}

	}

	private int getHeaderOverlap(SlidingCardLayout above,
								 SlidingCardLayout below) {
		return above.getTop() + above.getHeaderHeight() - below.getTop();
	}

	//取中间值
	private int clamp(int i, int minOffset, int maxOffset) {//Math.min(Math.max(a,b),c)
		if(i>maxOffset){
			return maxOffset;
		}else if(i<minOffset){
			return minOffset;
		}else{
			return i;
		}
	}

	//思考：如何实现惯性---fling
	@Override
	public boolean onNestedFling(CoordinatorLayout coordinatorLayout,
								 SlidingCardLayout child, View target, float velocityX,
								 float velocityY, boolean consumed) {
		// 让child飞一会
		return super.onNestedFling(coordinatorLayout, child, target, velocityX,
				velocityY, consumed);
	}

}
