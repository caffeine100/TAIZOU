package com.websarva.wings.android.production;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ListAdapter extends BaseExpandableListAdapter {
    private Activity conText;
    private Map<String, List<String>> parentListItem;
    private List<String> items;

    public  ListAdapter(Activity conText,List<String> items,Map<String,List<String>> parentListItem){
        this.conText=conText;
        this.parentListItem=parentListItem;
        this.items=items;
    }
    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(parentListItem.get(items.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(parentListItem.get(items.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String array=(String) getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater) conText.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater!=null;
            convertView=layoutInflater.inflate(R.layout.tab1_parent_item,null);
        }
        TextView item=convertView.findViewById(R.id.tv_parent);
        item.setText(array);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childName=(String) getChild(groupPosition,childPosition);
        LayoutInflater inflater=conText.getLayoutInflater();

        if(convertView==null){
            convertView=inflater.inflate(R.layout.tab1_child_item,null);
        }
        TextView item=convertView.findViewById(R.id.tv_child);
        item.setText(childName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

