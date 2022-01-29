package com.websarva.wings.android.production;

public class User {
    public String userName;
    public String icon;
    public String title;
    public int experiencePoint;
    public int level;


    public  User(){}

    public User(String userName){
        this.userName=userName;
        icon=String.valueOf(1);
        title="始まりの一歩";
        experiencePoint=1;
        level=0;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getExperiencePoint(){ return experiencePoint ; }

    public void setExperiencePoint(int experiencePoint){
        this.experiencePoint=experiencePoint;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
