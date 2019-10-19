package com.example.grocerylist.util;

import com.example.grocerylist.R;
import com.example.grocerylist.entities.GroceryList;
import com.example.grocerylist.entities.ListItem;
import com.google.firebase.database.DataSnapshot;

public class EmailUtil {


    public static String buildContent(GroceryList groceryList, DataSnapshot dataSnapshot){
        StringBuilder sb = new StringBuilder();
        sb.append("Grocery list: "+groceryList.getListName());
        sb.append(getLineSeparator());
        sb.append("Due date: " + groceryList.getDueDate());
        sb.append(getLineSeparator());
        sb.append(getLineSeparator());
        sb.append("Items:");
        String[] measurements = MyApplication.getAppContext().getResources().getStringArray(R.array.unit_measure_imperial);
        for(DataSnapshot data: dataSnapshot.getChildren()){
            ListItem listItem = data.getValue(ListItem.class);
            sb.append("- \n\tItem: "+ listItem.getName());
            sb.append("- \n\tQuantity: "+ listItem.getQuantity() + " " + measurements[listItem.getMeasure()]);
            sb.append(getLineSeparator());
        }
        return sb.toString();
    }

    private static String getLineSeparator(){
        return System.getProperty("line.separator");
    }

}
