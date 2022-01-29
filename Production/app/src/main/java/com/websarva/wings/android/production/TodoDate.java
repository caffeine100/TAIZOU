package com.websarva.wings.android.production;

public class TodoDate {
    public String menu;
    public int frequency;
    public String number;
    public String key;
    public Boolean flag;
    public String position;

    public TodoDate(){

    }

    public TodoDate(String menu,int frequency,String number,String key,Boolean flag,String position) {
        this.position=position;
        this.menu = menu;
        this.frequency=frequency;
        this.number=number;
        this.key=key;
        this.flag=flag;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getFlag(){return  flag;}

    public void setFlag(Boolean flag){this.flag=flag;}

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
