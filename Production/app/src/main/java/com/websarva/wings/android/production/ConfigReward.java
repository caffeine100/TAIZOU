package com.websarva.wings.android.production;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfigReward extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_config_reward, container, false);


        RecyclerView recyclerView=view.findViewById(R.id.rv_config_reward);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Info");
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User user=task.getResult().getValue(User.class);
                LevelUpTable table=new LevelUpTable();
                ConfigRewardAdapter adapter=new ConfigRewardAdapter(CreateRewardList(),
                        table.levelCheck(user.getExperiencePoint()));
                adapter.setHasStableIds(true);
                recyclerView.setAdapter(adapter);
            }
        });
        return view;
    }

    private List<Map<String,Object>> CreateRewardList(){
        List<Map<String,Object>> rewardList=new ArrayList<>();

        //trueが称号
        //falseがアイコン

        //degree
        Map<String,Object> reward=new HashMap<>();
        reward.put("get",1);
        reward.put("type",false);
        reward.put("up",0);
        reward.put("ex",0);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get",2);
        reward.put("type",false);
        reward.put("up",0);
        reward.put("ex",0);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","始まりの一歩");
        reward.put("type",true);
        reward.put("up",5);
        reward.put("ex",0);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","新米ルーキー");
        reward.put("type",true);
        reward.put("up",10);
        reward.put("ex",5);
        rewardList.add(reward);


        reward=new HashMap<>();
        reward.put("get","駆け出しトレーニー");
        reward.put("type",true);
        reward.put("up",15);
        reward.put("ex",10);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","理想を追い求める者");
        reward.put("type",true);
        reward.put("up",20);
        reward.put("ex",15);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","脂肪を燃やす者");
        reward.put("type",true);
        reward.put("up",25);
        reward.put("ex",20);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","筋肉を好む者");
        reward.put("type",true);
        reward.put("up",30);
        reward.put("ex",25);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get",3);
        reward.put("type",false);
        reward.put("up",35);
        reward.put("ex",30);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","代謝を上げる者");
        reward.put("type",true);
        reward.put("up",40);
        reward.put("ex",35);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","運動不足解消");
        reward.put("type",true);
        reward.put("up",45);
        reward.put("ex",40);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","モチベーションを維持する者");
        reward.put("type",true);
        reward.put("up",50);
        reward.put("ex",45);
        rewardList.add(reward);

        //
        reward=new HashMap<>();
        reward.put("get",4);
        reward.put("type",false);
        reward.put("up",55);
        reward.put("ex",50);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","健康意識が高い者");
        reward.put("type",true);
        reward.put("up",60);
        reward.put("ex",55);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","トレーニー");
        reward.put("type",true);
        reward.put("up",65);
        reward.put("ex",60);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","食生活を変える者");
        reward.put("type",true);
        reward.put("up",70);
        reward.put("ex",65);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get",5);
        reward.put("type",false);
        reward.put("up",75);
        reward.put("ex",70);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","体重が変わった者");
        reward.put("type",true);
        reward.put("up",80);
        reward.put("ex",75);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","上級トレーニー");
        reward.put("type",true);
        reward.put("up",85);
        reward.put("ex",80);
        rewardList.add(reward);
        reward=new HashMap<>();
        reward.put("get","見た目が変わった者");
        reward.put("type",true);
        reward.put("up",90);
        reward.put("ex",85);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","塵も積もれば山となる");
        reward.put("type",true);
        reward.put("up",95);
        reward.put("ex",90);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","筋トレを愛する者");
        reward.put("type",true);
        reward.put("up",100);
        reward.put("ex",95);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get",6);
        reward.put("type",false);
        reward.put("up",105);
        reward.put("ex",100);
        rewardList.add(reward);

        reward=new HashMap<>();
        reward.put("get","頂の景色");
        reward.put("type",true);
        reward.put("up",105);
        reward.put("ex",100);
        rewardList.add(reward);


        return rewardList;
    }
}