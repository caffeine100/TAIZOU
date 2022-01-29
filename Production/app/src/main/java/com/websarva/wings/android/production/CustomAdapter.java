package com.websarva.wings.android.production;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomAdapterViewHolder> {

    public interface OnChangeCheck{
        void onChangeCheck(CheckBox checkBox,TodoDate todoDate);
    }
    public interface OnLongClickListener{
        void onLongClickListener(TodoDate todoDate);
    }

    public interface PopupItemSelectedListener{
        boolean popupItemSelected(TodoDate todoDate,MenuItem item);
    }

    private List<TodoDate> todoList;
    private OnChangeCheck changeListener;
    private OnLongClickListener onLongClickListener;
    private PopupItemSelectedListener popupItemSelectedListener;
    private TodoDate todoDate;

    public TodoDate getToDoData(){
        return todoDate;
    }

    public CustomAdapter(List<TodoDate> todoList,OnChangeCheck changeListener,OnLongClickListener onLongClickListener,PopupItemSelectedListener popupItemSelectedListener){
        this.todoList=todoList;
        this.changeListener=changeListener;
        this.onLongClickListener=onLongClickListener;
        this.popupItemSelectedListener=popupItemSelectedListener;
    }
    //ToDoリスト追加
    public void add(TodoDate todoDate){
        this.todoList.add(todoDate);
    }

    //ToDoリスト削除
    public void remove(TodoDate todoDate){this.todoList.remove(todoDate);}

    public TodoDate getToDoDataKey(String key) {
        for (TodoDate toDoData : todoList) {
            if (toDoData.getKey().equals(key)) {
                return toDoData;
            }
        }
        return null;
    }


    @NonNull
    @Override
    public CustomAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.tab2_recyclerview,parent,false);
        return new CustomAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterViewHolder holder, int position) {
        todoDate=todoList.get(position);

        holder.getCheckBox().setChecked(todoDate.getFlag());
        holder.getTvTitle().setText(todoDate.getMenu());
        String str="";
        if(todoDate.getNumber().equals("web")){
            str="";
        }else if(!Character.isDigit(todoDate.getNumber().charAt(0))){
            str=String.format("%s×%dセット",todoDate.getNumber().substring(0,2),todoDate.getFrequency());
        }else{
            str=String.format("%s回×%dセット",todoDate.getNumber(),todoDate.getFrequency());
        }
        holder.getTvFrequency().setText(str);
        holder.checkedListener(changeListener,todoList.get(holder.getLayoutPosition()));
        holder.LongClickListener(onLongClickListener,todoList.get(position));
        holder.getImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showPopupMenu(v,todoList.get(holder.getLayoutPosition()));
            }
        });

    }




    @Override
    public int getItemCount()
    {
        return todoList.size();
    }

    public class CustomAdapterViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener{
        private TextView tvTitle;
        private TextView tvFrequency;
        private CheckBox checkBox;
        private ImageButton imageButton;
        private PopupMenu popupMenu;

        public CustomAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle=itemView.findViewById(R.id.tv_tab2_title);
            tvFrequency=itemView.findViewById(R.id.tv_tab2_frequency);
            checkBox=itemView.findViewById(R.id.checkbox);
            imageButton=itemView.findViewById(R.id.imb_menu);
         //   imageButton.setOnClickListener(this);

        }
        private TextView getTvTitle(){return tvTitle;}
        private TextView getTvFrequency(){return tvFrequency;}
        private CheckBox getCheckBox(){return checkBox;}
        private ImageButton getImageButton(){ return  imageButton;}
        private PopupMenu getPopupMenu(){return popupMenu;}

        private void checkedListener(final OnChangeCheck listener,TodoDate todoDate){
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChangeCheck(checkBox,todoDate);
                }
            });
        }

        private void LongClickListener(final OnLongClickListener listener,TodoDate todoDate){
          tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                  listener.onLongClickListener(todoDate);
                  return false;
              }
          });
        }


        private void showPopupMenu(View view,TodoDate todoDate) {
            popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(new MenuItemClickListener(todoDate));
            popupMenu.show();
        }
        private class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
            TodoDate todoDate;
            public MenuItemClickListener(TodoDate todoDate){
                this.todoDate=todoDate;
            }
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return popupItemSelectedListener.popupItemSelected(todoDate,item);
            }
        }


    }

}
