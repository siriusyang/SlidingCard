package widget.common.slidingcard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by sirius on 2017-6-24.
 */
@CoordinatorLayout.DefaultBehavior(SlindingCardBehavior.class)
public class SlidingCardLayout2 extends FrameLayout {
    private String text= "";
    private int textColor = 0;
    private int textSize=24;
    private int backgroundColor = 0;
    private int common_sliding_headHeight;

    public SlidingCardLayout2(@NonNull Context context) {
        this(context, null);
    }

    public SlidingCardLayout2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_widget_sliding_card_layout, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CommonSlidingCardLayoutStyleable);
        textColor = typedArray.getColor(R.styleable.CommonSlidingCardLayoutStyleable_textColor,Color.parseColor("#FF0000"));
        backgroundColor = typedArray.getColor(R.styleable.CommonSlidingCardLayoutStyleable_backgroundColor, Color.parseColor("#00FF00"));
        text = typedArray.getString(R.styleable.CommonSlidingCardLayoutStyleable_text);
        textSize = typedArray.getDimensionPixelSize(R.styleable.CommonSlidingCardLayoutStyleable_textSize,24);


        TextView  common_sliding_head_text = (TextView) findViewById(R.id.common_sliding_head_text);
        RecyclerView common_sliding_recyclerView = (RecyclerView) findViewById(R.id.common_sliding_recyclerView);

        common_sliding_head_text.setText(text);
        common_sliding_head_text.setTextSize(textSize);
        common_sliding_head_text.setTextColor(textColor);
        common_sliding_head_text.setBackgroundColor(backgroundColor);

        common_sliding_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CommonSlidingCardAdapter mCommonSlidingCardAdapter = new CommonSlidingCardAdapter(context);
        common_sliding_recyclerView.setAdapter(mCommonSlidingCardAdapter);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        common_sliding_headHeight = findViewById(R.id.common_sliding_head_text).getMeasuredHeight();

    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        if (w != oldw || h != oldh) {
//            common_sliding_headHeight =findViewById(R.id.common_sliding_head_text).getMeasuredHeight();
//        }
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public int getHeaderHeight() {
        return common_sliding_headHeight;

    }
}
