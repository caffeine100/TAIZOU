package com.websarva.wings.android.production;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.LongFunction;

public class Tab3CustomAdapter extends RecyclerView.Adapter<Tab3CustomAdapter.Tab3CustomViewHolder> {

    private List<TodoDate> todoDateList;

    private OnLongClick onLongClick;//


    public interface OnLongClick{
        void onLongClickListener(TodoDate todoDate);
    }//

    public Tab3CustomAdapter(List<TodoDate> todoDateList,OnLongClick onLongClickListener){
        this.todoDateList=todoDateList;
        this.onLongClick=onLongClickListener;
    }


    public void add(TodoDate todoDate){
        todoDateList.add(todoDate);
    }

    public void AllRemove(){
        todoDateList.clear();
    }

    public void remove(TodoDate todoDate){todoDateList.remove(todoDate);}


    public TodoDate getToDoDataKey(String key) {
        for (TodoDate toDoData : todoDateList) {
            if (toDoData.getKey().equals(key)) {
                return toDoData;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public Tab3CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tab3_recyclerview,parent,false);
        return new Tab3CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Tab3CustomViewHolder holder, int position) {
        TodoDate todoDate=todoDateList.get(position);

        holder.getTvName().setText(todoDate.getMenu());
        String str="";
        if(todoDate.getNumber().equals("web")){
            str="";
        } else if(!Character.isDigit(todoDate.getNumber().charAt(0))){
            str=String.format("%s×%dセット",todoDate.getNumber().substring(0,2),todoDate.getFrequency());
        }else{
            str=String.format("%s回×%dセット",todoDate.getNumber(),todoDate.getFrequency());
        }
        holder.getTvNumber().setText(str);
        holder.LongClick(onLongClick,todoDateList.get(position));



    }

    @Override
    public int getItemCount() {
        return todoDateList.size();
    }

    public class Tab3CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvNumber;

        public Tab3CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tv_history_name);
            tvNumber=itemView.findViewById(R.id.tv_history_frequency);

            tvName.setText("");
            tvNumber.setText("");
        }

        private TextView getTvName(){
            return tvName;
        }
        private TextView getTvNumber(){
            return tvNumber;
        }


        private void LongClick(final OnLongClick listener, TodoDate todoDate){
            tvName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClickListener(todoDate);
                    return false;
                }
            });
        }

    }

}
