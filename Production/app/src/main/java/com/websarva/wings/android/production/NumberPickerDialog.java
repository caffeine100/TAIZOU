package com.websarva.wings.android.production;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseRegistrar;
import com.google.firebase.database.FirebaseDatabase;

public class NumberPickerDialog extends DialogFragment {

    private String name;
    private String number;
    private String position;
    private TodoDate todoDate;

    private boolean flag;
    public NumberPickerDialog(String name,String number,String position){
        this.name=name;
        this.number=number;
        this.position=position;
        flag=false;
    }

    public NumberPickerDialog(TodoDate todoDate){
        this.todoDate=todoDate;
        number=String.valueOf(todoDate.getNumber());
        flag=true;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View view=inflater.inflate(R.layout.number_picker_dialog,null,false);
        TextView textView=view.findViewById(R.id.tv_dialog_number);
        NumberPicker numberPicker=view.findViewById(R.id.numberPicker);


        textView.setText(number);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(50);
        numberPicker.setValue(10);
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setView(view);
        builder.setTitle("セット数");
        if(flag){
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int selectedItem=numberPicker.getValue();
                    Change(todoDate,selectedItem);
                    Toast.makeText(getContext(),"変更しました",Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //okを押したときの処理
                    int selectedItem=numberPicker.getValue();
                    Save(name,selectedItem);
                    Toast.makeText(getContext(),"ToDoリストに追加しました",Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void Save(String menu,int frequency){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference();
        String key=reference.push().getKey();

        TodoDate todoDate=new TodoDate(menu,frequency,number,key, false,position);

        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ToDo").child(key).setValue(todoDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                return;
            }
        });
    }

    private void Change(TodoDate todoDate,int frequency){
        FirebaseDatabase database=FirebaseDatabase.getInstance();

        todoDate.setFrequency(frequency);
        DatabaseReference reference=database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("ToDo").child(todoDate.getKey());
        reference.setValue(todoDate);
    }
}
