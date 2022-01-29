package com.websarva.wings.android.production;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
import java.util.ArrayList;

public class Home extends Fragment {

    private ViewPager2 viewPager;
    private TextView tvCurrentName;
    private TextView tvDegree;
    private TextView tvLevel;
    private ImageView imgIcon;
    private ImageButton imgConf;
    private ProgressBar prLevel;
    private FragmentActivity fragmentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        fragmentActivity=getActivity();
        viewPager=view.findViewById(R.id.viewPager);
        tvCurrentName=view.findViewById(R.id.tvCurrentName);
        tvDegree=view.findViewById(R.id.tvDegree);
        imgIcon=view.findViewById(R.id.img_icon);
        imgConf=view.findViewById(R.id.img_conf);
        prLevel=view.findViewById(R.id.pr_experience_point);
        tvLevel=view.findViewById(R.id.tvLevel);

        //名前セット
        DatabaseReference database=FirebaseDatabase.getInstance().getReference().child("Users").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Info");

        database.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)//progressのアニメイションのため
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                //名前セット
                tvCurrentName.setText(user.getUserName());
                //称号セット
                tvDegree.setText(user.getTitle());
                //レベルセット
                LevelUpTable levelUpTable=new LevelUpTable();
                String le=String.format("Lv:%d",levelUpTable.levelCheck(user.getExperiencePoint()));
                tvLevel.setText(le);
                int p=levelUpTable.getExMax(user.getExperiencePoint());
                prLevel.setMax(p);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(levelUpTable.getExMax(p)!=levelUpTable.getLvMax()){
                        prLevel.setMin(levelUpTable.getExMin(user.experiencePoint));
                    }
                }
                prLevel.setProgress(user.experiencePoint,true);

                //画像セット
                IconWhich icon=new IconWhich(Integer.valueOf(user.getIcon()));
                imgIcon.setImageResource(icon.which());
                

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //設定画面へ
        imgConf.setOnClickListener(new onClickListener());

        FragmentStateAdapter fragmentStateAdapter=new TabAdapter(fragmentActivity);
        viewPager.setAdapter(fragmentStateAdapter);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ArrayList<String> tabName=new ArrayList<>();
        tabName.add("トレーニング");
        tabName.add("ToDo");
        tabName.add("カレンダー");
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabName.get(position))
        ).attach();
    }


    private class onClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Navigation.findNavController(view).navigate(R.id.action_home2_to_config2);
        }
    }

}