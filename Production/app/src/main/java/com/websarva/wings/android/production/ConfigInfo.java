package com.websarva.wings.android.production;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ConfigInfo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_config_info, container, false);

        Button button1 = view.findViewById(R.id.bt_config_icon_change);
        Button button2 = view.findViewById(R.id.bt_config_name_change);
        Button button3 = view.findViewById(R.id.bt_config_degree_change);
        Button button4 = view.findViewById(R.id.bt_email_change);
        Button button5 = view.findViewById(R.id.bt_config_pas_change);
        Button button6=view.findViewById(R.id.bt_config_email_change);

        //ボタンを押されたときの動作を持つインスタンスを生成
        ButtonClickListener listener = new ButtonClickListener();

        //ボタンにButtonClickListenerインスタンスをセット
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        return view;
    }

    //ボタンを押されたときの動作を定義
    public class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            //結果を出力するテキストビューを取得
            //TextView color =findViewById(R.id.color_text);

            //押されたボタンのidを取得
            int id = view.getId();

            //押されたボタンのidによって条件分岐
            switch (id) {
                case R.id.bt_config_icon_change:
                    Navigation.findNavController(view).navigate(R.id.action_configInfo_to_configIconChange);
                    break;
                case R.id.bt_config_name_change:
                    Navigation.findNavController(view).navigate(R.id.action_configInfo_to_configNameChange);
                    break;
                case R.id.bt_config_degree_change:
                    Navigation.findNavController(view).navigate(R.id.action_configInfo_to_configDegreeChange);
                    break;
                case R.id.bt_email_change:
                    Navigation.findNavController(view).navigate(R.id.action_configInfo_to_configAccountDelete);
                    break;
                case R.id.bt_config_pas_change:
                    Navigation.findNavController(view).navigate(R.id.action_configInfo_to_configPasswordChange);
                    break;
                case R.id.bt_config_email_change:
                    Navigation.findNavController(view).navigate(R.id.action_configInfo_to_configEmailChange);

            }
        }


    }
}