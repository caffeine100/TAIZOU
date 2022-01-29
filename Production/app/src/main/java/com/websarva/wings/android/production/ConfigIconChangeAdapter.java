package com.websarva.wings.android.production;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigIconChangeAdapter extends RecyclerView.Adapter<ConfigIconChangeAdapter.ConfigIconChangeViewHolder> {

    private List<Map<String,Object>> iconList;
    private int checkPoint=-1;
    private int level;
    private String iconName;
    private boolean flag=false;

    public ConfigIconChangeAdapter(List<Map<String, Object>> iconList,int level,String iconName) {
        this.iconList = iconList;
        this.level=level;
        this.iconName=iconName;
    }

    @NonNull
    @Override
    public ConfigIconChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.config_icon_change_recyclerview,parent,false);
        return new ConfigIconChangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigIconChangeViewHolder holder, int position) {
        Map<String,Object> icon=iconList.get(position);

        holder.textView.setText("Lv."+String.valueOf(icon.get("ex")));
        IconWhich iconWhich=new IconWhich((Integer)icon.get("img"));
        holder.imageView.setImageResource(iconWhich.which());

        if(!flag && iconName.equals(String.valueOf(icon.get("img")))){
            flag=true;
            checkPoint=holder.getAdapterPosition();
        }

        if((Integer)icon.get("ex")>level){
            holder.imageView.setEnabled(false);
            holder.imageView.setImageBitmap(null);
            holder.layout.setBackgroundColor(Color.GRAY);
        }

        if(checkPoint==position){
            holder.layout.setBackgroundResource(R.drawable.test);
        }else{
            holder.layout.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    public class ConfigIconChangeViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        CardView layout;

        public ConfigIconChangeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.text_view);
            layout=itemView.findViewById(R.id.config_icon_change_layout);


            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Info");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPoint=getAdapterPosition();
                    reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            User user=task.getResult().getValue(User.class);
                            Map<String,Object> icon=new HashMap<>();
                            icon=iconList.get(checkPoint);
                            user.setIcon(String.valueOf(icon.get("img")));
                            reference.setValue(user);
                        }
                    });
                    notifyDataSetChanged();
                }
            });
        }
    }
}
