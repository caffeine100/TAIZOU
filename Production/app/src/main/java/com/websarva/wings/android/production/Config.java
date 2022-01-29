package com.websarva.wings.android.production;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Config extends Fragment {

    private ProgressBar prExPoint;
    private TextView tvLevel;
    private TextView tvExPoint;
    private TextView tvName;
    private TextView tvDegree;
    private Button btInfo;
    private Button btReward;
    private Button btLogout;
    private ImageView imgIcon;
    private ImageButton imgTwitter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_config, container, false);

        tvName=view.findViewById(R.id.tv_config_name);
        tvDegree=view.findViewById(R.id.tv_config_degree);
        tvLevel=view.findViewById(R.id.tv_config_lv);
        tvExPoint=view.findViewById(R.id.tv_config_experience_point);
        prExPoint=view.findViewById(R.id.pr_config_experience_point);
        btInfo=view.findViewById(R.id.bt_config_info);
        imgIcon=view.findViewById(R.id.img_config_icon);
        imgTwitter=view.findViewById(R.id.img_twitter);
        btReward=view.findViewById(R.id.bt_config_reward);
        btLogout=view.findViewById(R.id.bt_config_logout);

        btInfo.setOnClickListener(new ClickOnListener());
        btReward.setOnClickListener(new ClickOnListener());
        btLogout.setOnClickListener(new ClickOnListener());
        imgTwitter.setOnClickListener(new ClickOnListener());

        DatabaseReference database=FirebaseDatabase.getInstance().getReference().child("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Info");

        database.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                //名前セット
                tvName.setText(user.getUserName());
                //称号セット
                tvDegree.setText(user.getTitle());
                //レベルセット
                LevelUpTable table=new LevelUpTable();
                String lv=String.valueOf(table.levelCheck(user.getExperiencePoint()));
                tvLevel.setText(lv);
                //経験値セット
                table.getExMax(user.getExperiencePoint());
                if(table.getLvMax()<= user.getExperiencePoint()){
                    user.setExperiencePoint(table.getLvMax());
                }
                String s=String.format("%d/%d",user.getExperiencePoint(), table.getExMax(user.getExperiencePoint()));
                tvExPoint.setText(s);
                int p=table.getExMax(user.getExperiencePoint());
                prExPoint.setMax(p);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(table.getExMax(p)!=table.getLvMax()){
                        prExPoint.setMin(table.getExMin(user.experiencePoint));
                    }
                }
                prExPoint.setProgress(user.getExperiencePoint(),true);

                //
                IconWhich icon=new IconWhich(Integer.valueOf(user.getIcon()));
                imgIcon.setImageResource(icon.which());
                //
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private class ClickOnListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            int id=view.getId();
            if(id==R.id.bt_config_info){
                Navigation.findNavController(view).navigate(R.id.action_config2_to_configInfo);
            }
            if(id==R.id.bt_config_reward){
                Navigation.findNavController(view).navigate(R.id.action_config2_to_configReward);
            }
            if(id==R.id.bt_config_logout){
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_config2_to_login);
            }
            if(id==R.id.img_twitter) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());

                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("History")
                        .child(df.format(date)).child("Todo");

                reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        StringBuilder tweetText=new StringBuilder();
                        String hTag;

                        for(DataSnapshot s:task.getResult().getChildren()){
                            TodoDate todoDate=s.getValue(TodoDate.class);
                            tweetText.append(String.format("%sを%dセットこなしました!\n",todoDate.getMenu(),todoDate.getFrequency()));
                        }
                       // tweetText = String.format("%sを%dセットこなしました！\n");//セット数とトレーニング名は変化させる
                        hTag = "#TAIZOU\n";

                        String strTweet = "";
                        String strMessage = tweetText.toString();
                        String strHashTag = hTag;


                        try {
                            strTweet = "http://twitter.com/intent/tweet?text="
                                    + URLEncoder.encode(strMessage, "UTF-8")
                                    + "+"
                                    + URLEncoder.encode(strHashTag, "UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strTweet));
                        startActivity(intent);


                    }
                });
            }
        }
    }
}

