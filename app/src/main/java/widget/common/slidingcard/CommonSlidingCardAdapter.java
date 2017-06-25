package widget.common.slidingcard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sirius on 2017-6-24.
 */

public class CommonSlidingCardAdapter extends RecyclerView.Adapter<CommonSlidingCardAdapter.ViewHolder> {
    private Context context;
    private List<String> list = new ArrayList<>();

    public CommonSlidingCardAdapter(Context context) {
        this.context = context;
        list.clear();
        list.add("禁剑姬");
        list.add("纠结");
        list.add("对方水电费");
        list.add("顺丰到付");
        list.add("热");
        list.add("发热");
        list.add("发热1");
        list.add("发热2");
        list.add("发热3");
        list.add("发热4");
        list.add("发热5");
        list.add("发热6");
        list.add("发热7");
        list.add("发热8");
        list.add("发热9");
        list.add("发热10");
        list.add("发热11");
        list.add("发热12");
        list.add("发热13");
        list.add("发热14");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_widget_sliding_card_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.common_sliding_head_text);
        }
    }
}
