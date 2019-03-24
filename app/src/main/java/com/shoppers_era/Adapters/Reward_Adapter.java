package com.shoppers_era.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import com.shoppers_era.Model.Reward;
import com.shoppers_era.R;


public class Reward_Adapter extends RecyclerView.Adapter<Reward_Adapter.MyViewHolder> {


    private List<Reward> rewards;
    private Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView reward_id;
        private TextView valid_from;
        private TextView valid_till;
        private TextView reward_amount;

        public MyViewHolder(View view) {
            super(view);
            reward_id = (TextView) view.findViewById(R.id.coupon_id_text);
            valid_from = (TextView) view.findViewById(R.id.valid_from_text);
            valid_till = (TextView) view.findViewById(R.id.valid_to_text);
            reward_amount = (TextView) view.findViewById(R.id.amount_text);
        }
    }


    public Reward_Adapter(Context mContext,List<Reward> rewards) {
        this.mContext = mContext;
        this.rewards = rewards;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Reward reward = rewards.get(position);

        holder.reward_id.setText(reward.getReward_id());
        if (reward.getValid_from().equals("1")){
            holder.valid_from.setText("Active");
            holder.valid_from.setTextColor(mContext.getResources().getColor(R.color.green));
        }else {
            holder.valid_from.setText("Expired");
            holder.valid_from.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        holder.valid_till.setText(reward.getValid_till());
        holder.reward_amount.setText("Rs"+reward.getReward_amount());
    }


    @Override
    public int getItemCount() {
        return rewards.size();
    }



}
