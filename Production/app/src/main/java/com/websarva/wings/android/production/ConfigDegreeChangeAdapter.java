package com.websarva.wings.android.production;

import android.app.AlertDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ConfigDegreeChangeAdapter extends RecyclerView.Adapter<ConfigDegreeChangeAdapter.ConfigDegreeChangeAdapterViewHolder>{

    private List<Map<String,Object>> degreeList;
    private int checkPosition=-1;
    private int level;
    private String degreeName;
    private boolean flag=false;

    public ConfigDegreeChangeAdapter(List<Map<String,Object>> degreeList,int level,String degreeName){
        this.degreeList=degreeList;
        this.level=level;
        this.degreeName=degreeName;
    }

    @NonNull
    @Override
    public ConfigDegreeChangeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.config_degree_change_recyclerview,parent,false);
        return new ConfigDegreeChangeAdapterViewHolder(view);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigDegreeChangeAdapterViewHolder holder, int position) {
        Map<String,Object> degree=degreeList.get(position);

        holder.tvDegree.setText(String.valueOf(degree.get("get")));
        holder.tvEx.setText("Lv."+String.valueOf(degree.get("ex")));

        if(!flag && degreeName.equals(holder.tvDegree.getText())){
            flag=true;
            checkPosition=holder.getAdapterPosition();
        }

        if((Integer)degree.get("ex")>level) {
            Log.i("degree",String.valueOf(level));
            holder.tvDegree.setText("?????");
            holder.tvDegree.setAlpha(0.5f);
            holder.radioButton.setVisibility(View.GONE);
            holder.constraintLayout.setBackgroundColor(Color.BLACK);
        }
        holder.radioButton.setChecked(checkPosition == position);

    }

    @Override
    public int getItemCount() {
        return degreeList.size();
    }

    public class ConfigDegreeChangeAdapterViewHolder extends RecyclerView.ViewHolder{

        private RadioButton radioButton;
        private TextView tvDegree;
        private TextView tvEx;
        private ConstraintLayout constraintLayout;

        public ConfigDegreeChangeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton=itemView.findViewById(R.id.radioButton);
            tvDegree=itemView.findViewById(R.id.tv_degree_name);
            tvEx=itemView.findViewById(R.id.tv_degree_explanation);
            constraintLayout=itemView.findViewById(R.id.degree_card);



            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   checkPosition = getAdapterPosition();
                   DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Info");
                   reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            User user=task.getResult().getValue(User.class);
                            user.setTitle(String.valueOf(tvDegree.getText()));
                            reference.setValue(user);
                        }
                    });
                   notifyDataSetChanged();
                }
            });
        }
    }

}
