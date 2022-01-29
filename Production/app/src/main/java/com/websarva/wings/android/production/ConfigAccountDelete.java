package com.websarva.wings.android.production;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfigAccountDelete extends Fragment {

    private EditText edPas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_config_account_delete, container, false);
        Button btAccountDelete=view.findViewById(R.id.bt_config_account_delete);
        edPas=view.findViewById(R.id.ed_config_account_delete_pas);


        btAccountDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pas=edPas.getText().toString();
                if(pas.isEmpty()){
                    edPas.setError("入力してください");
                }else {
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                    AuthCredential credential= EmailAuthProvider.getCredential(user.getEmail(),pas);

                    user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            new AlertDialog.Builder(getContext()).setTitle("削除").setMessage("一度削除されると元に戻りません。\nよろしいですか？").setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Navigation.findNavController(view).navigate(R.id.action_configAccountDelete_to_login);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(),"失敗",Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
        });

        return view;
    }
}