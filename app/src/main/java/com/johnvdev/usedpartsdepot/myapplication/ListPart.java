package com.johnvdev.usedpartsdepot.myapplication;

public class ListPart {
    private String id;
    private String title, shortDesc;
    private double rating;
    private double price;
    private int image;

    public ListPart(String id, String title, String shortDesc, double rating, double price, int image) {
        this.id = id;
        this.title = title;
        this.shortDesc = shortDesc;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public double getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
