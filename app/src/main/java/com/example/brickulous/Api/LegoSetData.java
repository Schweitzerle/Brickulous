package com.example.brickulous.Api;

public class LegoSetData {

    String setNumb, name, imageURL, setURL;
    int year, themeID, numbOfParts;

    public LegoSetData(String setNumb, String name, String imageURL, String setURL, int year, int themeID, int numbOfParts) {
        this.setNumb = setNumb;
        this.name = name;
        this.imageURL = imageURL;
        this.numbOfParts = numbOfParts;
        this.setURL = setURL;
        this.themeID = themeID;
        this.year = year;
    }

    public LegoSetData() {

    }

    public int getNumbOfParts() {
        return numbOfParts;
    }

    public int getThemeID() {
        return themeID;
    }

    public int getYear() {
        return year;
    }

    public String getSetURL() {
        return setURL;
    }

    public void setNumbOfParts(int numbOfParts) {
        this.numbOfParts = numbOfParts;
    }

    public void setSetURL(String setURL) {
        this.setURL = setURL;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSetNumb() {
        return setNumb;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setSetNumb(String setNumb) {
        this.setNumb = setNumb;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
