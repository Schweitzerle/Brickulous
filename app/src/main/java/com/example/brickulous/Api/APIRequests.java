package com.example.brickulous.Api;

public enum APIRequests {

    GET_COLORS("/api/v3/lego/colors/"),
    GET_ELEMENTS("/api/v3/lego/colors/{id}/"),
    GET_MINIFIGS("/api/v3/lego/minifigs/"),
    GET_PARTS("/api/v3/lego/parts/"),
    GET_PART_CATEGORIES("/api/v3/lego/part_categories/"),
    GET_THEMES("/api/v3/lego/themes/"),
    GET_ALL_SETS("https://rebrickable.com/api/v3/lego/sets/"),
    GET_SET("https://rebrickable.com/api/v3/lego/sets/");

    private String url;

    APIRequests(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }


}
