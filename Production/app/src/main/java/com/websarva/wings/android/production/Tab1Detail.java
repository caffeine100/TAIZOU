package com.websarva.wings.android.production;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Tab1Detail extends Fragment {

    private StorageReference storage;
    private ImageSliderAdapter imageSliderAdapter;
    private List<Bitmap> image;
    private ViewPager2 viewPager2;
    private ProgressBar pbImgLoading;
    private String name;
    private String[] explanation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tab1_detail, container, false);

        Button btTodoAdd=view.findViewById(R.id.bt_todo_add);
        btTodoAdd.setOnClickListener(new onClickListener());
        viewPager2=view.findViewById(R.id.viewPager2);
        pbImgLoading=view.findViewById(R.id.pb_img_loading);
        pbImgLoading.setVisibility(View.VISIBLE);
        String position=Tab1DetailArgs.fromBundle(getArguments()).getPosition();

        //todoタブからのアクセス時非表示にする
        if(position=="null"){
            btTodoAdd.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvMenuName=view.findViewById(R.id.tv_menu_name);
        TextView tvTrainingDescription=view.findViewById(R.id.tv_training_description);
        TextView tvKnowledge=view.findViewById(R.id.tv_knowledge);

        name= Tab1DetailArgs.fromBundle(getArguments()).getItemName();
        explanation=Tab1DetailArgs.fromBundle(getArguments()).getExplanation();

        tvTrainingDescription.setMovementMethod(new ScrollingMovementMethod());
        tvMenuName.setText("・"+name);
        tvTrainingDescription.setText(explanation[0]);
        tvKnowledge.setText(explanation[1]);

        //ViewPager2の設定
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.20f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        //firebase storageから画像取得
        image=new ArrayList<>();
        List<String> sort=new ArrayList<>();
        for(int i=3;i<=explanation.length-1;i++){
            Log.i("ltd",explanation[i]);
            storage= FirebaseStorage.getInstance().getReference("images/"+explanation[i]);
            try {
                File localFile=File.createTempFile(explanation[i],".png");

                storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        sort.add( localFile.getAbsolutePath());

                        Log.i("ltd",String.valueOf(sort.size())+"::"+explanation.length);
                        if(sort.size()==(explanation.length-3)){
                            Collections.sort(sort);
                            for(String s:sort){
                                image.add(BitmapFactory.decodeFile(s));
                            }
                            imageSliderAdapter=new ImageSliderAdapter(image);
                            viewPager2.setAdapter(imageSliderAdapter);
                            pbImgLoading.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Ltd","失敗");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Ltd","catchされた");
            }
        }
    }

    private class onClickListener implements View.OnClickListener{



        @Override
        public void onClick(View view){

            String position=Tab1DetailArgs.fromBundle(getArguments()).getPosition();
            NumberPickerDialog numberPickerDialog=new NumberPickerDialog(name,explanation[2],position);
            numberPickerDialog.show(getActivity().getSupportFragmentManager(),"NumberPickerDialog");

        }
    }
}