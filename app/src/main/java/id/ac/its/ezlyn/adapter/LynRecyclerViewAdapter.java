//package id.ac.its.ezlyn.adapter;
//
//import android.content.Context;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import id.ac.its.ezlyn.R;
//import id.ac.its.ezlyn.model.Lyn;
//
///**
// * Created by Ilham Aulia Majid on 19-Apr-17.
// */
//
//public class LynRecyclerViewAdapter extends RecyclerView.Adapter<LynRecyclerViewAdapter.ViewHolder> {
//
//    Context context;
//    List<Lyn> lyns;
//
//    public LynRecyclerViewAdapter(Context context, List<Lyn> lyns) {
//        this.context = context;
//        this.lyns = lyns;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lyn_list, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Lyn lyn = lyns.get(position);
//
//        holder.tvCode.setText(lyn.getType().getCode());
//        holder.tvCode.setTextColor(ContextCompat.getColor(context,lyn.getType().getTextColor()));
//        holder.tvCode.setBackgroundColor(ContextCompat.getColor(context,lyn.getType().getBackgroundColor()));
//        holder.tvDestination.setText(lyn.getType().getEndPoint());
//        holder.tvFee.setText(lyn.getType().getFee());
//
//        holder.tvPlate.setText(lyn.getPlate());
//        holder.tvEta.setText(lyn.getEta());
//        holder.tvStatus.setText(lyn.getStatus());
//    }
//
//    @Override
//    public int getItemCount() {
//        return lyns.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.tv_lyn_code)
//        TextView tvCode;
//        @BindView(R.id.tv_lyn_plate)
//        TextView tvPlate;
//        @BindView(R.id.tv_lyn_destination)
//        TextView tvDestination;
//        @BindView(R.id.tv_lyn_eta)
//        TextView tvEta;
//        @BindView(R.id.tv_lyn_fee)
//        TextView tvFee;
//        @BindView(R.id.tv_lyn_status)
//        TextView tvStatus;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
//}
