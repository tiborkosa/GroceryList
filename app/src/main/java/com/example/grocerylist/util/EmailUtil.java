package com.example.grocerylist.util;

import com.example.grocerylist.R;
import com.example.grocerylist.entities.GroceryList;
import com.example.grocerylist.entities.ListItem;
import com.google.firebase.database.DataSnapshot;

/**
 * To build the email body
 */
public class EmailUtil {

    /**
     * build email body
     * @param groceryList that will be shared
     * @param dataSnapshot of the grocery list items from the db
     * @return string value of the parsed data
     */
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

    /**
     * getting system line separator
     * @return
     */
    private static String getLineSeparator(){
        return System.getProperty("line.separator");
    }

}
