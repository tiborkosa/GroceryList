package com.example.grocerylist.ui.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.grocerylist.R;
import com.example.grocerylist.util.MeasureUtil;
import com.example.grocerylist.entities.ListItem;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.grocerylist.util.Constants.DLG_TITLE;
import static com.example.grocerylist.util.Constants.ITEM_ID;
import static com.example.grocerylist.util.Constants.ITEM_NAME;
import static com.example.grocerylist.util.Constants.ITEM_POSITION;
import static com.example.grocerylist.util.Constants.ITEM_QUANTITY;
import static com.example.grocerylist.util.Constants.ITEM_UNIT_OF_MEASURE;

/**
 * New list item dialog is used to create new or edit list items
 *
 * NOTE: DialogSubmitListener interface needs to be implemented in the calling class
 */
public class NewListItemDialog extends DialogFragment {

    /**
     * DialogSubmitListener interface to add the list item
     * if position is passed in the item was edited
     */
    public interface DialogSubmitListener {
        void onDialogSubmit(ListItem listItem, int position);
    }

    @Nullable
    @BindView(R.id.tv_dlg_item_name) EditText mListItemName;
    @Nullable @BindView(R.id.tv_dialog_qty) EditText mQuantity;
    @Nullable @BindView(R.id.spinner_dlg_unit) Spinner mMeasureUnit;
    @Nullable @BindView(R.id.btn_dlg_add)
    Button mSubmit;

    // callback
    private DialogSubmitListener callback;

    /**
     * Getting the new instance of the dialog
     * @param args holds the passed in data if the item is edited
     * @param callback implemented DialogSubmitListener interface
     * @return the new fragment
     */
    public static NewListItemDialog newInstance(Bundle args, DialogSubmitListener callback){
        NewListItemDialog frag = new NewListItemDialog();
        frag.callback = callback;
        frag.setArguments(args);
        return frag;
    }

    /**
     * onCreateView
     * @param inflater Layout inflater
     * @param container where we want to inflate the dialog
     * @param savedInstanceState bundle of the saved instance state
     * @return inflated view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_new_gl_item,null);
        ButterKnife.bind(this, view);

        String[] measuringUnits = MeasureUtil.getMeasurements();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item, measuringUnits);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeasureUnit.setAdapter(adapter);

        return view;
    }

    /**
     * onViewCreated to set up the fields
     * @param view inflated view
     * @param savedInstanceState saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(getArguments().getString(DLG_TITLE, "Dialog"));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogWindowAnim;

        mListItemName.setText(getArguments().getString(ITEM_NAME ,""));
        double q = getArguments().getDouble(ITEM_QUANTITY, 0);
        if(q != 0)
            mQuantity.setText(String.valueOf(q));
        mMeasureUnit.setSelection(getArguments().getInt(ITEM_UNIT_OF_MEASURE,0));

        if(getArguments().containsKey(ITEM_POSITION)){
            mSubmit.setText("Edit");
        }
    }

    /**
     * on adding new item
     * Binded by Butterknife onclick
     * calles the callback and passes in the newly created or updated list item
     */
    @Nullable @OnClick(R.id.btn_dlg_add)
    public void onAdd(){
        String itemName = mListItemName.getText().toString();
        String quantity = mQuantity.getText().toString();
        int measure = mMeasureUnit.getSelectedItemPosition();
        double m=0;
        try{
            m = Double.parseDouble(quantity);
        } catch (Exception e){
            Log.e("RR", "error parsing " + e.getMessage());
            return;
        }

        if(itemName.trim().isEmpty()){
            displaySnackBar("List name cannot be empty");
        } else if(quantity.trim().isEmpty()){
            displaySnackBar("Quantity is not set!");
        } else {
            ListItem listItem = new ListItem(itemName, m, measure);
            listItem.setId(getArguments().getString(ITEM_ID, null));
            int position = getArguments()
                    .containsKey(ITEM_POSITION) ? getArguments().getInt(ITEM_POSITION) : -1;
            DialogSubmitListener listener = (DialogSubmitListener) getTargetFragment();
            listener.onDialogSubmit(listItem, position);
            dismiss();
        }
    }

    /**
     * Closing the dialog
     */
    @Nullable @OnClick(R.id.btn_dlg_cancel)
    public void onCancelDialog(){
        dismiss();
    }

    /**
     * helper function to display snackbar messages
     * @param message
     */
    private void displaySnackBar(String message){
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

}
