package com.websarva.wings.android.production;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfigIconChange extends Fragment {

    private List<Map<String,Object>> iconList;

    private TaskCompletionSource<DataSnapshot> dbSource = new TaskCompletionSource<>();
    private Task dbTask = dbSource.getTask();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_icon_change, container, false);


        RecyclerView recyclerView=view.findViewById(R.id.rv_icon_change);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Info");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User user=task.getResult().getValue(User.class);
                LevelUpTable table=new LevelUpTable();
                ConfigIconChangeAdapter adapter=new ConfigIconChangeAdapter(CreateIconList(),
                        table.levelCheck(user.getExperiencePoint()), user.icon);
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

    private List<Map<String,Object>> CreateIconList(){
        List<Map<String,Object>> iconList=new ArrayList<>();

        Map<String,Object> icon=new HashMap<>();
        icon.put("img",1);
        icon.put("up",30);
        icon.put("ex",0);
        iconList.add(icon);

        icon=new HashMap<>();
        icon.put("img",2);
        icon.put("up",30);
        icon.put("ex",0);
        iconList.add(icon);

        icon=new HashMap<>();
        icon.put("img",3);
        icon.put("up",50);
        icon.put("ex",30);
        iconList.add(icon);

        icon=new HashMap<>();
        icon.put("img",4);
        icon.put("up",70);
        icon.put("ex",50);
        iconList.add(icon);


        icon=new HashMap<>();
        icon.put("img",5);
        icon.put("up",100);
        icon.put("ex",70);
        iconList.add(icon);


        icon=new HashMap<>();
        icon.put("img",6);
        icon.put("up",105);
        icon.put("ex",100);
        iconList.add(icon);
        return iconList;
    }

}