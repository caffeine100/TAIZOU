package com.websarva.wings.android.production;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tab1 extends Fragment {

    private List<String> childList,parentList;
    private Map<String,List<String>> parentListItem;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private FloatingActionButton actionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_tab1, container, false);

        actionButton=view.findViewById(R.id.floating_action_bt);//


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment sheetDialog=new BottomSheetFragment();
                sheetDialog.show(getParentFragmentManager(),"Tag");
            }
        });



        parentList=new ArrayList<>();
        parentList.add("胸筋");
        parentList.add("背筋");
        parentList.add("腹筋");
        parentList.add("腕・肩");
        parentList.add("足");

        String[] pectoral={
                "プッシュアップ","ワイドプッシュアップ","バックプッシュアップ","パイクプッシュアップ","デクラインプッシュアップ","膝つき腕立て伏せ","ディップス"
        };
        String[] spine={
                "バックエクステンション", "バードドック","リバースエルボープッシュアップ", "リバーススノウエンジェル" ,"順手懸垂" ,"逆手懸垂" ,"ナローグリップチンニング","ワイドグリップチンニング ","Ｌ字懸垂"
        };
        String[] abdominal={
                "シットアップ", "トゥータッチ","クランチ","バイシクルクランチ","サイドクランチ","ヒールタッチクランチ" ,"ロシアンツイスト" ,"レッグレイズ","腹筋ローラー"
        };
        String[] armShoulder={
                "ナロープッシュアップ","リバースプッシュアップ","プッシュアップ","ワイドプッシュアップ","バックプッシュアップ","パイクプッシュアップ" ,"デクラインプッシュアップ ","膝つき腕立て伏せ","ディップス" ,"順手懸垂","逆手懸垂","ナローグリップチンニング ","ワイドグリップチンニング","Ｌ字懸垂"
        };
        String[] leg={
                "スクワット","ワイドスクワット","ジャンピングスクワット","ブルガリアンスクワット","スタンディングカーフレイズ"
        };


        parentListItem=new LinkedHashMap<>();
        for(String i:parentList){
            if(i.equals("胸筋")){
                LoadChild(pectoral);
            }else if(i.equals("背筋")){
                LoadChild(spine);
            }
            else if(i.equals("腹筋")){
                LoadChild(abdominal);
            }else if(i.equals("腕・肩")){
                LoadChild(armShoulder);
            }
            else{
                LoadChild(leg);
            }

            parentListItem.put(i,childList);
        }

        FragmentActivity fragmentActivity=getActivity();
        expandableListView=view.findViewById(R.id.expandableListView);
        expandableListAdapter=new ListAdapter(fragmentActivity,parentList,parentListItem);

        expandableListView.setOnChildClickListener(new  ExpandableListOnClickListener());

        //

        //
        expandableListView.setAdapter(expandableListAdapter);

        return view;
    }

    private class ExpandableListOnClickListener implements ExpandableListView.OnChildClickListener{
        @Override
        public boolean onChildClick(ExpandableListView parent,View view,int groupPosition,int childPosition,long id){

            Resources resources=getResources();
            TypedArray typedArray;
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
            int menu=typedArray.getResourceId(childPosition,0);
            typedArray.recycle();

            String[] str=resources.getStringArray(menu);
            String position=String.valueOf(groupPosition)+String.valueOf(childPosition);

            String c=(String) expandableListAdapter.getChild(groupPosition,childPosition);
            HomeDirections.ActionHome2ToTab1Detail action=HomeDirections.actionHome2ToTab1Detail(str);
            action.setItemName(c);
            action.setPosition(position);

            Navigation.findNavController(view).navigate(action);

            return true;
        }
    }

    private void LoadChild(String[] parentElementName) {
        childList=new ArrayList<>();
        Collections.addAll(childList,parentElementName);
    }
}