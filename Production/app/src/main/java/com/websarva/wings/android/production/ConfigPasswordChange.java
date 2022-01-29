package com.websarva.wings.android.production;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ConfigPasswordChange extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_config_password_change, container, false);

        EditText edPas=view.findViewById(R.id.ed_config_pas_before);
        EditText edNewPas=view.findViewById(R.id.ed_config_pas_new_pas);
        EditText edNewPasEquals=view.findViewById(R.id.ed_config_pas_new_pas_equals);
        Button button=view.findViewById(R.id.button000);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPas=edPas.getText().toString();
                String newPassStr=edNewPas.getText().toString();
                String newPassEquals=edNewPasEquals.getText().toString();


                if(TextUtils.isEmpty(oldPas)){
                    edPas.setError("入力してください");
                }else if(newPassStr.isEmpty()){
                    edNewPas.setError("入力してください");
                }else if(newPassEquals.isEmpty()){
                    edNewPasEquals.setError("入力してください");
                }else if(!newPassStr.equals(newPassEquals)){
                    Toast.makeText(getContext(),"新しいパスワードが異なっています",Toast.LENGTH_SHORT).show();
                }else{
                    updatePassword(oldPas,newPassStr);
                }
            }

        });

        return view;
    }



    private void updatePassword(String oldPas, String newPassStr) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential authCredential= EmailAuthProvider.getCredential(user.getEmail(),oldPas);

        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPassStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"完了",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"失敗",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"パスワードが違います",Toast.LENGTH_SHORT).show();
            }
        });



    }
}