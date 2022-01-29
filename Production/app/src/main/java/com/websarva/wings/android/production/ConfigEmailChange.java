package com.websarva.wings.android.production;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ConfigEmailChange extends Fragment {

    private EditText edNewEmail;
    private EditText edPas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_config_email_change, container, false);

        TextView tvEmail=view.findViewById(R.id.tv_config_email_before);
        edNewEmail=view.findViewById(R.id.ed_config_email_after);
        edPas=view.findViewById(R.id.ed_config_email_change_Pas);
        Button button=view.findViewById(R.id.bt_config_email_change_ok);

        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail=edNewEmail.getText().toString();
                String pas=edPas.getText().toString();

                if(newEmail.isEmpty()){
                    edNewEmail.setError("入力してください");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
                    edNewEmail.setError("正しく入力してください");
                }else if(pas.isEmpty()){
                    edPas.setError("入力してください");
                }else{
                    updateEmail(pas,newEmail);
                }
            }
        });
        return view;
    }

    private void updateEmail(String pas, String newEmail) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),pas);

       user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused) {
               user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                    new AlertDialog.Builder(getContext()).setTitle("完了").setMessage("変更しました").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edNewEmail.setText(null);
                            edPas.setText(null);
                        }
                    }).show();
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