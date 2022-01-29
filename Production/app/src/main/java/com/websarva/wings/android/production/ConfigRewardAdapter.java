package com.websarva.wings.android.production;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ConfigRewardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String,Object>> rewardList;
    private int unlockLevel=0;
    private int level;


    public ConfigRewardAdapter(List<Map<String,Object>> rewardList,int level){
        this.rewardList=rewardList;
        this.level=level;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view;
        if(viewType==0){
            view=inflater.inflate(R.layout.config_reward_recyclerview,parent,false);
            return new RewardIconViewHolder(view);
        }
        view=inflater.inflate(R.layout.config_reward_recyclerview2,parent,false);
        return new RewardDegreeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Map<String,Object> reward=rewardList.get(position);
        if(!(Boolean) rewardList.get(position).get("type")){
            RewardIconViewHolder viewHolder=(RewardIconViewHolder) holder;
            viewHolder.tvLevel.setText(String.format("・レベル%d達成",reward.get("ex")));
            viewHolder.tvRewardIconQ.setText("新しいアイコン");

            IconWhich icon=new IconWhich((Integer) reward.get("get"));
            viewHolder.imageView.setImageResource(icon.which());

            if((Integer)reward.get("ex")>level){
                viewHolder.tvRewardIconQ.setText("??????");
                viewHolder.imageView.setImageBitmap(null);
            }
        }else{
            RewardDegreeViewHolder viewHolder=(RewardDegreeViewHolder) holder;
            viewHolder.tvLevel.setText(String.format("・レベル%d達成",reward.get("ex")));
            viewHolder.tvDegreeQ.setText("称号:");
            viewHolder.tvDegree.setText((String)reward.get("get"));

            if((Integer)reward.get("ex")>level) {
                viewHolder.tvDegreeQ.setText("?????");
                viewHolder.tvDegree.setText(null);
            }

        }
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if((Boolean) rewardList.get(position).get("type")){
            return 1;
        }
        return 0;

    }

    public static class RewardIconViewHolder extends RecyclerView.ViewHolder {

        private TextView tvLevel;
        private TextView tvRewardIconQ;
        private ImageView imageView;

        public RewardIconViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLevel=itemView.findViewById(R.id.tv_reward_level);
            imageView=itemView.findViewById(R.id.img_config_reward);
            tvRewardIconQ=itemView.findViewById(R.id.tv_reward_icon_text);
        }
    }

    public static class RewardDegreeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvLevel;
        private TextView tvDegreeQ;
        private TextView tvDegree;

        public RewardDegreeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLevel=itemView.findViewById(R.id.tv_reward_degree_level);
            tvDegreeQ=itemView.findViewById(R.id.tv_reward_degree_text);
            tvDegree=itemView.findViewById(R.id.tv_reward_degree_name);

        }
    }

}
