package com.websarva.wings.android.production;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.LongFunction;


public class Tab2 extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private CustomAdapter customAdapter;

    private EditText edBodyWidth;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tab2, container, false);


        edBodyWidth=view.findViewById(R.id.et_body_weight);
        recyclerView=view.findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users").child(FirebaseAuth.getInstance().getUid());


        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration decoration=new DividerItemDecoration(getContext(),new LinearLayoutManager(getContext()).getOrientation());
        decoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.border_recycleview));
        recyclerView.addItemDecoration(decoration);


        customAdapter = new CustomAdapter(new ArrayList<TodoDate>(), new CustomAdapter.OnChangeCheck() {
            boolean flag;

            @Override
            public void onChangeCheck(CheckBox checkBox, TodoDate todoDate) {
                if (checkBox.isChecked()) {
                    flag=false;
                    new AlertDialog.Builder(getContext()).setTitle("完了確認").setMessage("よろしいですか？").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //firebaseに登録処理
                            todoDate.setFlag(true);
                            reference.child("ToDo").child(todoDate.getKey()).setValue(todoDate);
                            Save(todoDate.getMenu(), todoDate.getFrequency(),todoDate.getNumber(), todoDate.getKey(), todoDate.getFlag(), todoDate.getPosition());
                            flag=true;
                        }
                    }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(flag==false){
                                checkBox.setChecked(false);
                            }
                        }
                    }).show();
                } else {
                    flag=false;
                    new AlertDialog.Builder(getContext()).setTitle("取消確認").setMessage("よろしいですか？").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reference.child("Info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    User user=task.getResult().getValue(User.class);
                                    if(todoDate.getNumber().equals("web")){
                                        user.setExperiencePoint(user.getExperiencePoint()-100);
                                    }else{
                                        user.setExperiencePoint(user.getExperiencePoint()-10*todoDate.frequency);
                                    }
                                    reference.child("Info").setValue(user);
                                }
                            });
                            reference.child("History").child(getNowDate()).child("Todo").child(todoDate.getKey()).removeValue();
                            todoDate.setFlag(false);
                            reference.child("ToDo").child(todoDate.getKey()).setValue(todoDate);
                            flag=true;
                        }
                    }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(flag==false){
                                checkBox.setChecked(true);
                            }
                        }
                    }).show();
                }
            }
        }, new CustomAdapter.OnLongClickListener() {
            @Override
            public void onLongClickListener(TodoDate todoDate) {
                if(todoDate.getNumber().equals("web")){
                    //web表示
                    BottomSheetDialogFragment web=new BottomSheetFragment(todoDate.getPosition());
                    web.show(getParentFragmentManager(),"Tag");

                }else{
                    Resources resources = getResources();
                    TypedArray typedArray;

                    int groupPosition = Character.getNumericValue(todoDate.getPosition().charAt(0));
                    int childPosition = Character.getNumericValue(todoDate.getPosition().charAt(1));

                    if(groupPosition==0){
                        typedArray=resources.obtainTypedArray(R.array.胸);
                    }else if(groupPosition==1){
                        typedArray=resources.obtainTypedArray(R.array.背中);
                    }else if(groupPosition==2){
                        typedArray=resources.obtainTypedArray(R.array.腹);
                    }else if(groupPosition==3){
                        typedArray=resources.obtainTypedArray(R.array.腕肩);
                    }else{
                        typedArray=resources.obtainTypedArray(R.array.足);
                    }
                    int menu = typedArray.getResourceId(childPosition, 0);
                    typedArray.recycle();

                    String[] str = resources.getStringArray(menu);

                    String c = todoDate.getMenu();
                    HomeDirections.ActionHome2ToTab1Detail action = HomeDirections.actionHome2ToTab1Detail(str);
                    action.setItemName(c);
                    Navigation.findNavController(view).navigate(action);

                }
            }
        }, new CustomAdapter.PopupItemSelectedListener() {
            @Override
            public boolean popupItemSelected(TodoDate todoDate, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_popup_edit:
                        if(todoDate.flag){
                            return true;
                        }else{
                            if(todoDate.getNumber().equals("web")){
                                BottomSheetDialog dialog=new BottomSheetDialog(todoDate);
                                dialog.show(getActivity().getSupportFragmentManager(),"TAG");
                                return true;
                            }else {
                                NumberPickerDialog numberPickerDialog=new NumberPickerDialog(todoDate);
                                numberPickerDialog.show(getActivity().getSupportFragmentManager(),"NumberPickerDialog");
                                return true;
                            }

                        }
                    case R.id.action_popup_delete:
                        reference.child("ToDo").child(todoDate.getKey()).removeValue();
                        customAdapter.remove(todoDate);
                        Toast.makeText(getContext(), "削除しました", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
        recyclerView.setAdapter(customAdapter);

        reference.child("History").child(getNowDate()).child("Body").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String body=String.valueOf(task.getResult().getValue());
                if(!body.equals("null")){
                    edBodyWidth.setText(body);
                }
            }
        });

        edBodyWidth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(DoubleTryParse(String.valueOf(v.getText()))){
                        double w=Double.valueOf(String.valueOf(v.getText()));
                        reference.child("History").child(getNowDate()).child("Body").setValue(w);
                    }else{
                        v.setError("正しく入力してください");
                    }
                }
                return false;
            }
        });

        reference.child("ToDo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TodoDate todoDate = snapshot.getValue(TodoDate.class);
                customAdapter.add(todoDate);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TodoDate todoDate=snapshot.getValue(TodoDate.class);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d("ToDoActivity", "onChildRemoved:" + snapshot.getKey());
                TodoDate result = snapshot.getValue(TodoDate.class);
                if (result == null) return;

                TodoDate item = customAdapter.getToDoDataKey(result.getKey());
                customAdapter.remove(item);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    private void Save(String menu,int frequency,String number,String key,Boolean flag,String position){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid());

        TodoDate todoDate=new TodoDate(menu,frequency,number,key,flag,position);

        reference.child("History").child(getNowDate()).child("Todo").child(key).setValue(todoDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                return;
            }
        });

        reference.child("Info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User user=task.getResult().getValue(User.class);
                LevelUpTable table=new LevelUpTable();
                int beforeLv=table.levelCheck(user.getExperiencePoint());
                int afterLv;
                if(number.equals("web")){
                    afterLv=table.levelCheck(user.getExperiencePoint()+100);
                    user.setExperiencePoint(user.getExperiencePoint()+100);
                }else{
                    afterLv=table.levelCheck(user.getExperiencePoint()+(10*frequency));
                    user.setExperiencePoint(user.getExperiencePoint()+(10*frequency));
                }
                reference.child("Info").setValue(user);
                if(beforeLv<afterLv){
                    new AlertDialog.Builder(getContext()).setTitle("レベルアップ!").setMessage("レベルが"+afterLv+"になりました!!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }

            }
        });

    }

    public boolean DoubleTryParse(String s){
        try{
            Double.parseDouble(s);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}