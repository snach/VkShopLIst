package com.vkshoplist.sfilatov96.vkshoplist;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by sfilatov96 on 30.10.16.
 */

public class TableShopListClass extends SugarRecord {
    String name;
    String quantity;
    String value;
    String listTitle;
    private final static String TAG = TableShopListClass.class.getSimpleName();

    public TableShopListClass(){
    }
    public TableShopListClass(ShopListItem shopListItem) {
        this.name = shopListItem.name;
        this.quantity = shopListItem.quantity;
        this.value = shopListItem.value;
        this.listTitle = shopListItem.listTitle;
    }
}