package com.example.brickulous.Api;

public class ThemeData {
    String name;
    int themeID;

    public ThemeData(String name, int themeID) {
        this.name = name;
        this.themeID = themeID;
    }

    public ThemeData() {

    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
