package widget.common.slidingcard;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by sirius on 2017-6-24.
 * 卡片联动
 */

public class SlindingCardBehavior extends CoordinatorLayout.Behavior<SlidingCardLayout2> {
    private int mInitialOffset = 0;

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, SlidingCardLayout2 child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        //测量child
        //卡片的高度=父容器给的高度 - 上边和下边几个child的头部的高度和
        int offset = getChildMeasureOffset(parent, child);
        //性能的优化---避免了View的重复绘制问题
        int height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offset;//mode+size
        child.measure(parentWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        return true;
    }

    private int getChildMeasureOffset(CoordinatorLayout parent, SlidingCardLayout2 child) {
        int offset = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view != child && view instanceof SlidingCardLayout2) {
                offset += ((SlidingCardLayout2) view).getHeaderHeight();
            }
        }
        return offset;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, SlidingCardLayout2 child, int layoutDirection) {

        parent.onLayoutChild(child, layoutDirection);

        SlidingCardLayout2 previous = getPreviousChild(parent, child);
        int offset = 0;
        if (previous != null) {
            offset = previous.getTop() + previous.getHeaderHeight();
//            child.offsetTopAndBottom(offset);
        }
        int offset2 = getChildMeasureOffset2(parent, child);
        child.offsetTopAndBottom(Math.max(offset, offset2));
        mInitialOffset = child.getTop();
        return true;
    }

    //获取之前所以卡片头部的高度
    private int getChildMeasureOffset2(CoordinatorLayout parent, SlidingCardLayout2 child) {
        int offset = 0;
        int currentIndex = parent.indexOfChild(child);
        for (int i = currentIndex - 1; i >= 0; i--) {
            View view = parent.getChildAt(i);
            if (view != child && view instanceof SlidingCardLayout2) {
                offset += ((SlidingCardLayout2) view).getHeaderHeight();
            }
        }
        return offset;
    }

    private SlidingCardLayout2 getPreviousChild(CoordinatorLayout parent, SlidingCardLayout2 child) {
        int currentIndex = parent.indexOfChild(child);
        Log.e("getPreviousChild", "currentIndex=" + currentIndex);
        for (int i = currentIndex - 1; i >= 0; i--) {
            View view = parent.getChildAt(i);
            if (view instanceof SlidingCardLayout2) {
                return (SlidingCardLayout2) view;
            }
        }
        return null;
    }

    private SlidingCardLayout2 getNextSlidingCardLayout2(CoordinatorLayout parent, SlidingCardLayout2 child) {
        int currentIndex = parent.indexOfChild(child);
        for (int i = currentIndex + 1; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof SlidingCardLayout2) {
                return (SlidingCardLayout2) view;
            }
        }
        return null;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, SlidingCardLayout2 child, View directTargetChild, View target, int nestedScrollAxes) {
        boolean vertical = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return vertical && child == directTargetChild;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout parent, SlidingCardLayout2 child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(parent, child, target, dx, dy, consumed);
        int minOffset = mInitialOffset;
        int maxOffset = mInitialOffset + child.getHeight() - child.getHeaderHeight();
        int initialOffset = child.getTop();
        int offset = clamp(initialOffset - dy, minOffset, maxOffset) - initialOffset;
        child.offsetTopAndBottom(offset);
        consumed[1] = -offset;
        otherChildScroll(consumed[1], parent, child);
    }

    private void otherChildScroll(int i, CoordinatorLayout parent, SlidingCardLayout2 child) {
        if (i == 0) {
            return;
        }
        if (i > 0) {//向上滚动
            Log.e("tag", "向上滚动");
            SlidingCardLayout2 currentCard = child;
            SlidingCardLayout2 aboveCard = getPreviousChild(parent, currentCard);
            while (aboveCard != null) {
                int offset = getScrollOffset(aboveCard, currentCard);
                if (offset > 0) {
                    aboveCard.offsetTopAndBottom(-offset);
                }
                currentCard = aboveCard;
                aboveCard = getPreviousChild(parent, currentCard);
            }
        } else if (i < 0) {//向下滚动
            SlidingCardLayout2 currentCard = child;
            SlidingCardLayout2 belowCard = getNextSlidingCardLayout2(parent, currentCard);
            while (belowCard != null) {
                int offset = getScrollOffset(currentCard, belowCard);
                if (offset > 0) {
                    belowCard.offsetTopAndBottom(offset);
                }
                currentCard = belowCard;
                belowCard = getNextSlidingCardLayout2(parent, currentCard);
            }
        }

    }

    private int getScrollOffset(SlidingCardLayout2 aboveChild, SlidingCardLayout2 belowChild) {
        return aboveChild.getTop() + aboveChild.getHeaderHeight() - belowChild.getTop();
    }

    private int clamp(int number, int min, int max) {
        if (number < min) {
            number = min;
        } else if (number > max) {
            number = max;
        }
        return number;
    }


}
