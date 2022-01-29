package com.websarva.wings.android.production;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.LongFunction;
import java.util.logging.LogRecord;

public class Begin extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_begin, container, false);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                //今日の日付を取得
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date nowDate = new Date(System.currentTimeMillis());

                //ログインされているか
                if (mAuth.getCurrentUser() != null) {
                    //firebaseの情報取得
                    final DatabaseReference fireDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //1日一回実行（Todoのチェック済み削除）
                    fireDatabase.child("Date").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            String previousDate = String.valueOf(task.getResult().getValue());

                            if (!df.format(nowDate).equals(previousDate)) {// if (!df.format(nowDate).equals(previousDate))
                                fireDatabase.child("Date").setValue(df.format(nowDate));
                                fireDatabase.child("ToDo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        for (DataSnapshot postSnapshot: task.getResult().getChildren())     {
                                            TodoDate todoDate = postSnapshot.getValue(TodoDate.class);
                                            if (todoDate.getFlag()) {
                                                fireDatabase.child("ToDo").child(todoDate.getKey()).removeValue();
                                            }
                                        }
                                    }
                                });

                            }

                        }
                    });

                    fireDatabase.child("Info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                User data = task.getResult().getValue(User.class);
                               Navigation.findNavController(view).navigate(R.id.action_begin_to_home2);

                            }
                        }
                    });
                } else {
                    Navigation.findNavController(view).navigate(R.id.action_begin_to_login);
                }
            }
        }, 2000);

        return view;
    }

}