package com.vkshoplist.sfilatov96.vkshoplist;

/**
 * Created by sfilatov96 on 29.10.16.
 */
public class ShopListItem {
    String name;
    String quantity;
    String value;
    String listTitle;


    ShopListItem(String name, String quantity,  String value, String listTitle) {
        this.name = name;
        this.quantity = quantity;
        this.value = value;
        this.listTitle = listTitle;
    }
}
