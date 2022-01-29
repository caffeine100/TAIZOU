package com.websarva.wings.android.production;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
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


public class Tab3 extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private CalendarView calendar;
    private Tab3CustomAdapter adapter;
    private boolean flag;
    private ChildEventListener listener1=null;


    private TextView tvBodyData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        recyclerView=view.findViewById(R.id.tab3_recyclerView);
        calendar=view.findViewById(R.id.calendarView);

        tvBodyData=view.findViewById(R.id.tv_body_data);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration=new DividerItemDecoration(getContext(),new LinearLayoutManager(getContext()).getOrientation());
        decoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.border_recycleview));

        recyclerView.addItemDecoration(decoration);

        adapter=new Tab3CustomAdapter(new ArrayList<TodoDate>(), new Tab3CustomAdapter.OnLongClick() {
            @Override
            public void onLongClickListener(TodoDate todoDate) {


                if(todoDate.getNumber().equals("web")){
                    //web表示
                    BottomSheetDialogFragment web=new BottomSheetFragment(todoDate.getPosition());
                    web.show(getParentFragmentManager(),"Tag");

                }else {
                    Resources resources = getResources();
                    TypedArray typedArray;

                    int groupPosition = Character.getNumericValue(todoDate.getPosition().charAt(0));
                    int childPosition = Character.getNumericValue(todoDate.getPosition().charAt(1));

                    if (groupPosition == 0) {
                        typedArray = resources.obtainTypedArray(R.array.胸);
                    } else if (groupPosition == 1) {
                        typedArray = resources.obtainTypedArray(R.array.背中);
                    } else if (groupPosition == 2) {
                        typedArray = resources.obtainTypedArray(R.array.腹);
                    } else if (groupPosition == 3) {
                        typedArray = resources.obtainTypedArray(R.array.腕肩);
                    } else {
                        typedArray = resources.obtainTypedArray(R.array.足);
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
        });
        recyclerView.setAdapter(adapter);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());

        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("History");


        reference.child(df.format(date)).child("Body").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bodyWeight=String.valueOf(snapshot.getValue());
                if(bodyWeight.equals("null")){
                    tvBodyData.setText("データなし");
                }else{
                    tvBodyData.setText(bodyWeight+"kg");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ChildEventListener listener= reference.child(df.format(date)).child("Todo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TodoDate toDoData = snapshot.getValue(TodoDate.class);
                adapter.add(toDoData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TodoDate todoDate=snapshot.getValue(TodoDate.class);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                TodoDate result = snapshot.getValue(TodoDate.class);
                if (result == null) return;

                TodoDate item = adapter.getToDoDataKey(result.getKey());
                adapter.remove(item);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        flag=true;
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if(flag){
                    flag=false;
                    reference.child(df.format(date)).child("Todo").removeEventListener(listener);
                }else{
                    reference.child("Todo").removeEventListener(listener1);
                }

                String data=String.format("%d-%02d-%02d",year,month+1,dayOfMonth);

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("History");
                reference=reference.child(data);
                reference.child("Body").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            String bodyWeight="";
                            bodyWeight=String.valueOf(task.getResult().getValue());
                            if(bodyWeight.equals("null")){
                                tvBodyData.setText("データなし");
                            }else {
                                tvBodyData.setText(bodyWeight+"kg");
                            }
                        }
                    }
                });
                adapter.AllRemove();
                adapter.notifyDataSetChanged();

                listener1=reference.child("Todo").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        TodoDate toDoData = snapshot.getValue(TodoDate.class);
                        adapter.add(toDoData);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        TodoDate result = snapshot.getValue(TodoDate.class);
                        if (result == null) return;

                        TodoDate item = adapter.getToDoDataKey(result.getKey());
                        adapter.remove(item);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        return view;
    }




}