package com.websarva.wings.android.production;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BottomSheetDialog extends DialogFragment {
    private String url;
    private TodoDate todoDate;
    private boolean flag;
    private EditText editText;

    public BottomSheetDialog(String url){
        this.url=url;
        flag=false;
    }

    public BottomSheetDialog(TodoDate todoDate){
        this.todoDate=todoDate;
        flag=true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View view=inflater.inflate(R.layout.web_dialog,null,false);
        editText=view.findViewById(R.id.edMenuName);


        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(view);

        builder.setTitle("ToDoへ");
        if(flag){
            editText.setText(todoDate.getMenu());
            builder.setPositiveButton("変更", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String edMenu=editText.getText().toString();
                    if(edMenu.isEmpty()){
                        new AlertDialog.Builder(getContext()).setTitle("入力エラー").setMessage("入力して下さい").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                    }else{
                        Change(todoDate,edMenu);
                        Toast.makeText(getContext(),"変更しました",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            builder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String ed=editText.getText().toString();
                    if(ed.isEmpty()){
                        new AlertDialog.Builder(getContext()).setTitle("入力エラー").setMessage("未入力があります").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                    }else{
                        Save(ed,url);
                        Toast.makeText(getContext(),"ToDoリストに追加しました",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        return builder.create();
    }

    @Override
    public void onPause() {
        super.onPause();

        // onPause でダイアログを閉じる場合
        dismiss();
    }

    private void Save(String menu,String position){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference();
        String key=reference.push().getKey();
        int frequency=0;
        String number="web";


        TodoDate todoDate=new TodoDate(menu,frequency,number,key, false,position);

        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ToDo").child(key).setValue(todoDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                return;
            }
        });
    }

    private void Change(TodoDate todoDate,String menu){
        FirebaseDatabase database=FirebaseDatabase.getInstance();

        todoDate.setMenu(menu);
        DatabaseReference reference=database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("ToDo").child(todoDate.getKey());
        reference.setValue(todoDate);
    }
}
