package com.websarva.wings.android.production;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Login extends Fragment {

    private EditText edEmail,edPassword;
    private Button btLogin;
    private TextView tvRegister;
    private ProgressBar pbLoading;
    private FirebaseAuth mAuth;
    private FragmentActivity activity;
    private OnBackPressedCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        //View取得
        edEmail=view.findViewById(R.id.edEmail);
        edPassword=view.findViewById(R.id.edPassword);
        btLogin=view.findViewById(R.id.btLogin);
        tvRegister=view.findViewById(R.id.tvSignUp);
        pbLoading=view.findViewById(R.id.progressBar);
        //認証
        mAuth=FirebaseAuth.getInstance();
        //フラグメントアクティビティ取得
        activity=getActivity();

        //ログインボタン処理
        btLogin.setOnClickListener(new LoginOnClickListener());
        //新規登録ボタン処理
        tvRegister.setOnClickListener(new SignUpOnClickListener());

        callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(),callback);


        return view;
    }

    private class LoginOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){

            String email=edEmail.getText().toString();
            String password=edPassword.getText().toString();

            //未入力・正しくない
            if(email.isEmpty()){
                edEmail.setError("メールアドレスを入力してください");
                edEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edEmail.setError("正しく入力してください");
                edEmail.requestFocus();
                return;
            }
            if(password.isEmpty()){
                edEmail.setError("パスワードを入力してください");
                edEmail.requestFocus();
                return;
            }
            //処理中
            pbLoading.setVisibility(View.VISIBLE);

            //今日の日付を取得
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = new Date(System.currentTimeMillis());

            //認証処理
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //ホームにリダイレクト
                        pbLoading.setVisibility(View.GONE);
                        //firebaseの情報取得
                        final DatabaseReference firebaseDatabase= FirebaseDatabase.getInstance().getReference();

                        firebaseDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    //1日一回実行（Todoのチェック済み削除）
                                    firebaseDatabase.child("Date").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            String previousDate=String.valueOf(task.getResult().getValue());
                                            if(!df.format(nowDate).equals(previousDate)){
                                                firebaseDatabase.child("Date").setValue(df.format(nowDate));
                                                firebaseDatabase.child("ToDo").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot postSnapshot:snapshot.getChildren()){
                                                            TodoDate todoDate=postSnapshot.getValue(TodoDate.class);
                                                            if(todoDate.getFlag()){
                                                                firebaseDatabase.child("ToDo").child(todoDate.getKey()).removeValue();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }

                                        }
                                    });

                                    //ユーザ情報取得
                                    User user=task.getResult().getValue(User.class);
                                    callback.remove();
                                    Navigation.findNavController(view).navigate(R.id.action_login_to_home2);
                                }
                            }});
                    }else{
                        Toast.makeText(activity,"メールアドレスまたはパスワードが違います",Toast.LENGTH_LONG).show();
                        pbLoading.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    private class SignUpOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            callback.remove();
            Navigation.findNavController(view).navigate(R.id.action_login_to_signUp);
        }
    }



}