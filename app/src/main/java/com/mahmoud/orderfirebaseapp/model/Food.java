package com.mahmoud.orderfirebaseapp.model;

public class Food {
    private String Name ,Image ,Description,Prise,Discount,MenuId;

    public Food( ) {

    }

    public Food(String name, String image, String description, String prise, String discount, String menuId) {
        Name = name;
        Image = image;
        Description = description;
        Prise = prise;
        Discount = discount;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrise() {
        return Prise;
    }

    public void setPrise(String prise) {
        Prise = prise;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    @Override
    public String toString() {
        return "Food{" +
                "Name='" + Name + '\'' +
                ", Image='" + Image + '\'' +
                ", Description='" + Description + '\'' +
                ", Prise='" + Prise + '\'' +
                ", Discount='" + Discount + '\'' +
                ", MenuId='" + MenuId + '\'' +
                '}';
    }
}
