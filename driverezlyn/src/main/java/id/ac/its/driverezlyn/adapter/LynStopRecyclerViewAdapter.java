package id.ac.its.driverezlyn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.its.driverezlyn.R;

/**
 * Created by Ilham Aulia Majid on 19-Apr-17.
 */

public class LynStopRecyclerViewAdapter extends RecyclerView.Adapter<LynStopRecyclerViewAdapter.ViewHolder>{

    Context context;
    List<LynStop> lynStops;

    public LynStopRecyclerViewAdapter(Context context, List<LynStop> lynStops) {
        this.context = context;
        this.lynStops = lynStops;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lyn_stop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LynStop lynStop = lynStops.get(position);
        holder.tvLynStopNum.setText(String.valueOf(position + 1));
        holder.tvLynStopName.setText(lynStop.getName());
        holder.tvLynStopWaiting.setText(lynStop.getWaiting());
    }

    @Override
    public int getItemCount() {
        return lynStops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_lyn_stop_num)
        TextView tvLynStopNum;
        @BindView(R.id.tv_lyn_stop_name)
        TextView tvLynStopName;
        @BindView(R.id.tv_lyn_stop_waiting)
        TextView tvLynStopWaiting;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
