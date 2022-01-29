package com.websarva.wings.android.production;

public class IconWhich {
    private int icon;
    public IconWhich(int icon){
        this.icon=icon;
    }

    public int which(){
        if(icon==1){
            return R.drawable.img_icon1;
        }else if(icon==2){
            return R.drawable.img_icon2;
        }else if(icon==3){
            return R.drawable.img_icon3;
        }else if(icon==4){
            return R.drawable.img_icon4;
        }else if(icon==5){
            return R.drawable.img_icon5;
        }else{
            return R.drawable.img_icon6;
        }
    }

}
