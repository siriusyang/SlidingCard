package widget.common.slidingcard;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleHolder> {

    public static class SimpleHolder extends RecyclerView.ViewHolder {
        public final TextView text;

        public SimpleHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.text);
            itemView.setClickable(true);
        }
    }

    private final String[] ITEMS = {
            "赵丽颖", "angelababy", "林心如", "柳岩",
            "陈乔恩", "苍老师", "宋茜", "杨幂",
            "范冰冰", "唐嫣", "刘亦菲",
            "刘诗诗", "高圆圆", "林志玲"
    };

    private final LayoutInflater mInflater;

    public SimpleAdapter(RecyclerView recyclerView) {
        mInflater = LayoutInflater.from(recyclerView.getContext());
        //条目间距
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                final int position = parent.getChildViewHolder(view).getAdapterPosition();
                final int offset = parent.getResources()
                        .getDimensionPixelOffset(R.dimen.activity_vertical_margin);
                outRect.set(offset,
                        position == 0 ? offset : 0,
                        offset,
                        offset);
            }
        });
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater
                .inflate(R.layout.list_item, parent, false);
        return new SimpleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleHolder viewHolder, int position) {
        viewHolder.text.setText(ITEMS[position]);
    }

    @Override
    public int getItemCount() {
        return ITEMS.length;
    }
}