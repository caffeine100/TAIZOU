package com.websarva.wings.android.production;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.DrmInitData;
import android.os.Bundle;

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

public class SignUp extends Fragment {
    private EditText edUserName,edRegisterEmail,edRegisterPassword,edConfirmation;
    private Button btSingUp;
    private ProgressBar pbLoading;
    private FirebaseAuth mAuth;
    private FragmentActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sign_up, container, false);
        edUserName=view.findViewById(R.id.edUserName);
        edRegisterEmail=view.findViewById(R.id.edSignUpEmail);
        edRegisterPassword=view.findViewById(R.id.edSignUpPassword);
        edConfirmation=view.findViewById(R.id.edConfirmation);
        btSingUp=view.findViewById(R.id.btSignup);
        pbLoading=view.findViewById(R.id.progressBar2);
        mAuth=FirebaseAuth.getInstance();
        activity=getActivity();

        btSingUp.setOnClickListener(new registerOnClickListener());

        return view;

    }

    private class registerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            registerUser(view);
        }

        private void registerUser(View view) {
            String name=edUserName.getText().toString();
            String email=edRegisterEmail.getText().toString();
            String pass=edRegisterPassword.getText().toString();
            String cPass=edConfirmation.getText().toString();

            if(name.isEmpty()){
                edUserName.setError("?????????????????????????????????");
                edUserName.requestFocus();
                return;
            }
            if(email.isEmpty()){
                edRegisterEmail.setError("?????????????????????????????????????????????");
                edRegisterEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edRegisterEmail.setError("?????????????????????????????????????????????????????????");
                edRegisterEmail.requestFocus();
                return;
            }
            if(pass.isEmpty()){
                edRegisterPassword.setError("??????????????????????????????????????????");
                edRegisterPassword.requestFocus();
                return;
            }
            if(pass.length() < 6){
                edRegisterPassword.setError("6?????????????????????????????????????????????????????????");
                edRegisterPassword.requestFocus();
                return;
            }
            if(cPass.isEmpty()){
                edConfirmation.setError("??????????????????????????????????????????????????????");
                edConfirmation.requestFocus();
                return;
            }
            if(!cPass.equals(pass)){
                edConfirmation.setError("????????????????????????????????????");
                edConfirmation.requestFocus();
                return;
            }


            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());

            pbLoading.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user=new User(name);
                        //????????????????????????
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Info")
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //????????????
                                    final DatabaseReference firebase=FirebaseDatabase.getInstance().getReference();
                                    firebase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Date").setValue(df.format(date));

                                    //firebase???????????????
                                    final DatabaseReference firebaseDatabase= FirebaseDatabase.getInstance().getReference();

                                    firebaseDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if(task.isSuccessful()){
                                                User user1=task.getResult().getValue(User.class);
                                                pbLoading.setVisibility(View.GONE);
                                                new AlertDialog.Builder(getContext()).setTitle("????????????????????????").setMessage("???????????????????????????").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Navigation.findNavController(view).navigate(R.id.action_signUp_to_login);
                                                    }
                                                }).show();
                                            }
                                        }});
                                } else {
                                    Toast.makeText(activity, "?????????????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
                                    pbLoading.setVisibility(View.GONE);
                                }
                            }
                        });
                    }else{
                        Toast.makeText(activity,"??????????????????????????????",Toast.LENGTH_LONG).show();
                        pbLoading.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}