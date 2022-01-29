package com.websarva.wings.android.production;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfigNameChange extends Fragment {

    private EditText edName;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_config_name_change, container, false);

        edName=view.findViewById(R.id.ed_config_new_name);
        button=view.findViewById(R.id.bt_config_name_change_ok);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        User user =task.getResult().getValue(User.class);
                        user.setUserName(String.valueOf(edName.getText()));
                        reference.child("Info").setValue(user);
                        Toast.makeText(getContext(),"変更しました",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }
}