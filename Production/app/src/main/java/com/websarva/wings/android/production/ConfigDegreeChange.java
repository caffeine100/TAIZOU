package com.websarva.wings.android.production;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

public class ConfigDegreeChange extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_config_degree_change, container, false);

        recyclerView=view.findViewById(R.id.rv_degree_name_change);

        DividerItemDecoration decoration=new DividerItemDecoration(getContext(),new LinearLayoutManager(getContext()).getOrientation());
        decoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.border_recycleview));
        recyclerView.addItemDecoration(decoration);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Info");
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User user=task.getResult().getValue(User.class);
                LevelUpTable table=new LevelUpTable();
                ConfigDegreeChangeAdapter adapter=new ConfigDegreeChangeAdapter(CreateDegreeList(),
                        table.levelCheck(user.getExperiencePoint()),user.getTitle());
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

    private List<Map<String,Object>> CreateDegreeList(){
        List<Map<String,Object>> degreeList=new ArrayList<>();

        Map<String,Object> degree=new HashMap<>();

        degree=new HashMap<>();
        degree.put("get","始まりの一歩");
        degree.put("type",true);
        degree.put("up",5);
        degree.put("ex",0);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","新米ルーキー");
        degree.put("type",true);
        degree.put("up",10);
        degree.put("ex",5);
        degreeList.add(degree);


        degree=new HashMap<>();
        degree.put("get","駆け出しトレーニー");
        degree.put("type",true);
        degree.put("up",15);
        degree.put("ex",10);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","理想を追い求める者");
        degree.put("type",true);
        degree.put("up",20);
        degree.put("ex",15);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","脂肪を燃やす者");
        degree.put("type",true);
        degree.put("up",25);
        degree.put("ex",20);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","筋肉を好む者");
        degree.put("type",true);
        degree.put("up",35);
        degree.put("ex",25);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","代謝を上げる者");
        degree.put("type",true);
        degree.put("up",40);
        degree.put("ex",35);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","運動不足解消");
        degree.put("type",true);
        degree.put("up",45);
        degree.put("ex",40);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","モチベーションを維持する者");
        degree.put("type",true);
        degree.put("up",55);
        degree.put("ex",45);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","健康意識が高い者");
        degree.put("type",true);
        degree.put("up",60);
        degree.put("ex",55);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","トレーニー");
        degree.put("type",true);
        degree.put("up",65);
        degree.put("ex",60);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","食生活を変える者");
        degree.put("type",true);
        degree.put("up",75);
        degree.put("ex",65);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","体重が変わった者");
        degree.put("type",true);
        degree.put("up",80);
        degree.put("ex",75);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","上級トレーニー");
        degree.put("type",true);
        degree.put("up",85);
        degree.put("ex",80);
        degreeList.add(degree);
        degree=new HashMap<>();
        degree.put("get","見た目が変わった者");
        degree.put("type",true);
        degree.put("up",90);
        degree.put("ex",85);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","塵も積もれば山となる");
        degree.put("type",true);
        degree.put("up",95);
        degree.put("ex",90);
        degreeList.add(degree);

        degree=new HashMap<>();
        degree.put("get","筋トレを愛する者");
        degree.put("type",true);
        degree.put("up",100);
        degree.put("ex",95);
        degreeList.add(degree);


        degree=new HashMap<>();
        degree.put("get","頂の景色");
        degree.put("type",true);
        degree.put("up",100);
        degree.put("ex",100);
        degreeList.add(degree);
        return degreeList;
    }

}